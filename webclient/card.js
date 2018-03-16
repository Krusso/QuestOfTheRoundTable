var res = "resources/";
var app = angular.module('gameApp', ['ngDragDrop']);

app.controller('gameController', function ($scope) {
    $scope.calls = 0;
    $scope.currentDrag;
    $scope.cardId=0;
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
            id: $scope.cardId++,
            draggable: true,
        };
        
        $scope.game.hand.push(card);
    };
    
    $scope.startDrag = function(event, card){
        console.log(event);
        console.log("asdf")
        console.log(card);
//        console.log(event.currentTarget);
        $scope.calls = $scope.calls + 1;
        $scope.currentDrag = card;
    };  
    
    $scope.onDrop = function(event){
        
        console.log(event);
    }
    
    $scope.findCardById = function(id){
        
    }

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
