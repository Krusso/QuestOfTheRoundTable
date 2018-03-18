angular.module("gameApp", [
    "gameApp.controllers",
    "gameApp.services"
]);

angular.module("gameApp.controllers", ['ngDragDrop']);
angular.module("gameApp.services", []);



var res = "resources/";
/*=========================================   *
 *            Controllers                     *
 *=========================================== */
angular.module('gameApp.controllers').controller('gameController', function ($scope, MessageService) {

    /*=========================================   *
     *            Controller Variables            *
     *=========================================== */


    $scope.currentDrag; //card id of the currently dragged card, null otherwise.
    $scope.cardId = 0;
    $scope.zones = {
        "handZone": [],
        "faceDownZone": [],
        "faceUpZone": [],
        "stage1Zone": [],
        "stage2Zone": [],
        "stage3Zone": [],
        "stage4Zone": [],
        "stage5Zone": []
    };
    $scope.playerId = 0;
    $scope.addCard = function (n, id) {
        card = {
            name: n,
            id: ($scope.cardId++).toString(),
            draggable: true,
        };

        $scope.zones.handZone.push(card);
    };




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

    $scope.uuid = null; //the uuid for the gamelobby

    //Specify all endpoints 
    $scope.ep_joinGame = "/app/game.joinGame";
    $scope.ep_listGames = "/app/game.listGames";
    $scope.ep_createGame = "/app/game.createGame";

    /*  IMPORTANT - do not use this function directly. Make a wrapper function that calls this method when you wish to send a message to the server */
    /*  Parameters: endpoints - the endpoint to which we are sending a message to  */
    $scope.addMessage = function (endpoint) {
        console.log("sending message: " + $scope.message);
        MessageService.send($scope.message, endpoint);
        $scope.message = null;
    };

    $scope.sendCreateGameClient = function (np, rigType, pName) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            numPlayers: parseInt(np),
            rigged: $scope.RIGGED.NORMAL,
            playerName: pName,
            java_class: "GameCreateClient"
        }
        $scope.addMessage($scope.ep_createGame);
    };

    $scope.sendListGamesClient = function () {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            java_class: "GameListClient",

        };
        $scope.addMessage($scope.ep_listGames);
    };
    $scope.sendGameJoinClient = function (uuid, playerName) {
        $scope.message = {
            TYPE: $scope.TYPE_GAME,
            messageType: $scope.MESSAGETYPES.JOINGAME,
            player: 0,
            uuid: uuid,
            playerName: playerName,
            java_class: "GameJoinClient"
        };
        $scope.addMessage($scope.ep_joinGame);
    };

    MessageService.receive().then(null, null, function (message) {
        $scope.messages.push(message);
        console.log(message);
    });


    /*=========================================   *
     *             Dragging Functions             *
     *=========================================== */

    $scope.startDrag = function (event) {
        var cardId = event.currentTarget.id;
        console.log("Start drag on card id - " + cardId)
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

});


//Example templating:
//<div card-img="<url>"></div>
angular.module('gameApp.controllers').directive('cardImg', function () {
    console.log("img");
    return function (scope, element, attrs) {
        var url = attrs.cardImg;
        element.css({
            'background-image': 'url(' + url + ')',
            'background-size': '100px 150px',
            'background-repeat': 'no-repeat'
        });
    };
});


/*=========================================   *
 *           Services                         *
 *=========================================== */

angular.module("gameApp.services").service("MessageService", function ($q, $timeout) {

    var service = {},
        listener = $q.defer(),
        socket = {
            client: null,
            stomp: null
        },
        messageIds = [];

    service.RECONNECT_TIMEOUT = 30000;
    service.SOCKET_URL = "/ws";
    service.CHAT_TOPIC = "/topic/public";
    service.CHAT_BROKER = "/app/ws"; //gonna need multiple of these


    service.receive = function () {
        return listener.promise;
    };

    /*  Parameters: message, json object
                    endpoint, the endpoint we wish to send this to
     *
     */
    service.send = function (message, endpoint) {
        var id = Math.floor(Math.random() * 1000000);
        console.log(endpoint);
        socket.stomp.send(endpoint, {}, JSON.stringify(message));
        console.log("hello");
        messageIds.push(id);
    };

    var reconnect = function () {
        $timeout(function () {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function (data) {
        var message = JSON.parse(data),
            out = {};
        out.message = message.message;
        out.time = new Date(message.time);
        if (_.contains(messageIds, message.id)) {
            out.self = true;
            messageIds = _.remove(messageIds, message.id);
        }
        return out;
    };

    var startListener = function () {
        socket.stomp.subscribe(service.CHAT_TOPIC, function (data) {
            listener.notify(getMessage(data.body));
        });
    };

    var initialize = function () {
        socket.client = new SockJS(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});
