angular.module("gameApp", [
    "gameApp.controllers",
    "gameApp.services"
]);

angular.module("gameApp.controllers", ['ngDragDrop']);
angular.module("gameApp.services", []);






//Example templating:
//<div card-img="<url>"></div>
angular.module('gameApp.controllers').directive('cardImg', function () {
    return function (scope, element, attrs) {
        var url = attrs.cardImg;
        element.css({
            'background-image': 'url(' + url + ')',
            'background-size': '100px 150px',
            'background-repeat': 'no-repeat'
        });
    };
});

angular.module('ui.bootstrap.demo', ['ngAnimate', 'ui.bootstrap']);