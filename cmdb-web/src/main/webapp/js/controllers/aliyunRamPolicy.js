'use strict';

app.controller('aliyunRamPolicyCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryName = "";
    $scope.queryDescription = "";
    $scope.butUpdatePolicy = false;

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryDescription = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 20;


    $scope.queryPolicy = function () {
        var url = "/aliyun/ram/policy/page?policyName=" + ($scope.queryName == null ? "" : $scope.queryName)
            + "&description=" + ($scope.queryDescription == null ? "" : $scope.queryDescription)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.setAllows = function (id) {
        var url = "/aliyun/ram/policy/set?id=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "设置成功！");
                $scope.queryPolicy();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.updatePolicy = function () {
        $scope.butUpdatePolicy = true;
        var url = "/aliyun/ram/policy/update";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "同步成功！");
                $scope.queryPolicy();
                $scope.butUpdatePolicy = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butUpdatePolicy = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butUpdatePolicy = false;
        });
    }

    $scope.queryPolicy();

    $scope.pageChanged = function () {
        $scope.queryPolicy();
    }



});

