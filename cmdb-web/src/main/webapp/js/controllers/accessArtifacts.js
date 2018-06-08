'use strict';

app.controller('accessArtifactsCtrl', function($scope, $location, $uibModal, $state, toaster, httpService) {

    $scope.artifactsItem = null;
    var init = function() {
        var id = $location.search().id;
        var url = "/jenkins/job/builds?id=" + id;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                $scope.artifactsItem = data.body;
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