// tab controller
app.controller('CustomTabController', ['$scope', function($scope) {
  $scope.tabs = [true, false, false];
  $scope.tab = function(index){
    angular.forEach($scope.tabs, function(i, v) {
      $scope.tabs[v] = false;
    });
    $scope.tabs[index] = true;
  }
}]);