var res = "resources/";
/*=========================================   *
 *            Controllers                     *
 *=========================================== */
angular.module('gameApp.controllers').controller('gameController', function ($scope, MessageService, $location) {

    /*=========================================   *
     *            Controller Variables            *
     *=========================================== */

    $scope.status = "";
    $scope.range = [1, 2, 3, 4];
    $scope.loginToast = "";
    $scope.pname = ""; // hacky workaround but will do for now
    $scope.np = [2, 3, 4];
    $scope.ais = [];
    $scope.strats = [1, 2, 3];
    $scope.currentDrag; //card id of the currently dragged card, null otherwise.
    $scope.cardId = 0;

    $scope.serverList = [];
    /*=========================================   *
     *        Controller Variables: GameData    *
     *=========================================== */
    $scope.toast = "Just Chilling";
    $scope.joinedGame = false;
    $scope.myPlayerId;
    $scope.players = [];
    $scope.stageZones = {
        stage1: [],
        stage2: [],
        stage3: [],
        stage4: [],
        stage5: []
    };
    $scope.middleCard = "";
    $scope.tryingToPlay = [];
    $scope.numStages = 0;

    /*=========================================   *
     *            Controller Variables: Stage      *
     *=========================================== */
    $scope.bidSlider = {
        value: 200,
        options: {
            floor: 0,
            ceil: 500,
            vertical: true
        }
    }

    /*=========================================   *
     *             Messaging Functions            *
     *=========================================== */

    //Message variables
    $scope.messages = [];
    $scope.message = null; //the message should be a JSON object
    //Types//
    $scope.TYPE_GAME = "GAME";
    $scope.MESSAGETYPES = Object.freeze({
        GAMESTART: 0,
        JOINGAME: 1,
        JOINTOURNAMENT: 2,
        PICKTOURNAMENT: 3,
        TIETOURNAMENT: 4,
        WINTOURNAMENT: 5,
        SHIELDCOUNT: 6,
        RANKUPDATE: 7,
        ADDCARDS: 8,
        FACEDOWNCARD: 9,
        LISTSERVER: 10,
        PLAYCARD: 11,
        SHOWMIDDLECARD: 12,
        JOINEDTOURNAMENT: 13,
        FINISHPICKTOURNAMENT: 14
    });
    $scope.RIGGED = Object.freeze({
        ONE: 0,
        TWO: 1,
        THREE: 2,
        FOUR: 3,
        NORMAL: 4,
        LONG: 5,
        AITOURNAMENT: 6,
        AIQUEST: 7,
        AIQUEST1: 8,
        AIQUEST2: 9,
        GAMEEND: 10
    });
    $scope.ZONE = Object.freeze({
        FACEDOWN: 0,
        FACEUP: 1,
        DISCARD: 2,
        STAGE1: 3,
        STAGE2: 4,
        STAGE3: 5,
        STAGE4: 6,
        STAGE5: 7,
        HAND: 8
    });

    $scope.GAME_STATE = Object.freeze({
        NONE: 0,
        SPONSORQUEST: 1
    });

    $scope.currentState = $scope.GAME_STATE.NONE;
    $scope.uuid = null; //TODO: the uuid for the gamelobby not sure if still need this 

    //Specify all endpoints
    $scope.ep_joinGame = "/app/game.joinGame";
    $scope.ep_listGames = "/app/game.listGames";
    $scope.ep_createGame = "/app/game.createGame";
    $scope.ep_playCardQuestSetup = "/app//game.playCardQuestSetup";
    $scope.ep_sponsorQuest = "/app/game.sponsorQuest";

    /*  IMPORTANT - do not use this function directly. Make a wrapper function that calls this method when you wish to send a message to the server */
    /*  Parameters: endpoints - the endpoint to which we are sending a message to  */
    $scope.addMessage = function (endpoint) {
        MessageService.send($scope.message, endpoint);
        $scope.message = null;
    };

    /* MESSAGING FUNCTIONS THAT SHOULD BE USED IN THE HTML */

    $scope.sendCreateGameClient = function (np, rigType, gName, ais) {
        numP = parseInt(np, 10);
        if (!numP) {
            $scope.showStatus("Select Num Players");
            return;
        }
        if ($scope.ais.length >= numP) {
            $scope.showStatus("Too many AIs");
            return;
        }
        if (!gName) {
            $scope.showStatus("Enter a name for your game");
            return;
        }

        console.log($scope.pname);

        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            numPlayers: numP,
            rigged: $scope.RIGGED.NORMAL,
            playerName: $scope.pname,
            gameName: gName,
            ais: ais,
            java_class: "GameCreateClient"
        }
        $scope.addMessage($scope.ep_createGame);
        $scope.loadInLobby();
    };

    $scope.sendListGamesClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            java_class: "GameListClient",

        };
        $scope.addMessage($scope.ep_listGames);
    };
    $scope.sendGameJoinClient = function (uuid) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            player: 0,
            uuid: uuid,
            playerName: $scope.pname,
            java_class: "GameJoinClient"
        };
        $scope.addMessage($scope.ep_joinGame);
        console.log("got data");
    };

    $scope.sendPlayCardClient = function (from, to, cardID, endpoint) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            player: 0,
            card: cardID,
            zoneFrom: from,
            zoneTo: to,
            java_class: "PlayCardClient"
        };
        console.log("SENDING PLAYCARDCLIENT");
        console.log(message);
        $scope.addMessage(endpoint);
    };

    $scope.sendStoryResponse = function (isAccept) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.SPONSERQUEST,
            sponser: isAccept,
            java_class: "QuestSponsorClient"
        };
        $scope.addMessage($scope.ep_sponsorQuest);
    }


    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    demo();
    async function demo() {
        // wait a bit for socket to be initialized
        await sleep(2000);

        console.log("this");
        console.log(this);
        var that = this;

        MessageService.registerObserverCallback(that, function (message1) {
            message = JSON.parse(message1.body);
            console.log("Processing message: ");
            console.log(message);
            console.log(message.messageType);
            $scope.messages.push(message);
            if (message.messageType === "LISTSERVER") {
                $scope.serverList = message.games;
            }
            if (message.messageType === "GAMESTART") {
                $location.path('/gameboard');
            }
            if (message.messageType === "JOINGAME") {
                $scope.players = []; //reset the array
                var p = message.players; //array of strings that denote the player's name
                for (var i = 0; i < p.length; i++) {
                    var playerInfo = {
                        name: p[i],
                        id: i,
                        hand: [],
                        faceUp: [],
                        faceDown: [],
                        shieldIcon: "", //should probably be the URL or the name of the icon.png
                        rank: null
                    };
                    console.log("playerInfo");
                    console.log(playerInfo);
                    $scope.players.push(playerInfo);
                }
                if ($scope.joinedGame == false) {
                    $scope.myPlayerId = $scope.players.length - 1;
                    $scope.joinedGame = true;
                }
            }
            if (message.messageType === "ADDCARDS") {
                var playerNum = message.player;
                for (var i = 0; i < message.cards.length; i++) {
                    message.cards[i].zone = $scope.ZONE.HAND;
                }
                $scope.players[playerNum].hand = message.cards;
            }
            if (message.messageType === "SHOWMIDDLECARD") {
                $scope.middleCard = message.card;
                console.log($scope.middleCard);
            }

            if (message.messageType === "SPONSERQUEST") {
                $scope.currentState = $scope.GAME_STATE.SPONSORQUEST;
                $scope.toast = "Sponsor " + $scope.middleCard + " ?";
            }
            if (message.messageType === "PICKSTAGES") {
                $scope.currentState = $scope.GAME_STATE.SPONSORQUEST;
                $scope.toast = "Player " + message.player + " is picking stage cards";
                $scope.numStages = message.numStages;
            }

            // TODO all combinations here
            if (message.messageType === "PLAYCARD") {
                console.log("trying to play card");
                console.log("trying to play: ");
                console.log($scope.tryingToPlay);
                for (var i = 0; i < $scope.tryingToPlay.length; i++) {
                    console.log("looping through cards: " + $scope.tryingToPlay[i].value + " " + message.card);
                    if ($scope.tryingToPlay[i].value === message.card) {
                        console.log("same id");
                        console.log(message.zoneTo);
                        if (message.zoneTo === $scope.ZONE.HAND || message.zoneTo === "HAND") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.HAND;
                            $scope.players[$scope.myPlayerId].hand.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                        if (message.zoneTo === $scope.ZONE.STAGE1 || message.zoneTo === "STAGE1") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE1;
                            $scope.stageZones.stage1.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                        if (message.zoneTo === $scope.ZONE.STAGE2 || message.zoneTo === "STAGE2") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE2;
                            $scope.stageZones.stage2.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                        if (message.zoneTo === $scope.ZONE.STAGE3 || message.zoneTo === "STAGE3") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE3;
                            $scope.stageZones.stage3.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                        if (message.zoneTo === $scope.ZONE.STAGE4 || message.zoneTo === "STAGE4") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE4;
                            $scope.stageZones.stage4.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                        if (message.zoneTo === $scope.ZONE.STAGE5 || message.zoneTo === "STAGE5") {
                            $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE5;
                            $scope.stageZones.stage5.push($scope.tryingToPlay[i]);
                            $scope.tryingToPlay.slice(i);
                            break;
                        }
                    }
                }
            }

            console.log("done parsing");
            $scope.$apply();
        });
    }
    /*=========================================   *
     *             Display Functions              *
     *=========================================== */

    $scope.showStatus = function (message) {
        $scope.status = message;
    }

    $scope.showLoginToast = function (message) {
        $scope.loginToast = message;
    }

    $scope.loadLobby = function () {
        $location.path('/lobby');
    }

    $scope.loadInLobby = function () {
        $location.path('/inlobby');
    }

    $scope.enterLobby = function (pn) {
        if (!pn) {
            $scope.showLoginToast("Enter a name");
            return
        } else {
            console.log(pn);
            $scope.pname = pn;
            $scope.loadLobby();
        }
    }

    $scope.addAI = function (np) {
        if (!np) {
            $scope.showStatus("Select Num Players first", $scope.status);
            return;
        }
        l = $scope.ais.length;
        if (l + 1 < np) {
            $scope.ais.push({
                num: l + 1,
                strat: 1
            });
            console.log($scope.ais);
        } else {
            $scope.showStatus("Max AIs", $scope.status);
        }
    }

    $scope.deleteAI = function (num) {
        console.log($scope.ais);
        for (var i = $scope.ais.length - 1; i >= 0; i--) {
            if ($scope.ais[i].num == num) {
                $scope.ais.splice(i, 1);
                return;
            }
            $scope.ais[i].num -= 1;
        }
    }

    $scope.selectShield = function (num) {}

    /*=========================================   *
     *             Dragging Functions             *
     *=========================================== */

    $scope.startDrag = function (event) {
        var cardId = event.currentTarget.id;
        console.log("Start drag on card id - " + cardId);
        $scope.currentDrag = cardId;
    };

    //the event.target returns the div element of the dropped over target. Use event.target.id to get the id of the div.
    $scope.onDrop = function (event) {
        console.log("Trying to drop into zone - " + event.target.id);
        //array of where the currently dragged card originated from
        var originalZone = $scope.findZoneWithCardId($scope.currentDrag);
        var targetZone = $scope.getZone(event.target.id);
        var card = originalZone.map(function (e) {
            console.log(e.id);
            console.log($scope.currentDrag);
            if (e.id == $scope.currentDrag) {
                console.log(e);
                return e;
            }
        })[0];
        console.log("Copying card " + card.name + " of id " + card.id + " to target zone");
        targetZone.push(card);
        console.log("Removing card from original zone");
        var pos = $scope.getIndexOfCardInZone(originalZone, $scope.currentDrag);
        originalZone.splice(pos, 1);
        console.log("position of card to remove: " + pos);
    }

    /*=========================================  *
     *              Helper functions              *
     *=========================================== */
    /*
        Parameters: zone, an array that contains card objects
                    id, a string of the card
        return: index of the card in the zone array
    */
    $scope.getIndexOfCardInZone = function (zone, id) {
        return zone.map(function (e) {
            return e.id
        }).indexOf(id);
    }

    /*
        Parameters: name, the name of the zone in $scope.zones
        return: the zone array corresponding the the passed in name
    */
    $scope.getZone = function (name) {
        var zone;
        switch (name) {
            case "handZone":
                zone = $scope.zones.handZone;
                break;
            case "faceDownZone":
                zone = $scope.zones.faceDownZone;
                break;
            case "faceUpZone":
                zone = $scope.zones.faceUpZone;
                break;
            case "stage1Zone":
                zone = $scope.zones.stage1Zone;
                break;
            case "stage2Zone":
                zone = $scope.zones.stage2Zone;
                break;
            case "stage3Zone":
                zone = $scope.zones.stage3Zone;
                break;
            case "stage4Zone":
                zone = $scope.zones.stage4Zone;
                break;
            case "stage5Zone":
                zone = $scope.zones.stage5Zone;
                break;
        }
        if (zone != null) {
            console.log("returning zone: " + name);
        } else {
            console.log("Could not find zone with name - " + name);
        }
        return zone;
    }

    /*
        Parameters: id, the id of the card
        return: the zone array that contains this card id. Null if it does not exists
    */
    $scope.findZoneWithCardId = function (id) {
        console.log("Trying to find card with id - " + id);
        if ($scope.zoneContainCard(id, $scope.zones.handZone)) {
            console.log("Card id - (" + id + ") is in handZone");
            return $scope.zones.handZone;
        }
        if ($scope.zoneContainCard(id, $scope.zones.faceDownZone)) {
            console.log("Card id - ()" + id + ") is in faceDownZone");
            return $scope.zones.faceDownZone;
        }
        console.log("Card does not exists in any zones");
        return null;
    }

    /* 
        Parameters: id, the card id
                    cardArray, an array that contains cards
        return: true if the cardArray contains a card with id. false otherwise
    */
    $scope.zoneContainCard = function (id, cardArray) {
        for (var i = 0; i < cardArray.length; i++) {
            if (cardArray[i].id == i.toString()) {
                return true;
            }
        }
        return false
    }


    //TODO need to make dropcallback for all stages
    $scope.dropCallback_s1 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE1, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }
    $scope.dropCallback_s1 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE1, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }

    $scope.dropCallback_s2 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE2, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }
    $scope.dropCallback_s3 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE3, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }
    $scope.dropCallback_s4 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE4, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }
    $scope.dropCallback_s5 = function (event, ui) {
        console.log(ui);
        console.log(ui.draggable.scope());
        console.log($scope.players[$scope.myPlayerId].hand);
        console.log(ui.draggable.scope().card.zone);
        console.log(event.currentTarget);
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE1) {
            $scope.stageZones.stage1 = $scope.stageZones.stage1.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage1| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE2) {
            $scope.stageZones.stage2 = $scope.stageZones.stage2.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE3) {
            $scope.stageZones.stage3 = $scope.stageZones.stage3.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage3| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE4) {
            $scope.stageZones.stage4 = $scope.stageZones.stage4.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        } else if (cardFrom == $scope.ZONE.STAGE5) {
            $scope.stageZones.stage5 = $scope.stageZones.stage5.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log("stage2| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.STAGE5, ui.draggable.scope().card.value, $scope.ep_playCardQuestSetup);
    }

});
