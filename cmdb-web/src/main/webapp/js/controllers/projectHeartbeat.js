'use strict';

app.controller('projectHeartbeatCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.projectType = staticModel.projectType;
    $scope.projectStatus = staticModel.projectStatus;
    $scope.queryProjectName = "";
    $scope.queryProjectType = -1;
    $scope.queryStatus = -1;
    $scope.queryLeaderUsername = "";
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    // 生成负责人信息
    $scope.refreshLeaderUserInfo = function () {

        if ($scope.pageData.length == 0) return;

        for (var i = 0; i < $scope.pageData.length; i++) {
            var info = '<b style="color: #286090">项目负责人</b>';
            var item = $scope.pageData[i].leaderUser;
            var mobile = "";
            if (item.mobile != null && item.mobile != '') {
                mobile = "<br/>" + item.mobile;
            }
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' + item.username +
                '<b style="color: #777"><' + item.displayName + "></b><br/>"
                + '<b style="color: #286090">' + item.mail + "</b>"
                + mobile
            item.userInfo = $sce.trustAsHtml(
                info
            );
        }
    }

    $scope.doQuery = function () {
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }
        // 构建参数 rojectType
        var projectTypeParam = -1;
        if ($scope.queryProjectType != null) {
            projectTypeParam = $scope.queryProjectType < 0 ? -1 : $scope.queryProjectType;
        }
        // 构建参数 status
        var statusParam = -1;
        if ($scope.queryStatus != null) {
            statusParam = $scope.queryStatus < 0 ? -1 : $scope.queryStatus;
        }

        var url = "/project/heartbeat/page?"
            + "projectName=" + ($scope.queryProjectName == null ? "" : $scope.queryProjectName) + "&"
            + "projectType=" + projectTypeParam + "&"
            + "status=" + statusParam + "&"
            + "leaderUsername=" + ($scope.queryLeaderUsername == null ? "" : $scope.queryLeaderUsername) + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshLeaderUserInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    ////////////////////////////////////////////////////////
    $scope.saveHeartbeat = function (item, status) {

        var url = "/project/heartbeat/save?"
            + "pmId=" + item.id + "&"
            + "status=" + status;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }
    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryProjectName = "";
        $scope.queryProjectType = -1;
        $scope.queryLeaderUsername = "";
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////////////////
});
