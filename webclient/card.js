var res = "resources/";
var app = angular.module('gameApp', ['ngDragDrop']);

app.controller('gameController', function ($scope) {
    $scope.game = {
        "hand": [],
        "field": []
    };
    $scope.hand = [];
    $scope.playerId = 0;
    $scope.addCard = function (n, id) {
        console.log($scope.game);
        card = {
            name: n,
            id: id,
            draggable: true,
        };
        $scope.game.hand.push(card);
    };

});


//Example templating:
//<div card-img="<url>"></div>
app.directive('cardImg', function () {
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
