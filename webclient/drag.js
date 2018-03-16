var app = angular.module('app', []);

app.controller('controller1', function ($scope) {

    $scope.first = 1;
    $scope.second = 1;

    $scope.updateValue = function () {
        $scope.calculation = $scope.first + '+' + $scope.second + '=' + (+$scope.first + +$scope.second)
    };

});

var app2 = angular.module('app2', []);
app2.controller('controller1', function ($scope) {
    $scope.randomNum1 = Math.floor((Math.random() * 10) * 1);
    $scope.randomNum2 = Math.floor((Math.random() * 10) * 1);
});

app2.controller('badController', function ($scope) {
    var badFeelings = ["Disregarded", "Shit", "fucked up", "Rejected"];
    $scope.bad = badFeelings[Math.floor((Math.random() * 4))];

});
app2.controller('goodController', function ($scope) {
    var goodFeelings = ["Pleasure", "Awesome", "Lovable", "Peace"];
    $scope.good = goodFeelings[Math.floor((Math.random() * 4))];

});
