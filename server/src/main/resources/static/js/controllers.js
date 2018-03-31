var res = "resources/";
/*=========================================   *
 *            Controllers                     *
 *=========================================== */
angular.module('gameApp.controllers').controller('gameController', function ($scope, MessageService, $location) {

    /*=========================================   *
     *            Controller Variables            *
     *=========================================== */
    $scope.counter = 0;

    $scope.status = "";
    $scope.range = [1, 2, 3, 4];
    $scope.loginToast = "";
    $scope.pname = ""; // hacky workaround but will do for now
    $scope.np = [2, 3, 4];
    $scope.riggedOptions = ["ONE",
		"TWO",
		"THREE",
		"FOUR",
		"NORMAL",
		"LONG",
		"AITOURNAMENT",
		"AIQUEST",
		"AIQUEST1",
		"AIQUEST2",
		"GAMEEND",
		"ONESTAGETOURNAMENT",
		"TWOSTAGETOURNAMENT",
		"ONEHUNDREDSTAGETOURNAMENT",
		"KINGSCALLTOARMS",
		"PROSPERITY"];
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
    $scope.myPlayerId = 0;
    $scope.inGamePlayers;
    $scope.players = [];
    $scope.stageZones = {
        stage1: [],
        stage2: [],
        stage3: [],
        stage4: [],
        stage5: []
    };

    $scope.playerZoneToListMap = {
        'STAGE1': $scope.stageZones.stage1,
        'STAGE2': $scope.stageZones.stage2,
        'STAGE3': $scope.stageZones.stage3,
        'STAGE4': $scope.stageZones.stage4,
        'STAGE5': $scope.stageZones.stage5
    }; // eg {"hand0" : players[0].hand,}
    $scope.middleCard = "";
    $scope.tryingToPlay = [];
    $scope.numStages = 0;


    /*=========================================   *
     *            Controller Variables: Stage      *
     *=========================================== */
    $scope.bidSlider = {
        value: 1,
        options: {
            floor: 0,
            ceil: 13,
            vertical: true,
            step: 1
        }
    };

    /*=========================================   *
     *             Messaging Functions            *
     *=========================================== */

    //Message variables
    $scope.messages = [];
    $scope.message = null; //the message should be a JSON object
    //Types//
    $scope.TYPE_GAME = "GAME";
    $scope.MESSAGETYPES = {
        GAMESTART: 0,
        JOINGAME: 1,
        JOINTOURNAMENT: 2,
        PICKTOURNAMENT: 3,
        WINTOURNAMENT: 4,
        SHIELDCOUNT: 5,
        RANKUPDATE: 6,
        ADDCARDS: 7,
        FACEDOWNCARD: 8,
        LISTSERVER: 9,
        PLAYCARD: 10,
        SHOWMIDDLECARD: 11,
        JOINEDTOURNAMENT: 12,
        FINISHPICKTOURNAMENT: 13,
        SPONSERQUEST: 14,
        JOINQUEST: 15,
        PICKSTAGES: 16,
        PICKQUEST: 17,
        WINQUEST: 18,
        BIDQUEST: 19,
        DISCARDQUEST: 20,
        UPQUEST: 21,
        PLAYCARD: 22,
        HANDDISCARD: 23
    };
    $scope.RIGGED = {
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
        GAMEEND: 10,
        ONESTAGETOURNAMENT: 11,
        TWOSTAGETOURNAMENT: 12,
        ONEHUNDREDSTAGETOURNAMENT: 13,
        KINGSCALLTOARMS: 14,
        PROSPERITY: 15
    };
    $scope.ZONE = {
        FACEDOWN: 0,
        FACEUP: 1,
        DISCARD: 2,
        STAGE1: 3,
        STAGE2: 4,
        STAGE3: 5,
        STAGE4: 6,
        STAGE5: 7,
        HAND: 8
    };

    $scope.GAME_STATE = {
        NONE: 0,
        SPONSORQUEST: 1,
        JOINQUEST: 2,
        PICKSTAGES: 3,
        PICKQUEST: 4,
        BIDQUEST: 5,
        DISCARDQUEST: 6,
        JOINTOURNAMENT: 20,
        PICKTOURNAMENT: 21,
        HANDDISCARD: 22
    };


    $scope.currentState = $scope.GAME_STATE.NONE;
    $scope.uuid = null; //TODO: the uuid for the gamelobby not sure if still need this 

    //Specify all endpoints
    $scope.ep_joinTournament = "/app/game.joinTournament";
    $scope.ep_playCardTournament = "/app/game.playCardTournament";
    $scope.ep_finishSelectingTournament = "/app/game.finishSelectingTournament";

    $scope.ep_joinGame = "/app/game.joinGame";
    $scope.ep_listGames = "/app/game.listGames";
    $scope.ep_createGame = "/app/game.createGame";
    $scope.ep_playCardQuestSetup = "/app//game.playCardQuestSetup";
    $scope.ep_sponsorQuest = "/app/game.sponsorQuest";
    $scope.ep_finishSelectingQuestStages = "/app/game.finishSelectingQuestStages";
    $scope.ep_finishSelectingQuestCards = "/app/game.finishSelectingQuestCards";
    $scope.ep_joinQuest = "/app/game.joinQuest";
    $scope.ep_playForQuest = "/app/game.playForQuest";
    $scope.ep_bid = "/app/game.bid";
    $scope.ep_discardFullHand = "/app/game.discardFullHand";
    $scope.ep_discardBid = "/app/game.discardBid";
    $scope.ep_discardFinish = "/app/game.finishDiscard";
    $scope.ep_finishSelectingDiscardHand = "/app/game.finishSelectingDiscardHand";

    /*  IMPORTANT - do not use this function directly. Make a wrapper function that calls this method when you wish to send a message to the server */
    /*  Parameters: endpoints - the endpoint to which we are sending a message to  */
    $scope.addMessage = function (endpoint) {
        MessageService.send($scope.message, endpoint);
        $scope.message = null;
    };


    /* MESSAGING FUNCTIONS THAT SHOULD BE USED IN THE HTML */
    $scope.sendCreateGameClient = function (np, rigType, gName, ais) {
        var numP = parseInt(np, 10);
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
        console.log($scope.RIGGED[rigType]);
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            numPlayers: numP,
            rigged: $scope.RIGGED[rigType],
            playerName: $scope.pname,
            gameName: gName,
            ais: ais,
            java_class: "GameCreateClient"
        };
        $scope.addMessage($scope.ep_createGame);
        $scope.loadInLobby();
    };

    $scope.sendListGamesClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            java_class: "GameListClient"
        };
        $scope.addMessage($scope.ep_listGames);
    };

    $scope.sendGameJoinClient = function (uuid) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            uuid: uuid,
            playerName: $scope.pname,
            java_class: "GameJoinClient"
        };
        $scope.addMessage($scope.ep_joinGame);
        console.log("got data");
    };

    $scope.sendPlayCardClient = function (from, to, cardID, endpoint) {
        console.log("SENDING PLAYCARDCLIENT");
        console.log(from);
        console.log(to);
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.PLAYCARD,
            card: cardID,
            zoneFrom: from,
            zoneTo: to,
            java_class: "PlayCardClient"
        };
        console.log(message);
        console.log("current state: " + $scope.currentState);
        $scope.addMessage(endpoint);
    };

    //Tell server if we accept/decline the story (in this case it's quest) TODO: make it work for Tournaments as well, should be dependent on the state of the game
    $scope.sendStoryResponse = function (isAccept) {
        console.log($scope.currentState)
        // console.log($scope.GAME_STATE.SPONSORQUEST);
        // console.log($scope.currentState == $scope.GAME_STATE.SPONSORQUEST);
        if ($scope.currentState == $scope.GAME_STATE.SPONSORQUEST) {
            // console.log("hello");
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.SPONSERQUEST,
                sponser: isAccept,
                java_class: "QuestSponsorClient"
            };
            $scope.addMessage($scope.ep_sponsorQuest);
        } else if ($scope.currentState == $scope.GAME_STATE.JOINQUEST) {
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.SPONSERQUEST,
                joined: isAccept,
                java_class: "QuestJoinClient"
            };
            $scope.addMessage($scope.ep_joinQuest);
        } else if ($scope.currentState == $scope.GAME_STATE.JOINTOURNAMENT) {
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.JOINTOURNAMENT,
                player: $scope.myPlayerId,
                joined: isAccept,
                java_class: "TournamentAcceptDeclineClient"
            };
            $scope.addMessage($scope.ep_joinTournament);
        } else if ($scope.currentState == $scope.GAME_STATE.PICKTOURNAMENT) {
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.PICKTOURNAMENT,
                player: $scope.myPlayerId,
                java_class: "PlayCardClient"
            };
            $scope.addMessage($scope.ep_playCardTournament);
        }
    };

    $scope.sendTournamentPickCardsClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.PICKSTAGES,
            java_class: "TournamentFinishPickingClient"
        };
        $scope.addMessage($scope.ep_finishSelectingTournament);
    };

    //Send this message when player is finished picking stages
    $scope.sendQuestPickStagesClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.PICKSTAGES,
            java_class: "QuestPickStagesClient"
        };
        $scope.addMessage($scope.ep_finishSelectingQuestStages);
    };
    //Send this message when player is finished picking stages
    $scope.sendQuestPickCardsClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.PICKQUEST,
            java_class: "QuestPickCardsClient"
        };
        $scope.addMessage($scope.ep_finishSelectingQuestCards);
    };
    $scope.sendQuestBidClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.BIDQUEST,
            java_class: "QuestBidClient",
            bid: $scope.bidSlider.value
        };
        $scope.addMessage($scope.ep_bid);
    };
    $scope.sendFinishDiscard = function () {
        console.log("hello");
        console.log("sendfinishdiscarddddddddddd");
        console.log($scope.currentState);
        if ($scope.currentState === $scope.GAME_STATE.HANDDISCARD) {
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.FINISHDISCARD,
                java_class: "HandFullFinishPickingClient"
            };
            $scope.addMessage($scope.ep_finishSelectingDiscardHand);
        } else if ($scope.currentState === $scope.GAME_STATE.DISCARDQUEST) {
            $scope.message = {
                TYPE: $scope.TYPE_GAME,
                messageType: $scope.MESSAGETYPES.DISCARDQUEST,
                java_class: "QuestDiscardCardsClient"
            };
            console.log("quest bid discarding");
            $scope.addMessage($scope.ep_discardFinish);
        }
    }

    /***************************************************************/
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
            $scope.counter++;
            console.log($scope.counter);
            if (message.messageType === "LISTSERVER") {
                $scope.serverList = message.games;
            }
            if (message.messageType === "GAMESTART") {
                $location.path('/gameboard');
                //map the shit
                for (var i = 0; i < $scope.players.length; i++) {
                    $scope.playerZoneToListMap['HAND' + i] = $scope.players[i].hand;
                    $scope.playerZoneToListMap['FACEUP' + i] = $scope.players[i].faceUp;
                    $scope.playerZoneToListMap['FACEDOWN' + i] = $scope.players[i].faceDown;
                }
            }
            if (message.messageType === "JOINGAME") {
                $scope.inGamePlayers = [];
                for (var i = 0; i < message.players.length; i++) {
                    if (message.players[i] != $scope.pname) {
                        $scope.inGamePlayers.push({
                            name: message.players[i]
                        });
                    }
                }
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
                        rank: null,
                        isSponsoring: false,
                        joinedQuest: false,
                        joinedTourn: false,
                        discardPile: [],
                        needToDiscard: 0,
                        revealStage: [false, false, false, false, false]
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
                    $scope.players[playerNum].hand.push(message.cards[i]);
                    console.log("Adding card: " + message.cards[i].key + " " + message.cards[i].value);
                }
                console.log($scope.playerZoneToListMap);
            }
            //means a new story card has been flipped. we should be clearing everything from the stage panes
            //and making sure everything starts at default again
            if (message.messageType === "SHOWMIDDLECARD") {
                $scope.middleCard = message.card;

                console.log($scope.middleCard);
                console.log("clearing all cards from the stages");
                //we are setting the length to 0 as this will clear the array the most efficient way 
                //while retaining reference to the same array (we need this because our maps are referencing these arrs)
                $scope.stageZones.stage1.length = 0;
                $scope.stageZones.stage2.length = 0;
                $scope.stageZones.stage3.length = 0;
                $scope.stageZones.stage4.length = 0;
                $scope.stageZones.stage5.length = 0;

                //making sure all players are not participating in any quests
                for (var i = 0; i < $scope.players.length; i++) {
                    $scope.players[i].faceDown.length = 0; //shouldn't have any cards in facedown at this point
                    $scope.players[i].isSponsoring = false;
                    $scope.players[i].joinedQuest = false;
                    $scope.players[i].discardPile.length = 0;
                }
                // console.log(players);

            }
            if (message.messageType === "JOINTOURNAMENT") {
                $scope.currentState = $scope.GAME_STATE.JOINTOURNAMENT;
                $scope.toast = "Join Tournament ?";
            }

            if (message.messageType === "JOINEDTOURNAMENT") {
                $scope.players[message.player].joinedTourn = true;
                console.log("Player joined tourn? : " + $scope.players[message.player].joinedTourn);
            }

            if (message.messageType === "PICKTOURNAMENT") {
                //
            }

            if (message.messageType === "SPONSERQUEST") {
                $scope.currentState = $scope.GAME_STATE.SPONSORQUEST;
                console.log($scope.currentState + " " +
                    $scope.GAME_STATE.SPONSORQUEST);
                $scope.toast = "Sponsor " + $scope.middleCard + " ?";
                $scope.$apply();
            }
            if (message.messageType === "PICKSTAGES") {
                $scope.currentState = $scope.GAME_STATE.PICKSTAGES;
                $scope.toast = "Player " + message.player + " is picking stage cards";
                //if your player is picking stages, that means he's sponsoring
                $scope.players[message.player].isSponsoring = true;
                $scope.numStages = message.numStages;
            }
            if (message.messageType === "PICKQUEST") {
                $scope.currentState = $scope.GAME_STATE.PICKQUEST;
                $scope.toast = "Players " + message.players.toString() + " are picking quest cards";
                $scope.players[message.player].joinedQuest = true;
            }
            // TODO all combinations here
            if (message.messageType === "PLAYCARD") {
                console.log("trying to play card");
                console.log("trying to play: ");
                console.log($scope.tryingToPlay);
                //trying to add only works for this player. 
                //if we are trying to see the result from another client, we must search for this card and add it to the respective pane
                if ($scope.myPlayerId == message.player) {
                    for (var i = 0; i < $scope.tryingToPlay.length; i++) {
                        console.log("looping through cards: " + $scope.tryingToPlay[i].value + " " + message.card);
                        if ($scope.tryingToPlay[i].value === message.card) {
                            console.log("same id");
                            console.log(message.zoneTo);
                            if (message.zoneTo === $scope.ZONE.HAND || message.zoneTo === "HAND") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.HAND;
                                $scope.players[$scope.myPlayerId].hand.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.STAGE1 || message.zoneTo === "STAGE1") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE1;
                                $scope.stageZones.stage1.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.STAGE2 || message.zoneTo === "STAGE2") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE2;
                                $scope.stageZones.stage2.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.STAGE3 || message.zoneTo === "STAGE3") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE3;
                                $scope.stageZones.stage3.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.STAGE4 || message.zoneTo === "STAGE4") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE4;
                                $scope.stageZones.stage4.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay = $scope.tryingToPlay.slice(i);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.STAGE5 || message.zoneTo === "STAGE5") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.STAGE5;
                                $scope.stageZones.stage5.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                console.log("STAGE5: ");
                                console.log($scope.stageZones.stage5);
                                break;
                            }
                            if (message.zoneTo === $scope.ZONE.FACEDOWN || message.zoneTo === "FACEDOWN") {
                                $scope.tryingToPlay[i].zone = $scope.ZONE.FACEDOWN;
                                $scope.players[$scope.myPlayerId].faceDown.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                console.log("MY FACEDOWN CARDS: ");
                                console.log($scope.players[$scope.myPlayerId].faceDown);
                                break;
                            }
                            if (message.zoneTo === "DISCARD") {
                                if ($scope.currentState !== $scope.GAME_STATE.HANDDISCARD) {
                                    $scope.currentState = $scope.GAME_STATE.DISCARDQUEST;
                                }
                                $scope.tryingToPlay[i].zone = $scope.ZONE.DISCARD;
                                $scope.players[$scope.myPlayerId].discardPile.push($scope.tryingToPlay[i]);
                                $scope.tryingToPlay.splice(i, 1);
                                console.log("MY DISCARD PILE: ");
                                console.log($scope.players[$scope.myPlayerId].discardPile);
                                break;
                            }
                        }
                    }
                } else {
                    //move the opponents card from pane to pane
                    console.log(message.zoneFrom + message.player);
                    console.log(message.zoneTo + message.player);
                    console.log($scope.playerZoneToListMap);
                    console.log($scope.playerZoneToListMap['STAGE1']);
                    var fromZone;
                    var toZone;
                    var cardToMove;
                    //get the reference to the from/to zones for which we are moving cards to
                    if (message.zoneFrom.includes("STAGE")) {
                        fromZone = $scope.playerZoneToListMap[message.zoneFrom];
                    } else {
                        fromZone = $scope.playerZoneToListMap[message.zoneFrom + message.player];
                    }
                    if (message.zoneTo.includes("STAGE")) {
                        toZone = $scope.playerZoneToListMap[message.zoneTo];
                    } else {
                        toZone = $scope.playerZoneToListMap[message.zoneTo + message.player];
                    }
                    //get reference to the card we want to move
                    for (var i = 0; i < fromZone.length; i++) {
                        if (fromZone[i].value == message.card) {
                            cardToMove = fromZone[i];
                            console.log("removing from zone");
                            console.log(fromZone.toString());
                            fromZone.splice(i, 1); // remove card from the fromZone
                            console.log(fromZone);
                            break;
                        }
                    }
                    //move the card
                    toZone.push(cardToMove);
                }

            }

            if (message.messageType === "JOINQUEST") {
                $scope.currentState = $scope.GAME_STATE.JOINQUEST;
                $scope.toast = "Waiting for players " + message.players.toString() + " to join/decline";
                for (var i = 0; i < message.players.length; i++) {
                    if (message.players[i] == $scope.myPlayerId) {
                        $scope.toast = "Would you like to join the quest? ";
                        break;
                    }
                }
            }
            //TODO:: show the stage card
            if (message.messageType === "UPQUEST") {

            }
            if (message.messageType === "WINQUEST") {
                for (var i = 0; i < $scope.players.length; i++) {
                    if (!message.players.includes(i)) {
                        $scope.players[i].joinedQuest = false;
                    }
                }
                $scope.toast = "Players " + message.players.toString() + " passed the stage";
                console.log($scope.players);
                if (message.type === "WON") {
                    $scope.toast = "Players " + message.players.toString() + " won the quest!";
                }
                if (message.type === "NOSPONSOR") {
                    $scope.toast = "No players sponsored the quest";
                }
            }
            if (message.messageType === "BIDQUEST") {
                $scope.currentState = $scope.GAME_STATE.BIDQUEST;
                $scope.players[message.player].joinedQuest = true; //if the player is bidding, he's in the quest
                $scope.bidSlider.value = message.minBidValue;
                $scope.bidSlider.options.floor = message.minBidValue;
                $scope.bidSlider.options.ceil = message.maxBidValue;
                $scope.toast = "Players are currently bidding";
                console.log($scope.currentState + " " + $scope.GAME_STATE.BIDQUEST);
                console.log($scope.players[$scope.myPlayerId].joinedQuest);
                if ($scope.currentState === $scope.GAME_STATE.BIDQUEST && $scope.players[$scope.myPlayerId].joinedQuest) {
                    console.log("I should be showing the bidslider");
                }
                console.log($scope.currentState === $scope.GAME_STATE.BIDQUEST && $scope.players[$scope.myPlayerId].joinedQuest);
            }
            if (message.messageType === "DISCARDQUEST") {
                $scope.currentState = $scope.GAME_STATE.DISCARDQUEST;
                $scope.toast = "Players are currently discarding due to test";
                if (message.player == $scope.myPlayerId) {
                    $scope.toast = "You need to discard " + message.cardsToDiscard;
                    $scope.players[$scope.myPlayerId].needToDiscard = message.cardsToDiscard; //every player only needs to know how many cards they need to discard themselves (not other players) 
                }
            }
            if (message.messageType === "HANDDISCARD") {
                $scope.currentState = $scope.GAME_STATE.HANDDISCARD;
                if (message.player == $scope.myPlayerId) {
                    $scope.players[$scope.myPlayerId].needToDiscard = message.toDiscard; //every player only needs to know how many cards they need to discard themselves (not other players) 
                    $scope.toast = "Hand too full, select " + $scope.players[$scope.myPlayerId].needToDiscard + " cards to discard";
                }
            }
            if (message.messageType === "FINISHDISCARD") {
                if (message.player == $scope.myPlayerId) {
                    $scope.players[$scope.myPlayerId].discardPile.length = 0;
                    $scope.currentState = $scope.GAME_STATE.PICKTOURNAMENT;
                    $scope.toast = "Select cards for tournament";
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

    $scope.dropCallback_myfd = function (event, ui) {
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        console.log(cardFrom);
        console.log($scope.ZONE.HAND);
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
        }
        if ($scope.currentState == $scope.GAME_STATE.PICKQUEST) {
            console.log(ui.draggable.scope().card.zone.toString() + " " + $scope.ZONE.FACEDOWN.toString());
            $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.FACEDOWN, ui.draggable.scope().card.value, $scope.ep_playForQuest);
        }
        if ($scope.currentState == $scope.GAME_STATE.PICKTOURNAMENT) {
            console.log(ui.draggable.scope().card.zone.toString() + " " + $scope.ZONE.FACEDOWN.toString());
            $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.FACEDOWN, ui.draggable.scope().card.value, $scope.ep_playCardTournament);
        }
    }
    //can only discard cards from your hand
    $scope.dropCallback_discard = function (event, ui) {
        var cardFrom = ui.draggable.scope().card.zone;
        // TODO: take it from the correct place based on ui.draggle.scope().card.zone
        console.log(cardFrom);
        console.log($scope.ZONE.HAND);
        if (cardFrom == $scope.ZONE.HAND) {
            console.log($scope.players[$scope.myPlayerId].hand);
            // change destination based on current state
            $scope.players[$scope.myPlayerId].hand = $scope.players[$scope.myPlayerId].hand.filter(function (e) {
                if (ui.draggable.scope().card.value === e.value) {
                    $scope.tryingToPlay.push(e);
                    console.log($scope.tryingToPlay);
                    console.log("hand| added" + e.toString() + "to trying To Play");
                }
                return ui.draggable.scope().card.value !== e.value;
            });
        }
        if ($scope.currentState == $scope.GAME_STATE.DISCARDQUEST) {
            console.log(ui.draggable.scope().card.zone.toString() + " " + $scope.ZONE.FACEDOWN.toString());
            $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.DISCARD, ui.draggable.scope().card.value, $scope.ep_discardBid);
        }
        if ($scope.currentState == $scope.GAME_STATE.HANDDISCARD) {
            console.log(ui.draggable.scope().card.zone.toString() + " " + $scope.ZONE.FACEDOWN.toString());
            $scope.sendPlayCardClient(ui.draggable.scope().card.zone, $scope.ZONE.DISCARD, ui.draggable.scope().card.value, $scope.ep_discardFullHand);
        }
    }

    //returns true/false if it should show the accept/decline
    $scope.showAcceptDecline = function () {
        // console.log("checking showAcceptDecline");
        console.log($scope.currentState == $scope.GAME_STATE.SPONSORQUEST || ($scope.currentState == $scope.GAME_STATE.JOINQUEST && !$scope.players[$scope.myPlayerId].isSponsoring));
        return $scope.currentState == $scope.GAME_STATE.JOINTOURNAMENT || $scope.currentState == $scope.GAME_STATE.SPONSORQUEST || ($scope.currentState == $scope.GAME_STATE.JOINQUEST && !$scope.players[$scope.myPlayerId].isSponsoring);
    }

    $scope.showDonePickingCards = function () {
        if ($scope.currentState == $scope.GAME_STATE.PICKTOURNAMENT && $scope.players[$scope.myPlayerId].joinedTourn) {
            return true;
        }
        return false;
    }

    $scope.testShow = function () {
        // console.log("Checking display");
        // console.log($scope.currentState);
        // console.log($scope.currentState == $scope.GAME_STATE.SPONSORQUEST);
        return $scope.currentState == $scope.GAME_STATE.SPONSORQUEST;
    }

    //Only show the done setting up button if the player is sponsoring and the state is picking stages
    $scope.showDoneSettingUp = function () {
        // console.log("Checking display");
        // console.log("player: " + $scope.myPlayerId + " is sponsoring? " + $scope.players[$scope.myPlayerId].isSponsoring);
        console.log("current state: " + $scope.currentState);
        return $scope.players[$scope.myPlayerId].isSponsoring && $scope.currentState == $scope.GAME_STATE.PICKSTAGES;
    }
    //only show the bid slider if the player is in a quest and the state is GAME_STATE.BIDQUEST
    $scope.showBidSlider = function () {
        // console.log("Showing bid slider");
        console.log($scope.currentState == $scope.GAME_STATE.BIDQUEST && $scope.players[$scope.myPlayerId].joinedQuest == true);
        return $scope.currentState == $scope.GAME_STATE.BIDQUEST && $scope.players[$scope.myPlayerId].joinedQuest == true;
    }
    $scope.showDonePickingQuestCards = function () {
        // console.log("Check display for show down picking cards");
        return $scope.currentState == $scope.GAME_STATE.PICKQUEST && $scope.players[$scope.myPlayerId].joinedQuest;
    }
    $scope.showBidButton = function () {
        // console.log("check dispaly for show bid button");
        return $scope.currentState == $scope.GAME_STATE.BIDQUEST && $scope.players[$scope.myPlayerId].joinedQuest == true;
    }
    $scope.showDiscardButton = function () {
        // console.log("checking dispaly for discardButton");
        console.log("cards to discard: " + $scope.players[$scope.myPlayerId].needToDiscard);
        console.log($scope.players[$scope.myPlayerId].needToDiscard > 0)
        //        console.log($scope.currentState = GAME_STATE.DISCARDQUEST);
        return $scope.currentState == $scope.GAME_STATE.HANDDISCARD || $scope.currentState == $scope.GAME_STATE.DISCARDQUEST && $scope.players[$scope.myPlayerId].needToDiscard > 0;
    }

});
