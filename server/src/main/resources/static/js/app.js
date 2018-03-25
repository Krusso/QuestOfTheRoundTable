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
