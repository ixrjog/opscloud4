/**
 * Created by liangjian on 2017/1/16.
 */

'use strict';

app.controller('zabbixServerCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;
    $scope.userType = staticModel.userType;
    $scope.envType = staticModel.envType;
    //登录类型
    $scope.logType = staticModel.logType;
    //服务器类型
    $scope.serverType = staticModel.serverType;
    //监控状态
    $scope.zabbixStatus = staticModel.zabbixStatus;
    //主机监控
    $scope.zabbixMonitor = staticModel.zabbixMonitor;

    $scope.refresh = function () {
        var url = "/zabbixserver/refresh";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "数据更新任务后台执行！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

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
    $scope.nowType = 0;
    $scope.nowStatus = -1;
    $scope.nowMonitor = -1;
    $scope.nowEnv = -1;
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];
    $scope.queryTomcatVersion = "";

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryIp = "";
        $scope.nowType = 0;
        $scope.nowStatus = -1;
        $scope.nowMonitor = -1;
        $scope.nowEnv = -1;
        $scope.nowServerGroup = {};
        $scope.serverGroupList = [];
        $scope.queryTomcatVersion = "";
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
        var url = "/zabbixserver/page?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&serverName=" + $scope.queryName
            + "&useType=" + $scope.nowType
            + "&envType=" + $scope.nowEnv
            + "&queryIp=" + $scope.queryIp
            + "&zabbixStatus=" + ($scope.nowStatus == null ? -1 : $scope.nowStatus )
            + "&zabbixMonitor=" + ($scope.nowMonitor == null ? -1 : $scope.nowMonitor  )
            + "&tomcatVersion=" + ($scope.queryTomcatVersion == null ? "" : $scope.queryTomcatVersion)
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


    $scope.addHost = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'addHostInfo',
            controller: 'addHostInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                serverItem: function () {
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


    /**
     * 添加监控(废弃)
     * @param item
     */
    $scope.addMonitor = function (item) {
        var url = "/zabbixserver/addMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "添加主机监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 删除监控
     * @param item
     */
    $scope.delMonitor = function (item) {
        var url = "/zabbixserver/delMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除主机监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


    /**
     * 修复主机监控数
     * @param item
     */
    $scope.repair = function (item) {
        var url = "/zabbixserver/repair?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "修复主机监控数据成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


    /**
     * 启用监控
     * @param item
     */
    $scope.enableMonitor = function (item) {
        var url = "/zabbixserver/enableMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "启用监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 禁用监控
     * @param item
     */
    $scope.disableMonitor = function (item) {
        var url = "/zabbixserver/disableMonitor?"
            + "serverId=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "禁用监控成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

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

app.controller('zabbixUserCtrl', function ($scope, $state, $uibModal, toaster, httpService) {
    $scope.username = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        init();
    };

    /////////////////////////////////////////////////

    var init = function () {
        var url = "/users?"
            + "username=" + $scope.username
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

    init();

    $scope.doQuery = function () {
        init();
    }

    /////////////////////////////////////////////////////////////

    $scope.syncZabbixUser = function () {
        var url = "/zabbixserver/user/sync";

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "同步账户成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.createAllUserGroupConfigFile = function () {
        var url = "/zabbixserver/user/check";

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "校验账户成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////////////////

    $scope.addZabbixAuth = function (username) {
        var url = "/zabbixserver/user/auth/add?username=" + username;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delZabbixAuth = function (username) {
        var url = "/zabbixserver/user/auth/del?username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////

    $scope.serverList = function (username) {
        var modalInstance = $uibModal.open({
            templateUrl: 'keyBoxServerModal',
            controller: 'keyBoxServerInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                username: function () {
                    return username;
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

app.controller('zabbixTemplateCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {

    $scope.defSelect = staticModel.defSelect;

    $scope.authPoint = $state.current.data.authPoint;

    $scope.rsyncTemplate = function () {
        var url = "/zabbixserver/template/rsync";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "同步成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowEnabled = -1;
    }

    /////////////////////////////////////////////////

    $scope.queryName = "";
    $scope.nowEnabled = -1;
    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/zabbixserver/template/page?"
            + "templateName=" + $scope.queryName
            + "&enabled=" + ($scope.nowEnabled == null ? -1 : $scope.nowEnabled)
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

    /**
     * 设置模版
     * @param item
     */
    $scope.setTemplate = function (item) {
        var url = "/zabbixserver/template/set?id=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "设置成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


    ///////////////////////////////////////////////////////////

    /**
     * 删除模版
     * @param tiem
     */
    $scope.delTemplate = function (item) {
        var url = "/zabbixserver/template/del?id=" + item.id;

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

app.controller('zabbixConfigCtrl', function ($scope, $state, $uibModal, $sce, httpService, toaster) {

        $scope.configMap = {};

        $scope.itemGroup = "ZABBIX";

        /**
         * 获取配置
         */
        $scope.getConfig = function () {
            var url = "/config/center/get?itemGroup=" + $scope.itemGroup +
                "&env=";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.configMap = data.body;
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }


        /**
         * 更新配置
         */
        $scope.updateConfig = function () {
            var url = "/config/center/update";
            httpService.doPostWithJSON(url, $scope.configMap).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "配置保存成功!");
                    $scope.getConfig();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.getConfig();

        $scope.zabbixVersion = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'zabbixVersionInfo',
                controller: 'zabbixVersionInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    }
                }
            })
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

    }
);

app.controller('zabbixVersionInstanceCtrl', function ($scope, $uibModalInstance, $state, httpService) {
        // Zabbix server
        $scope.zabbix = {
            name: "Zabbix server"
        };

        $scope.version = "";

        $scope.getZabbixVersion = function () {
            var url = "/zabbixserver/version?zabbixServerName=" + $scope.zabbix.name;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var v = data.body;
                    if (v == null || v.version == null) {
                        $scope.version = "Zabbix API URL配置错误！";
                    } else {
                        $scope.version = "version : " + v.version + "[Zabbix API URL 配置正确]\n"

                        if (v.name == null || v.name == '') {
                            $scope.version += "Zabbix API 认证失败，或主机名称错误，请修改后重试！"
                        } else {

                            $scope.version += "name : " + v.name + "[Zabbix API 账户配置正确]\n"
                                + "hostid : " + v.hostid;
                        }
                    }
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.getZabbixVersion();

        $scope.closeModal = function () {
            $uibModalInstance.dismiss('cancel');
        }
    }
);


app.controller('addHostInstanceCtrl', function ($scope, $uibModalInstance, $state, httpService, serverItem) {

        $scope.serverItem = serverItem;
        $scope.zabbixHost = {};

        $scope.nowProxy = {};

        $scope.btnAddHost = true;


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


        var initSelectProxy = function () {
            if ($scope.zabbixHost.proxys == null || $scope.zabbixHost.proxys.length == 0) return;
            for (var i = 0; i < $scope.zabbixHost.proxys.length; i++) {
                if ($scope.zabbixHost.proxys[i].selected)
                    $scope.nowProxy.selected = $scope.zabbixHost.proxys[i];
            }

        }

        $scope.getHost = function () {
            var url = "/zabbixserver/host/get?serverId=" + serverItem.id;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.zabbixHost = data.body;
                    initSelectProxy();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }

        $scope.getHost();


        $scope.addHost = function () {
            $scope.closeAlert();

            if ($scope.zabbixHost.useProxy == true) {
                if ($scope.nowProxy.selected == null) {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = "启用代理必须指定代理服务器";
                    return;
                } else {
                    $scope.zabbixHost.proxy = $scope.nowProxy.selected;
                }
            }

            if ($scope.zabbixHost.host == '') {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Host名称";
                return;
            }

            if ($scope.zabbixHost.ip == '') {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定IP";
                return;
            }
            $scope.zabbixHost.serverVO = $scope.serverItem;


            $scope.btnAddHost = false;
            var url = "/zabbixserver/host/save";
            httpService.doPostWithJSON(url, $scope.zabbixHost).then(function (data) {
                if (data.success) {
                    var body = data.body;
                    if (body.success) {
                        $scope.alert.type = 'success';
                        $scope.alert.msg = "监控添加成功！";
                    } else {
                        $scope.alert.type = 'warning';
                        $scope.alert.msg = "监控添加失败，请检查关联的模版是否匹配当前主机！";
                    }
                } else {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = "监控添加失败，请检查配置！";
                }
            }, function (err) {
                $scope.alert.type = 'error';
                $scope.alert.msg = err;
            });
        }

        $scope.closeModal = function () {
            $uibModalInstance.dismiss('cancel');
        }
    }
);