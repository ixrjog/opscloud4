'use strict';

app.controller('logServiceGroupManageCtrl', function ($scope, $state, $uibModal, $timeout, staticModel, httpService, toaster) {

    $scope.queryProjectName = "";
    $scope.queryLogstoreName = "";

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
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }

        var url = "/logService/group/page?"
            + "project=" + $scope.queryProjectName
            + "&logstore=" + $scope.queryLogstoreName
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

    $scope.doQuery();

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryProjectName = "";
        $scope.queryLogstoreName = "";
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////


    /**
     * 新增
     * @param item
     */
    $scope.addLSGroup = function () {
        var logServiceGroupCfgItem = {
            id: 0,
            project: "",
            logstore: "",
            content: ""
        }
        saveLSGroup(logServiceGroupCfgItem);
    }

    /**
     * 编辑
     * @param item
     */
    $scope.editLSGroup = function (item) {
        saveLSGroup(item);
    }

    var saveLSGroup = function (logServiceGroupCfgItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'groupInfo',
            controller: 'groupInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                logServiceGroupCfgItem: function () {
                    return logServiceGroupCfgItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.pushGroupCfg = function (id) {
        var url = "/logService/group/pushCfg?id=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "推送配置成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////////////
});

app.controller('logServiceManageCtrl', function ($scope, $state, $uibModal, $timeout, staticModel, httpService, toaster) {

    $scope.userType = [];

    $scope.nowType = 0;

    $scope.queryName = "";

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
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }

        var url = "/logService/servergroup/query?"
            + "name=" + $scope.queryName + "&"
            + "isUsername=false" + "&"
            + "useType=" + $scope.nowType + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

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

    $scope.doQuery();


    $scope.queryUseType = function () {
        var url = "/servergroup/useType/query";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.useType = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryUseType();

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowType = 0;
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////


    /**
     * 保存
     * @param item
     */
    $scope.editMachineGroup = function (logServiceServerGroupCfgItem) {

        var modalInstance = $uibModal.open({
            templateUrl: 'machineGroupInfo',
            controller: 'machineGroupInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                logServiceServerGroupCfgItem: function () {
                    return logServiceServerGroupCfgItem;
                }
            }
        });
    }


    ///////////////////////////////////////////////////////////////////
});

app.controller('groupInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, toaster, logServiceGroupCfgItem) {
    $scope.logServiceGroupCfgItem = logServiceGroupCfgItem;
    $scope.serverGroupTable = logServiceGroupCfgItem.memberList;
    $scope.projectList = [];
    $scope.nowProject = {};
    $scope.logstoreList = [];
    $scope.nowLogstore = {};
    $scope.configList = [];
    $scope.nowConfig = {};


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

    var init = function () {
        var item = $scope.logServiceGroupCfgItem;
        if (item.project != null && item.project != '') {
            $scope.nowProject.selected = item.project;
        }
        $scope.queryProject();
        $scope.queryLogstore();

        if (item.logstore != null && item.logstore != '') {
            $scope.nowLogstore.selected = item.logstore;
        }

        $scope.queryConfig();
        if (item.config != null && item.config != '') {
            $scope.nowConfig.selected = item.config;
        }

    }


    $scope.queryProject = function () {
        var url = "/logService/project/query";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.projectList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryLogstore = function () {
        var url = "/logService/logstore/query?"
            + "project=" + ($scope.nowProject.selected != null ? $scope.nowProject.selected : $scope.logServiceGroupCfgItem.project );

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.logstoreList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryConfig = function () {
        var url = "/logService//config/query?"
            + "project=" + ($scope.nowProject.selected != null ? $scope.nowProject.selected : $scope.logServiceGroupCfgItem.project )
            + "&logstore=" + ($scope.nowLogstore.selected != null ? $scope.nowLogstore.selected : $scope.logServiceGroupCfgItem.logstore );

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.configList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }





    init();

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];


    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/logServiceMemberPage?name=" + queryParam + "&groupCfgId=" + $scope.logServiceGroupCfgItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.serverGroupList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveLogServiceGroup = function () {
        var url = "/logService/group/save";

        if ($scope.nowProject.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Project";
            return;
        }

        if ($scope.nowLogstore.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Logstore";
            return;
        }

        if ($scope.nowConfig.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Config";
            return;
        }

        var requestBody = {
            id: $scope.logServiceGroupCfgItem.id,
            project: $scope.nowProject.selected,
            logstore: $scope.nowLogstore.selected,
            config: $scope.nowConfig.selected,
            content: ""
        }

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.getLogServiceGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


    $scope.getLogServiceGroup = function () {
        var url = "/logService/group/get?id=" + $scope.logServiceGroupCfgItem.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.logServiceGroupCfgItem = data.body;
                $scope.serverGroupTable = data.body.memberList;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.addServerGroup = function () {
        addMember();
    }

    $scope.delServerGroup = function (id) {
        delMember(id);
    }

    var addMember = function () {
        var url = "/logService/group/addMember?"
            + "groupCfgId=" + $scope.logServiceGroupCfgItem.id
            + "&serverGroupId=" + $scope.nowServerGroup.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.getLogServiceGroup();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var delMember = function (id) {
        var url = "/logService/group/delMember?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.getLogServiceGroup();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

});

app.controller('machineGroupInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, toaster, logServiceServerGroupCfgItem) {
    $scope.logServiceServerGroupCfgItem = logServiceServerGroupCfgItem;
    $scope.projectList = [];
    $scope.nowProject = {};
    $scope.logstoreList = [];
    $scope.nowLogstore = {};
    $scope.machineGroup = {};

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

    var init = function () {
        var item = $scope.logServiceServerGroupCfgItem;
        if (item.project != null && item.project != '') {
            $scope.nowProject.selected = item.project;
        }

        if (item.logstore != null && item.logstore != '') {
            $scope.nowLogstore.selected = item.logstore;
        }
        $scope.queryProject();
        $scope.queryLogstore();
        $scope.queryMachineGroup();
    }

    $scope.queryMachineGroup = function () {
        var item = $scope.logServiceServerGroupCfgItem;
        if (item.project == null || item.project == '') return;

        var url = "/logService/machineGroup/query?" +
            "project=" + item.project +
            "&groupName=" + item.serverGroupDO.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.machineGroup = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryProject = function () {
        var url = "/logService/project/query";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.projectList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }

    $scope.queryLogstore = function () {

        var url = "/logService/logstore/query?"
            + "project=" + ($scope.nowProject.selected != null ? $scope.nowProject.selected : $scope.logServiceServerGroupCfgItem.project );

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.logstoreList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        });
    }


    $scope.saveCfg = function () {

        if ($scope.nowProject.selected == null) {
            toaster.pop("warning", "未选择project");
            return;
        }

        if ($scope.nowLogstore.selected == null) {
            toaster.pop("warning", "未选择logstore");
            return;
        }

        var cfgItem = $scope.logServiceServerGroupCfgItem;
        cfgItem.project = $scope.nowProject.selected;
        cfgItem.logstore = $scope.nowLogstore.selected;

        var url = "/logService/serverGroupCfg/save";

        httpService.doPostWithJSON(url, cfgItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "同步完成!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        });
    }


    init();

    // $scope.saveEdit = function () {
    //     $uibModalInstance.close($scope.showItem);
    // };
});

