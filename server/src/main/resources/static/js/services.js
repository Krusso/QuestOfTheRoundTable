/*=========================================   *
 *           Services                         *
 *=========================================== */

angular.module("gameApp.services").service("MessageService", function ($rootScope, $q, $timeout) {

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
    service.CHAT_BROKER = "/app/ws";


    service.receive = function () {
        console.log("received something");
        return listener.promise;
    };

    /*  Parameters: message, json object
                    endpoint, the endpoint we wish to send this to
     *
     */
    service.send = function (message, endpoint) {
        socket.stomp.send(endpoint, {}, angular.toJson(message));
    };

    var reconnect = function () {
        $timeout(function () {
            initialize();
        }, this.RECONNECT_TIMEOUT);
    };

    var getMessage = function (data) {
        var message = JSON.parse(data)
        return message;
    };

	service.subscribe = function(scope, callback) {
            var handler = $rootScope.$on('got-message', function(event, data) {
            	callback(data);
            });
            scope.$on('$destroy', funct);
    };


	var observerCallbacks = [];
	var missed = [];

  	//register an observer
  	service.registerObserverCallback = function(that, callback){
	    observerCallbacks.push({that:that, callback: callback});
	    while(missed.length != 0){
	    	notifyObservers(missed.shift());
	    }
  	};

  	//call this when you know 'foo' has been changed
  	var notifyObservers = function(data){
	    angular.forEach(observerCallbacks, function(item, index){
      		item.callback(data);
    	});
  	};
  	
  	service.unregister = function(that){
  		var index1 = -1;
  		angular.forEach(observerCallbacks, function(item, index) {
  			if(item.that = that){
  				index1 = index;
  			}
		});
		if(index1 != -1){
			observerCallbacks.splice(index1,1);
		}
  	}

    var notify = function(data) {
    	if(observerCallbacks.length == 0){
    		missed.push(data);
    	} else {
           notifyObservers(data);
        }
    };

    var startListener = function () {
    	socket.stomp.subscribe(service.CHAT_TOPIC, notify);
    };

    var initialize = function () {
        socket.client = new SockJS(service.SOCKET_URL);
        console.log(service.SOCKET_URL);
        socket.stomp = Stomp.over(socket.client);
        socket.stomp.connect({}, startListener);
        socket.stomp.onclose = reconnect;
    };

    initialize();
    return service;
});
