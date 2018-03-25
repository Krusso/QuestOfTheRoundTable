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
        var url = attrs.cardImg;
        if (url === null || url === "") {
            return;
        }
        var res = url.replace(/ /g, "%20");
        console.log(res);
        element.css({
            'background-image': 'url(../gameResources/' + res + '.png)',
            'background-size': 'contain',
            'background-repeat': 'no-repeat',
        });
    };
});
