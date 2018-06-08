'use strict';

app.controller('webHooksCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
    // $scope.authPoint = $state.current.data.authPoint;
    $scope.hookType = staticModel.hookType;
    $scope.triggerType = staticModel.jenkinsTriggerType;
    // $scope.projectType = staticModel.projectType;
    // $scope.projectStatus = staticModel.projectStatus;
    $scope.nowHookType = -1;
    $scope.nowTriggerBuild = -1;
    $scope.queryProjectName = "";
    $scope.queryRepositoryName = "";
    //$scope.queryWebHooksType = -1;
    $scope.queryTriggerBuild = -1;
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////


    $scope.doQuery = function () {

        var url = "/jenkins/webHooks/page?"
            + "projectName=" + ($scope.queryProjectName == null ? "" : $scope.queryProjectName) + "&"
            + "repositoryName=" + ($scope.queryRepositoryName == null ? "" : $scope.queryRepositoryName) + "&"
            + "webHooksType=" + ($scope.nowHookType == null ? -1 : $scope.nowHookType) + "&"
            + "triggerBuild=" + ($scope.nowTriggerBuild == null ? -1 : $scope.nowTriggerBuild) + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
              //  $scope.refreshLeaderUserInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryProjectName = "";
        $scope.queryRepositoryName = "";
        $scope.nowHookType = -1;
        $scope.nowTriggerBuild = -1;
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////////////////
});

app.controller('pmInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, pmItem) {
    $scope.pm = {};

    $scope.nowUser = {};
    $scope.nowServerGroup = {};

    $scope.userList = [];
    $scope.nowLeaderUser = {};

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];

    $scope.doQuery = function () {
        var url = "/project/get?"
            + "id=" + $scope.pm.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.pm = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var init = function () {
        if (pmItem == null) {
            $scope.pm = {id: 0};
            return;
        } else {
            $scope.pm = pmItem;
        }

        if ($scope.pm.leaderUser != null) {
            $scope.nowLeaderUser.selected = $scope.pm.leaderUser;
        }

        if ($scope.pm.beginTime != null && $scope.pm.beginTime != "") {
            $scope.pm.dt = new Date($scope.pm.beginTime);
        }

    }

    init();

    $scope.dateOptions = {
        //dateDisabled: disabled,
        formatYear: 'yyyy',
        maxDate: new Date(),
        minDate: new Date(2016, 1, 1),
        startingDay: 1
    };


    $scope.alert = {
        type: "",
        msg: ""
    };

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }

    $scope.popup = {
        opened: false
    };

    $scope.open = function () {
        $scope.popup.opened = true;
    };

    $scope.queryUsers = function (queryParam) {
        var url = "/users?page=0&length=10&username=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/project/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveItem = function () {
        var url = "/project/save";

        if ($scope.nowLeaderUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定负责人";
            return;
        } else {
            $scope.pm.leaderUserId = $scope.nowLeaderUser.selected.id;
            $scope.pm.leaderUsername = $scope.nowLeaderUser.selected.username;
            $scope.pm.leaderUser = $scope.nowLeaderUser.selected;
        }

        if ($scope.pm.dt != null && $scope.pm.dt != {}) {
            $scope.pm.beginTime = new Date($scope.pm.dt).Format("yyyy-MM-dd hh:mm:ss.S");
        } else {
            $scope.pm.beginTime = null;
        }

        httpService.doPostWithJSON(url, $scope.pm).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.pm = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////
    $scope.addUser = function () {
        if ($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/project/user/add?"
            + "pmId=" + $scope.pm.id + "&"
            + "userId=" + $scope.nowUser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                // var body = data.body;
                $scope.doQuery();
                //$scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });


    }

    $scope.delUser = function (id) {
        var url = "/project/user/del?"
            + "id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "移除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
    //////////////////////////////////////////////////////

    $scope.addServerGroup = function () {
        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/project/serverGroup/add?"
            + "pmId=" + $scope.pm.id + "&"
            + "serverGroupId=" + $scope.nowServerGroup.selected.id;

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

    $scope.delServerGroup = function (id) {
        var url = "/project/serverGroup/del?"
            + "id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "移除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
    //////////////////////////////////////////////////////
});