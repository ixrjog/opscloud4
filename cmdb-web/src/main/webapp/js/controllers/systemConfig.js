'use strict';

app.controller('systemConfigCtrl', function($scope, $state, $uibModal, toaster, httpService) {
    $scope.systemConfig = {};

    var init = function() {
        var url = "/config/system/query";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                $scope.systemConfig = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});