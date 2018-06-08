'use strict';

app.controller('accessTodoCtrl', function($scope, $location, $uibModal, $state, toaster, httpService) {

    $scope.todoItem = null;
    var init = function() {
        var id = $location.search().id;
        var url = "/todo/query?dailyId=" + id;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                $scope.todoItem = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }

    init();

    $scope.goBack = function() {
        $state.go('app.dashboard');
    }


});