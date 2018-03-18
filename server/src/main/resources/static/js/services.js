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
    service.CHAT_TOPIC = "/user/queue/response";
    service.CHAT_BROKER = "/app/ws"; //gonna need multiple of these


    service.receive = function () {
        console.log("received something");
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
