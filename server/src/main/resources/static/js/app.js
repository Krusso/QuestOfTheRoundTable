var app = angular.module("gameApp", [
    "gameApp.controllers",
    "gameApp.services",
    "ngRoute"
]);

app.config(function ($routeProvider) {
    $routeProvider
        .when("/", {
            templateUrl: "login.html"
        })
        .when("/lobby", {
            templateUrl: "lobby.html"
        })
        .when("/inlobby", {
            templateUrl: "inlobby.html"
        })
        .when("/gameboard", {
            templateUrl: "gameboard.html"
        });
});

angular.module("gameApp.controllers", ['ngDragDrop']);
angular.module("gameApp.services", []);


//Example templating:
//<div card-img="<url>"></div>
<<<<<<< HEAD
angular.module('gameApp.controllers').directive('c1', function () {
return {
  compile: function(tElement, tAttrs) {
      return function link(scope, element, attrs) {
        attrs.$observe('card1', function(value) { 
				var url = value;
        		if (url === null || url === "") {
		            return;
    	    	}
    	    	// encoding spaces for http connection
	        	var res = encodeURI(url);
	        	// encoding single quote so that css lets me do it
        		res = res.replace(/'/g, "%27");
        		console.log("res: " + res);
        		element.css({
		        	'background-image': "url(../gameResources/" + res + ".png)",
            		'background-size': 'contain',
            		'background-repeat': 'no-repeat',
        		});
             });
		
		};
    }
}});
=======
angular.module('gameApp.controllers').directive('cardImg', function () {
    return function (scope, element, attrs) {
        console.log("entered directive");
        console.log(attrs.cardImg);
        var url = attrs.cardImg;
        if (url == null || url == "") {
            return;
        }
        var res = encodeURI(url);
        console.log(res);
        element.css({
            'background-image': 'url(../gameResources/' + res + '.png)',
            'background-size': 'contain',
            'background-repeat': 'no-repeat',
        });

        scope.$watch('cardImg', function (newValue, oldValue) {
            if (newValue !== oldValue) {
                // You actions here
                //console.log("I got the new value! ", newValue);
            }
        }, true);
    };
});
>>>>>>> branch 'mkuang/stomp' of https://github.com/Krusso/QuestOfTheRoundTable
