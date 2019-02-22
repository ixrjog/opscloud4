'use strict';

app.controller('nginxVhostCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {

    $scope.envType = staticModel.envType;
    $scope.useType = [];
    $scope.queryServerName = "";
    $scope.taskRunning = false;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////
    $scope.doQuery = function () {
        var url = "/nginx/vhost/page?"
            + "serverName=" + $scope.queryServerName
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

    $scope.addVhost = function () {
        var body = {
            id: 0,
            serverKey: "",
            serverName: "",
            content: "",
            envList: []
        }

        doSaveItem(body);
    }

    $scope.editVhost = function (item) {
        doSaveItem(item);
    }

    var doSaveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'vhostInfo',
            controller: 'vhostInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                item: function () {
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

    $scope.configureVhost = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'vhostConfigureInfo',
            controller: 'vhostConfigureInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                item: function () {
                    return item;
                },
                envType: function () {
                    return $scope.envType;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


    $scope.delVhost = function (id) {
        var url = "/nginx/vhost/del?id=" + id;
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

    $scope.editVhostEnv = function (item) {
        doSaveItem(item);
    }


    $scope.launchFile = function (id, type) {
        var modalInstance = $uibModal.open({
            templateUrl: 'launchFileInfoModal',
            controller: 'launchFileInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                id: function () {
                    return id;
                },
                type: function () {
                    return type;
                }
            }
        });
    }

    $scope.buildFile = function (id) {
        $scope.taskRunning = true;
        var url = "/nginx/vhost/env/file/build?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.taskRunning = false;
                toaster.pop("success", "保存成功!", data.body);
            } else {
                $scope.taskRunning = false;
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            $scope.taskRunning = false;
            toaster.pop("error", err);
        });
    }
});


// 新建/编辑vhost
app.controller('vhostInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, item) {

    $scope.item = item;

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


    $scope.saveItem = function () {
        var url = "/nginx/vhost/save";

        if ($scope.item.serverName == null || $scope.item.serverName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "虚拟主机名称";
            return;
        }

        if ($scope.item.serverKey == null || $scope.item.serverKey == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "虚拟主机Key";
            return;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {
                    fileType: $scope.item.fileType
                };
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});

// 配置vhost
app.controller('vhostConfigureInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, item, envType) {

    $scope.item = item;
    $scope.envType = envType;
    $scope.nowEnv = {};
    $scope.nowFile = {};

    var initItem = function () {
        if ($scope.item.envList == null || $scope.item.envList.length == 0)
            return;
        for (var i = 0; i < $scope.item.envList.length; i++) {
            for (var j = 0; j < $scope.envType.length; j++) {
                if ($scope.envType[j].code == $scope.item.envList[i].envType) {
                    $scope.item.envList[i].envName = $scope.envType[j].name;
                    break;
                }
            }
        }
    }

    initItem();

    $scope.restEnv = function () {
        $scope.nowEnv = {
            id: 0,
            vhostId: 0,
            envType: -1,
            confPath: "",
            content: ""
        }
    }

    $scope.resetFile = function () {
        $scope.nowFile = {};
    }

    $scope.restEnv();

    $scope.alert = {
        type: "",
        msg: ""
    };

    $scope.editEnv = function (env) {
        $scope.nowEnv = Object.assign({}, env);
    }

    $scope.delEnv = function (id) {
        var url = "/nginx/vhost/env/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功！";
                $scope.doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }


    $scope.saveItem = function () {
        var url = "/nginx/vhost/env/save";

        if ($scope.nowEnv.envType == null || $scope.item.serverName == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必需指定环境";
            return;
        }

        if ($scope.nowEnv.confPath == null || $scope.nowEnv.confPath == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必需指定配置文件路径";
            return;
        }

        if ($scope.nowEnv.vhostId == 0) {
            $scope.nowEnv.vhostId = $scope.item.id;
        }

        httpService.doPostWithJSON(url, $scope.nowEnv).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.restEnv();
                $scope.doQuery();

            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.doQuery = function () {
        var url = "/nginx/vhost/get?id=" + $scope.item.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.item = body;
                initItem();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ////////////////////////////////////////////////////
    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];


    $scope.vhostServerGroups = [];

    $scope.doQueryServerGroup = function () {
        var url = "/nginx/vhost/serverGroup/query?vhostId=" + $scope.item.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.vhostServerGroups = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'err';
            $scope.alert.msg = err;
        });
    }
    $scope.doQueryServerGroup();

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'err';
            $scope.alert.msg = err;
        });
    }

    $scope.addServerGroup = function (choose) {
        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择服务器组才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/nginx/vhost/serverGroup/add?vhostId=" + $scope.item.id + "&serverGroupId=" + $scope.nowServerGroup.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "添加服务器组成功！";
                $scope.doQueryServerGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'err';
            $scope.alert.msg = err;
        });
    }

    $scope.delServerGroup = function (id) {
        var url = "/nginx/vhost/serverGroup/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.doQueryServerGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }


    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});

// 主配置页面
app.controller('playbookCtrl', function ($scope, $state, $uibModal, toaster, $interval, httpService) {

    $scope.playbookList = [];


    var initHostPattern = function () {
        if ($scope.playbookList.length == 0) return;

        for (var i = 0; i < $scope.playbookList.length; i++) {
            var playbook = $scope.playbookList[i];
            for (var j = 0; j < playbook.groupHostPattern.length; j++) {
                if (playbook.hostPattern == playbook.groupHostPattern[j].hostPattern) {
                    playbook.servers = playbook.groupHostPattern[j].servers;
                    break;
                }
            }
        }
    }

    $scope.queryPlaybook = function () {
        var url = "/nginx/playbook/page";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.playbookList = data.body;
                initHostPattern();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryPlaybook();

    $scope.addPlaybook = function () {
        var body = {
            id: 0,
            vhostId: 0,
            serverKey: "",
            serverGroupId: 0,
            serverGroupName: "",
            hostPattern: "",
            src: "",
            dest: "",
            username: "root",
            usergroup: "root",
            playbookId: 0
        }
        doSavePlaybook(body);
    }

    $scope.editPlaybook = function (item) {
        var body = {
            id: item.id,
            fileName: item.fileName,
            useType: item.useType,
            envType: item.envType,
            fileDesc: item.fileDesc,
            filePath: item.filePath,
            fileType: item.fileType,
            fileGroupId: item.fileGroupId,
            fileGroup: item.fileGroupDO,
            invokeCmd: item.invokeCmd,
            params: item.params,
            paramList: item.params
        }

        doSavePlaybook(body);
    }

    var doSavePlaybook = function (playbook) {
        var modalInstance = $uibModal.open({
            templateUrl: 'nginxPlaybookModal',
            controller: 'playbookInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                playbook: function () {
                    return playbook;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryPlaybook();
        }, function () {
            $scope.queryPlaybook();
        });
    }

    $scope.delPlaybook = function (id) {
        var url = "/nginx/playbook/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryPlaybook();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /**
     * 执行
     * @param id
     */
    $scope.doPlaybook = function (id) {

        var url = "/nginx/playbook/do?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                // butInvokeRunning(false);
                $scope.playbookLog = data.body;
                toaster.pop("success", "执行成功!", data.body);
            } else {
                // butInvokeRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            butInvokeRunning(false);
            toaster.pop("error", err);
        });
    }

    var getPlaybookLog = function () {
        var url = "/config/filePlaybook/getLog?id=" + $scope.playbookLog.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.playbookLog = data.body;
            } else {
                // butInvokeRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var timerQueryLog = $interval(function () {
        if ($scope.playbookLog == null || $scope.playbookLog.complete == null) return;
        if ($scope.playbookLog.complete == false) {
            getPlaybookLog();
        }
    }, 2000);


    $scope.createItem = function (id) {
        butSaveRunning(true);
        var url = "/config/file/create?id=" + id;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                butSaveRunning(false);
                toaster.pop("success", "创建成功!");
            } else {
                butSaveRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            butSaveRunning(false);
            toaster.pop("error", err);
        });
    }

    $scope.invokeItem = function (id) {
        butInvokeRunning(true);
        var url = "/config/file/invoke?id=" + id;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                butInvokeRunning(false);
                toaster.pop("success", "执行成功!", data.body);
            } else {
                butInvokeRunning(false);
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            butInvokeRunning(false);
            toaster.pop("error", err);
        });
    }

    $scope.launchItem = function (id) {
        var url = "/config/file/launch?id=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'configFileInfo',
                    controller: 'configFileInfoInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        item: function () {
                            return data.body;
                        }
                    }
                });
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }
});

// Playbook日志查询
app.controller('playbookLogCtrl', function ($scope, $state, $uibModal, toaster, httpService) {


    $scope.playbookName = "playbook-nginx.yml";
    $scope.username = "";

    $scope.reSet = function () {
        $scope.playbookName = "";
        $scope.queryUsername = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        doQuery();
    };

    /////////////////////////////////////////////////

    var doQuery = function () {
        var url = "/config/filePlaybook/queryLogPage?"
            + "playbookName=" + ($scope.playbookName == null ? "" : $scope.playbookName)
            + "&username=" + ($scope.username == null ? "" : $scope.username)
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

    doQuery();

    $scope.delItem = function (id) {
        var url = "/config/filePlaybook/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryPlaybook();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.viewLog = function (playbookLog) {
        var modalInstance = $uibModal.open({
            templateUrl: 'playbookLogInfoModal',
            controller: 'playbookLogInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                playbookLog: function () {
                    return playbookLog;
                }
            }
        });
    }

});

// 新增/编辑Playbook
app.controller('playbookInstanceCtrl', function ($scope, $uibModalInstance, httpService, playbook) {

    $scope.item = playbook;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.nowVhost = {};
    $scope.vhostList = [];

    $scope.configFileList = [];

    $scope.nowConfigFile = {};

    $scope.hostPatternList = {};
    $scope.nowHostPattern = {};

    $scope.filePathList = [];

    $scope.playbookList = [];

    $scope.nowPlaybook = {};

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
                //$scope.queryHostPattern();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    $scope.queryVhost = function (queryParam) {
        var url = "/nginx/vhost/page?"
            + "serverName=" + queryParam
            + "&page=0"
            + "&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.vhostList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var queryPlaybook = function () {
        var url = "/task/script/getPlaybook";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.playbookList = data.body;
                ;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    queryPlaybook();

    $scope.changeServerGroup = function () {
        queryHostPattern();
    }

    var queryHostPattern = function () {
        if ($scope.nowServerGroup.selected == null) return;

        var url = "/servergroup/hostPattern/get?"
            + "serverGroupId=" + $scope.nowServerGroup.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.hostPatternList = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
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


    $scope.saveItem = function () {
        var url = "/nginx/playbook/save";

        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器组";
            return;
        } else {
            $scope.item.serverGroupId = $scope.nowServerGroup.selected.id;
            $scope.item.serverGroupName = $scope.nowServerGroup.selected.name;
        }

        if ($scope.nowHostPattern.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定HostPattern";
            return;
        } else {
            $scope.item.hostPattern = $scope.nowHostPattern.selected.hostPattern;
        }

        if ($scope.nowVhost.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定配置文件";
            return;
        } else {
            $scope.item.vhostId = $scope.nowVhost.selected.id;
            $scope.item.serverKey = $scope.nowVhost.selected.serverKey;
        }

        if ($scope.item.src == null || $scope.item.src == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定源路径";
            return;
        }

        if ($scope.item.dest == null || $scope.item.dest == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定目标路径";
            return;
        }

        if ($scope.nowPlaybook.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Playbook";
            return;
        } else {
            $scope.item.playbookId = $scope.nowPlaybook.selected.id;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {
                    fileType: $scope.item.fileType
                };
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});

app.controller('playbookLogInfoInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, playbookLog) {

    $scope.playbookLog = playbookLog;

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});

/**
 * 查看配置文件
 */
app.controller('launchFileInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, id, type) {

    $scope.envFileId = id
    $scope.type = type;

    $scope.nginxFile = {};


    $scope.getNginxFile = function () {
        var url = "/nginx/vhost/env/file/launch?id=" + $scope.envFileId + "&type=" + $scope.type;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.nginxFile = body;
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.getNginxFile();

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


});

/**
 * nginx配置文件查看
 */
app.controller('configFileInfoInstanceCtrl', function ($scope, $uibModalInstance, $parse, item) {
    $scope.item = item;

    $scope.toPrettyJSON = function (objStr, tabWidth) {
        try {
            var obj = $parse(objStr)({});
        } catch (e) {
            // eat $parse error
            return objStr;
        }

        return JSON.stringify(obj, null, Number(tabWidth));
    };
});


/**
 * nginx配置文件查看
 */
app.controller('launchLogInstanceCtrl', function ($scope, $uibModalInstance, $parse, log) {
    $scope.log = log;

    $scope.toPrettyJSON = function (objStr, tabWidth) {
        try {
            var obj = $parse(objStr)({});
        } catch (e) {
            // eat $parse error
            return objStr;
        }

        return JSON.stringify(obj, null, Number(tabWidth));
    };
});

