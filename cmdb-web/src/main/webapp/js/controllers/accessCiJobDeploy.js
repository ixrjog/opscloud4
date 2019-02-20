'use strict';

app.controller('accessCiJobDeployCtrl', function ($scope, $location, $uibModal, $state, toaster, httpService,staticModel) {
    $scope.rollbackType = staticModel.rollbackType;

    $scope.ciType = staticModel.ciType;
    $scope.appType = staticModel.appType;
    $scope.jobEnvType = staticModel.envType;

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 5;
    $scope.jobId = 0;
    $scope.appId = 0;

    $scope.jobItem = {};
    $scope.appItem = {};

    var doQuery = function () {
        var url = "/ci/deploy/page?jobId=" + $scope.jobId
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //  $scope.refreshParamsInfo();
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    var getJob = function () {
        var url = "/ci/job/get?id=" + $scope.jobId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.jobItem = data.body;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    var getApp = function () {
        var url = "/ci/app/get?appId=" + $scope.appId;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.appItem = data.body;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    var init = function () {
        // TODO jobId
        $scope.jobId = $location.search().jobId;
        $scope.appId = $location.search().appId;
        doQuery();
        getJob();
        getApp();
    }

    init();


    $scope.pageChanged = function () {
        doQuery();
    }


    $scope.goBack = function () {
        $state.go('app.dashboard');
    }


});