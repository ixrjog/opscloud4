/**
 * Created by liangjian on 2017/8/29.
 */
app.controller('vmServerCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {

    $scope.userType = staticModel.userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    $scope.authPoint = $state.current.data.authPoint;

    //server表关联
    $scope.vmStatus = staticModel.serverStatus;

    //导航条按钮控制
    $scope.butSearchDisabled = false;
    $scope.butRepeatDisabled = false;
    $scope.butRefreshDisabled = false;
    $scope.butRefreshSpinDisabled = false;
    $scope.butCheckDisabled = false;
    $scope.butCheckSpinDisabled = false;

    var butRefreshRunning = function (isRun) {
        $scope.butRefreshDisabled = isRun;
        $scope.butSearchDisabled = isRun;
        $scope.butCheckDisabled = isRun;
        $scope.butRefreshSpinDisabled = isRun;
    }

    var butCheckRunning = function (isRun) {
        $scope.butCheckDisabled = isRun;
        $scope.butSearchDisabled = isRun;
        $scope.butRefreshDisabled = isRun;
        $scope.butCheckSpinDisabled = isRun;
    }

    $scope.vmStatistics = function () {
        var url = "/server/vmStatistics";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vmData = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.vmStatistics();


    $scope.ecsRefresh = function () {
        butRefreshRunning(true);
        var url = "/server/vmRefresh";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                butRefreshRunning(false);
                toaster.pop("success", "列表已更新！");
            } else {
                butRefreshRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            butRefreshRunning(false);
            toaster.pop("error", err);
        });
    }

    $scope.ecsCheck = function () {
        butCheckRunning(true);
        var url = "/server/vmCheck";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                butCheckRunning(false);
                toaster.pop("success", "校验完毕！");
            } else {
                butCheckRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            butCheckRunning(false);
            toaster.pop("error", err);
        });
    }

    $scope.ecsDump = function () {
        var url = "/server/ecsDump";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", data.msg);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    /**
     * 标记ecs状态为删除
     * @param serverId
     */
    $scope.setDelEcs = function (item) {
        var url = "/server/setStatus?insideIp=" + item.insideIp;
        httpService.doGet(url).then(function (data) {
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

    /**
     * 标记ecs状态为删除
     * @param serverId
     */
    $scope.delVm = function (item) {
        var url = "/server/delVm?insideIp=" + item.insideIp;
        httpService.doGet(url).then(function (data) {
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

    /**
     * 关闭电源
     * @param id
     */
    $scope.vmPowerOff = function (item) {
        var url = "/server/vmPowerOff?id=" + item.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "关闭电源完成！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 开启电源
     * @param id
     */
    $scope.vmPowerOn = function (item) {
        var url = "/server/vmPowerOn?id=" + item.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "开启电源完成！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    $scope.queryName = "";
    $scope.queryIp = "";
    //关联
    $scope.nowStatus = -1;
    $scope.nowPublicGroup = [];
    $scope.nowInternalGroup = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        //关联
        $scope.nowAssociate = 1;
    }

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
        var url = "/server/vmPage?"
            + "&serverName=" + $scope.queryName
            + "&queryIp=" + $scope.queryIp
            + "&status=" + $scope.nowStatus
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

    ////////////////////////////////////////////////

    $scope.addVmServer = function (item) {

        var serverItem = {
            id: 0,
            serverGroupDO: "",
            serverName: item.vmName,
            serverType: 1,
            loginType: 0,
            loginUser: "manage",
            envType: 2,
            area: "hangzhou-office",
            publicIP: "",
            insideIP: "",
            serialNumber: "",
            ciGroup: "",
            content: ""
        }

        saveItem(serverItem, item);
    }

    var saveItem = function (serverItem, item) {

        serverItem.insideIP = {
            ipNetworkDO: $scope.nowInternalGroup.selected,
            ip: item.insideIp
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'serverInfo',
            controller: 'vmServerInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                userType: function () {
                    return $scope.userType;
                },
                envType: function () {
                    return $scope.envType;
                },
                logType: function () {
                    return $scope.logType;
                },
                serverType: function () {
                    return $scope.serverType;
                },
                serverItem: function () {
                    return serverItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

});

app.controller('vmServerInstanceCtrl', function ($scope, $uibModalInstance, httpService, userType, envType, logType, serverType, serverItem) {
    $scope.userType = userType;
    $scope.envType = envType;
    $scope.logType = logType;
    $scope.serverType = serverType;

    $scope.serverItem = serverItem;
    $scope.nowServerGroup = {};

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

    $scope.insideip = "";
    $scope.publicip = "";

    /**
     * 初始化环境
     */
    var initEnv = function () {

        if ($scope.serverItem.serverName != null && $scope.serverItem.serverName != '') {

            if ($scope.envType.length != 0) {
                var serverName = $scope.serverItem.serverName;
                var rightName = "";
                for (var i = 0; i < $scope.envType.length; i++) {

                    var item = $scope.envType[i];
                    var name = item.name;
                    rightName = serverName.replace(new RegExp("-" + name), "");
                    if (serverName != rightName) {
                        $scope.serverItem.serverName = rightName;
                        $scope.serverItem.envType = item.code;
                        break;
                    }
                }
                setServergroupSelected("group_" + $scope.serverItem.serverName);
            }


        }

        if ($scope.serverItem.serverGroupDO == null) {
            $scope.nowServerGroup = {};
        } else {
            $scope.nowServerGroup = {
                selected: $scope.serverItem.serverGroupDO
            };
        }

        if ($scope.serverItem.publicIP == null) {
            $scope.nowPublicGroup = {};
        } else {
            $scope.nowPublicGroup = {
                selected: $scope.serverItem.publicIP.ipNetworkDO
            };
        }

        if ($scope.serverItem.insideIP == null) {
            $scope.nowInternalGroup = {};
        } else {
            $scope.nowInternalGroup = {
                selected: $scope.serverItem.insideIP.ipNetworkDO
            };
        }

        $scope.insideip = $scope.serverItem.insideIP != null ? $scope.serverItem.insideIP.ip : "";
        $scope.publicip = $scope.serverItem.publicIP != null ? $scope.serverItem.publicIP.ip : "";
    }

    var setServergroupSelected = function (serverGroupName) {
        if (serverGroupName == null || serverGroupName == '') return;
        var url = "/servergroup/query/get?name=" + serverGroupName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.nowServerGroup.selected = data.body;
            }
        });
    }

    initEnv();

    /**
     * 重置
     */
    $scope.resetServerItem = function () {
        $scope.serverItem = {
            id: 0,
            serverGroupDO: null,
            serverName: "",
            serverType: -1,
            loginType: -1,
            loginUser: "",
            envType: -1,
            area: "",
            publicIP: null,
            insideIP: null,
            serialNumber: "",
            ciGroup: "",
            content: ""
        }

        initEnv();
    }

    /**
     * 保存server item信息
     */
    $scope.saveServerItem = function (insideIP, publicIP) {
        var url = "/server/save";

        $scope.serverItem.serverGroupDO = $scope.nowServerGroup.selected;
        if ($scope.nowPublicGroup.selected != null) {
            $scope.serverItem.publicIP = {
                ipNetworkDO: $scope.nowPublicGroup.selected,
                ip: publicIP
            };
        }
        if ($scope.nowInternalGroup.selected != null) {
            $scope.serverItem.insideIP = {
                ipNetworkDO: $scope.nowInternalGroup.selected,
                ip: insideIP
            };
        }

        httpService.doPostWithJSON(url, $scope.serverItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////////

    $scope.serverGroupList = [];

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

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

    //////////////////////////////////////////////////////////

    $scope.publicGroupList = [];

    /**
     * 查询公网网段
     * @param ipNetwork
     */
    $scope.queryPublicIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 0);
    }

    $scope.internalGroupList = [];

    /**
     * 查询内网网段
     * @param ipNetwork
     */
    $scope.queryInternalIPGroup = function (ipNetwork) {
        queryIPGroup(ipNetwork, 1);
    }

    var queryIPGroup = function (ipNetwork, ipType) {
        var url = "/ipgroup/query?" + "page=" + 0 + "&length=" + 10;

        var queryBody = {
            ipNetwork: ipNetwork,
            serverGroupId: 0,
            ipType: ipType
        }
        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                if (ipType == 0) {   //公网
                    $scope.publicGroupList = body.data;
                    $scope.nowPublicGroup = {};
                } else if (ipType == 1) {   //内网
                    $scope.internalGroupList = body.data;
                    $scope.nowInternalGroup = {
                        selected: $scope.internalGroupList[1]
                    };
                }
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.checkIPUse = function (groupId, ip) {
        if (ip == null || ip == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "IP未指定!";
            return;
        }

        var url = "/ip/use/check?"
            + "groupId=" + groupId
            + "&ip=" + ip
            + "&serverId=" + $scope.serverItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = ip + "未被其它服务器使用!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.changeServerGroup = function (serverGroup) {
        return;
    }
});

app.controller('vmTemplateCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {

    $scope.authPoint = $state.current.data.authPoint;

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
        var url = "/server/template/vm/page?"
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

});