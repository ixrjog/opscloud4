'use strict';

app.controller('projectManagementCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
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

    // 生成发起人信息
    $scope.refreshLeaderUserInfo = function () {

        if ($scope.pageData.length == 0) return;

        for (var i = 0; i < $scope.pageData.length; i++) {
            var info = '<b style="color: #286090">项目负责人</b>';
            var item = $scope.pageData[i].leaderUser;
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
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

        var url = "/project/page?"
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

    $scope.reSet = function () {
        $scope.queryProjectName = "";
        $scope.queryProjectType = -1;
        $scope.queryLeaderUsername = "";
        $scope.doQuery();
    }

    $scope.editItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'pmInstance',
            controller: 'pmInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                pmItem: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.delItem = function (item) {
        var url = "/project/del?pmId=" + item.id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    $scope.addPm = function () {
        var modalInstance = $uibModal.open({
            templateUrl: 'pmInstance',
            controller: 'pmInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                pmItem: function () {
                    return null;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
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
        if (pmItem == null){
            $scope.pm =  {id: 0};
            return ;
        }else{
            $scope.pm =pmItem;
        }

        if ($scope.pm.leaderUser != null) {
            $scope.nowLeaderUser.selected = $scope.pm.leaderUser;
        }

        if ($scope.pm.beginTime != null && $scope.pm.beginTime != "" ) {
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
        }else{
            $scope.pm.leaderUserId = $scope.nowLeaderUser.selected.id;
            $scope.pm.leaderUsername =  $scope.nowLeaderUser.selected.username ;
            $scope.pm.leaderUser = $scope.nowLeaderUser.selected;
        }

        if ($scope.pm.dt != null && $scope.pm.dt != {}) {
            $scope.pm.beginTime = new Date($scope.pm.dt).Format("yyyy-MM-dd hh:mm:ss.S");
        }else{
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
    $scope.addUser = function() {
        if($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/project/user/add?"
            + "pmId=" + $scope.pm.id +"&"
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

    $scope.delUser = function(id) {
        var url = "/project/user/del?"
            + "id=" + id;

        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "移除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
    //////////////////////////////////////////////////////

    $scope.addServerGroup = function() {
        if($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/project/serverGroup/add?"
            + "pmId=" + $scope.pm.id +"&"
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

    $scope.delServerGroup = function(id) {
        var url = "/project/serverGroup/del?"
            + "id=" + id;

        httpService.doDelete(url).then(function(data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "移除成功!";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }
    //////////////////////////////////////////////////////
});