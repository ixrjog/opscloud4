'use strict';

app.controller('serverCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.useType = [];

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    $scope.queryUseType = function () {
        var url = "/servergroup/useType/query";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.useType =  data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryUseType();


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

    $scope.queryName = "";
    $scope.queryIp = "";
    $scope.nowUseType = 0;
    $scope.nowEnv = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        $scope.nowUseType = 0;
        $scope.nowEnv = -1;
        $scope.nowServerGroup = {};
        $scope.serverGroupList = [];
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
        var url = "/server/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&useType=" + ($scope.nowUseType == null ? 0 : $scope.nowUseType)
            + "&envType=" + ($scope.nowEnv == null ? -1 : $scope.nowEnv)
            + "&queryIp=" + $scope.queryIp
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

    ///////////////////////////////////////////////////////////

    $scope.addServer = function () {
        var serverItem = {
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
        saveItem(serverItem);
    }

    $scope.editServer = function (item) {
        var serverItem = {
            id: item.id,
            serverGroupDO: item.serverGroupDO,
            serverName: item.serverName,
            serverType: item.serverType,
            loginType: item.loginType,
            loginUser: item.loginUser,
            envType: item.envType,
            area: item.area,
            publicIP: item.publicIP,
            insideIP: item.insideIP,
            serialNumber: item.serialNumber,
            ciGroup: item.ciGroup,
            content: item.content
        }

        saveItem(serverItem);
    }

    var saveItem = function (serverItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'serverInfo',
            controller: 'serverInstanceCtrl',
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

    /**
     * 删除server 信息
     * @param serverId
     */
    $scope.delServer = function (item) {
        var url = "/server/del?serverId=" + item.id;

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
});

app.controller('serverPropertyCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;


    $scope.changeServerGroupSelected = function(){
        $scope.queryServer();
    }

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;

                $scope.queryServer($scope.nowServer.selected == null ? "" : $scope.nowServer.selected.serverName);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.queryServer = function (queryServer) {
        var url = "/server/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + (queryServer == null ? "" : queryServer)
            + "&useType=" + 0
            + "&envType=" + -1
            + "&queryIp="
            + "&page=" + 0
            + "&length=" + 20;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.nowServer = {};
    $scope.serverList = [];

    $scope.reSet = function () {
        $scope.nowServerGroup = {};
        $scope.nowServer = {};
        $scope.queryServer("");
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
        var url = "/server/propertygroup/query?"
            + "groupId=" + ($scope.nowServerGroup.selected == null ? 0 : $scope.nowServerGroup.selected.id)
            + "&serverId=" + ($scope.nowServer.selected == null ? 0 : $scope.nowServer.selected.id)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    //////////////////////////////////////////////////////////////////////

    $scope.addItem = function () {
        var item = {
            serverDO: $scope.nowServer.selected,
            serverGroupDO: $scope.nowServerGroup.selected,
            serverPropertyGroup: null,
            type: "new",
            groupProperties: []
        }

        saveItem(item);
    }

    $scope.editItem = function (serverDO, serverGroupDO, serverPropertyGroup, groupProperties) {
        var item = {
            serverDO: serverDO,
            serverGroupDO: serverGroupDO,
            serverPropertyGroup: serverPropertyGroup,
            type: "edit",
            groupProperties: groupProperties
        }

        saveItem(item);
    }

    $scope.delItem = function (serverId, propertyGroupId) {
        var url = "/config/propertygroup/del?"
            + "serverId=" + serverId
            + "&groupId=0"
            + "&propertyGroupId=" + propertyGroupId;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var saveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'serverpsInfo',
            controller: 'serverpsInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                serverItem: function () {
                    return item;
                },
                staticModel: function () {
                    return staticModel;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    //////////////////////////////////////////////////////////////////

    $scope.nowShowType = 0; //0:收起;1:展开
});

/**
 * 新增服务器属性
 */
app.controller('serverpsInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, serverItem, staticModel) {
    $scope.groupItem = serverItem;
    if ($scope.groupItem.serverDO != null) {
        for (var i = 0; i < staticModel.envType.length; i++) {
            if ($scope.groupItem.serverDO.envType == staticModel.envType[i].code) {
                $scope.groupItem.serverDO.envTypeStr = staticModel.envType[i].name;
                break;
            }
        }
    }

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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

    ////////////////////////////////////////////////////////////


    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
                //$scope.queryServer("");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    if ($scope.groupItem.serverGroupDO != null) {
        $scope.nowServerGroup = {
            selected: $scope.groupItem.serverGroupDO
        }
    } else {
        $scope.nowServerGroup = {};
    }

    $scope.serverGroupList = [];

    $scope.queryServer = function (queryServer) {
        var url = "/server/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + (queryServer == null ? "" : queryServer)
            + "&useType=" + 0
            + "&envType=" + -1
            + "&queryIp="
            + "&page=" + 0
            + "&length=" + 20;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.nowServer = {};
    $scope.serverList = [];

    if ($scope.groupItem.serverDO != null) {
        $scope.nowServer = {
            selected: $scope.groupItem.serverDO
        }
    } else {
        $scope.nowServer = {};
    }

    ////////////////////////////////////////////////////////////

    if ($scope.groupItem.serverPropertyGroup != null) {
        $scope.propertyGroup = {
            selected: $scope.groupItem.serverPropertyGroup
        }
    } else {
        $scope.propertyGroup = {};
    }
    $scope.propertyGroupList = [];

    $scope.queryGroup = function (groupName) {
        var url = "/config/property/group?"
            + "groupName=" + groupName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.propertyGroupList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    ////////////////////////////////////////////////////////////

    $scope.groupProperties = $scope.groupItem.groupProperties;
    $scope.changePropertyGroup = function () {
        var url = "/config/property?"
            + "proName="
            + "&groupId=" + $scope.propertyGroup.selected.id
            + "&page=" + 0
            + "&length=" + 10000;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.groupProperties = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    ////////////////////////////////////////////////////////////

    $scope.saveData = function () {
        var url = "/config/propertygroup/save";

        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器组";
            return;
        }

        if ($scope.nowServer.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器";
            return;
        }

        if ($scope.propertyGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定属性组";
            return;
        }

        var requestBody = {
            serverDO: $scope.nowServer.selected,
            propertyGroupDO: $scope.propertyGroup.selected,
            propertyDOList: $scope.groupProperties
        }

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.nowServerGroup = {};
                $scope.nowServer = {};
                $scope.propertyGroup = {};
                $scope.groupProperties = [];
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

});

app.controller('serverInstanceCtrl', function ($scope, $uibModalInstance, httpService, userType, envType, logType, serverType, serverItem) {
    $scope.userType = userType;
    $scope.envType = envType;
    $scope.logType = logType;
    $scope.serverType = serverType;

    $scope.serverItem = serverItem;

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
                } else if (ipType == 1) {   //内网
                    $scope.internalGroupList = body.data;
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
        if (serverGroup == null)
            return;
        var groupName = serverGroup.name;
        var serverName = groupName.replace(/^group_/, "");
        $scope.serverItem.serverName = serverName;
    }
});
