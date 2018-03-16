var res = "resources/";
var app = angular.module('gameApp', ['ngDragDrop']);

app.controller('handController', function ($scope){
    $scope.cards = [];
    $scope.id = 0;
    
    $scope.print = function(){
        console.log("Print called");
    };
    $scope.addCard = function(n){
        card = {name:n, id: $scope.id++};
        $scope.cards.push(card);
    };
});

//Example templating:
//<div card-img="<url>"></div>
app.directive('cardImg', function(){
    
        console.log("img");
    return function(scope, element, attrs){
        var url = attrs.cardImg;
        element.css({
            'background-image': 'url(' + url +')',
            'background-size' : '100px 150px',
            'background-repeat': 'no-repeat'
        });
    };
});