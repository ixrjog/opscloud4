'use strict';

app.controller('jenkinsJobBuildsCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
    // $scope.authPoint = $state.current.data.authPoint;

    //$scope.hookType = staticModel.hookType;
    $scope.jobEnvType = staticModel.jenkinsJobEnvType;
    $scope.buildType = staticModel.jenkinsBuildType;
    $scope.queryBuildNumber = 0;
    $scope.queryJobName = "";
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////


    // 生成参数
    $scope.refreshParamsInfo = function () {

        if ($scope.pageData.length == 0) return;

        for (var i = 0; i < $scope.pageData.length; i++) {
            var info = '<b style="color: #286090">执行参数</b>';
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
            var item = $scope.pageData[i];
            //var params = $scope.pageData[i].paramList;
            if (item.paramList.length == 0) return;
            for (var j = 0; j < item.paramList.length; j++) {
                var param = item.paramList[j];
                info +=  '<b style="color: red">' + param.paramName + "</b>:" ;
                info +=  '<b style="color: green">' + param.paramValue + "</b> <br/>" ;
            }
            item.paramsInfo = $sce.trustAsHtml(
                info
            );
        }

    }

    /////////////////////////////////////////////////

    $scope.doQuery = function () {

        var url = "/jenkins/job/builds/page?"
            + "jobName=" + ($scope.queryJobName == null ? "" : $scope.queryJobName) + "&"
            + "buildNumber=" + ($scope.queryBuildNumber == null ? 0 : $scope.queryBuildNumber) + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshParamsInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();


    $scope.rebuildJob = function (id) {
        $scope.butBuildSpinDisabled = true;
        var url = "/jenkins/jobs/rebuild?"
            + "id=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!");
                $scope.doQuery();
                $scope.butBuildSpinDisabled = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butBuildSpinDisabled = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butBuildSpinDisabled = false;
        });
    }

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryBuildNumber = 0;
        $scope.queryJobName = "";
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////////////////
});

