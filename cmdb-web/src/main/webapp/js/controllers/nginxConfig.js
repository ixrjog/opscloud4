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
 * 远程同步配置
 */
app.controller('copyCtrl', function ($scope, $state, $uibModal, toaster, $interval, httpService, staticModel) {

    $scope.envType = staticModel.envType;
    $scope.businessKey = "NGINX"
    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;

    // 行-同步按钮
    $scope.copyRunning = false;

    var queryCopy = function () {
        var url = "/copy/page?"
            + "businessKey=" + $scope.businessKey
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

    queryCopy();

    $scope.pageChanged = function () {
        queryCopy();
    }


    // var init = function () {
    //     if ($scope.fileGroupList.length == 0) return;
    //     for (var i = 0; i < $scope.fileGroupList.length; i++) {
    //         if ($scope.fileGroupList[i].groupName == $scope.filegroupName) {
    //             $scope.filegroup = $scope.fileGroupList[i];
    //             $scope.doQuery();
    //             break;
    //         }
    //     }
    // }
    //
    // init();

    //导航条按钮控制
    //$scope.butRunCopyItem = false;

    /////////////////////////////////////////////////

    $scope.addCopy = function () {
        var item = {
            id: 0,
            businessKey: "",
            businessId: 0,
            srcPath: "",
            destPath: "",
            username: "root",
            usergroup: "root",
            doCopy: true,
            doScript: false,
            taskScriptId: 0,
            params: "",
            content: ""
        }

        doSaveCopy(item);
    }

    $scope.editCopy = function (item) {
        doSaveCopy(item);
    }

    var doSaveCopy = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'copyInfo',
            controller: 'copyInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                envType: function () {
                    return $scope.envType;
                },
                item: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            queryCopy();
        }, function () {
            queryCopy();
        });
    }

    $scope.delCopy = function (id) {
        var url = "/copy/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                queryCopy();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doCopy = function (id) {
        $scope.copyRunning = true;
        var url = "/copy/doCopy?copyId=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO.ansibleTaskDO = body.body;
                $scope.copyRunning = false;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
            $scope.copyRunning = false;
        });
    }


    $scope.copyItemAll = function () {
        $scope.butRunCopyItem = true;

        $scope.filegroupName
        var url = "/task/copy/doFileCopyAll?groupName=" + $scope.filegroupName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO.ansibleTaskDO = body.body;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }


    $scope.resultChoose = {
        all: true,
        success: false,
        failed: false
    };

    $scope.allChoose = false;


    $scope.chooseAllResult = function () {
        $scope.resultChoose = {
            all: true,
            success: false,
            failed: false
        };
    }

    $scope.chooseResult = function (code) {
        var success = false;
        var failed = false;
        if (code == 0) {
            success = true;
        }
        if (code == 1) {
            failed = true;
        }
        $scope.resultChoose = {
            all: false,
            success: success,
            failed: failed
        };
    }

    $scope.checkResultShow = function (taskServer) {
        if ($scope.resultChoose.all == true) return true;

        var result = taskServer.result == 'SUCCESS';

        if (result) {
            if ($scope.resultChoose.success) {
                return true;
            } else {
                return false;
            }
        } else {
            if ($scope.resultChoose.failed) {
                return true;
            } else {
                return false;
            }
        }
    }


    $scope.taskVO = {};

    // x秒刷新1次待办工单
    var timer1 = $interval(function () {

        if ($scope.taskVO.ansibleTaskDO == null) return;

        if ($scope.taskVO.ansibleTaskDO.finalized == false) {
            $scope.queryTask();
        }
    }, 3000);

    $scope.queryTask = function () {
        var url = "/task/cmd/query?taskId=" + $scope.taskVO.ansibleTaskDO.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO = body.body;
                $scope.butRunCopyItem = false;
            }
        });
    }

    /**
     * 关闭任务详情
     * @param serverId
     */
    $scope.closeTaskServer = function (serverId) {
        // $scope.taskVO.taskServerList.remove(0);
        if ($scope.taskVO.taskServerList == null || $scope.taskVO.taskServerList.length == 0) return;
        for (var i = 0; i < $scope.taskVO.taskServerList.length; i++) {
            var id = $scope.taskVO.taskServerList[i].serverId;
            if (serverId === id) {
                $scope.taskVO.taskServerList.splice(i, 1);
                return;
            }
        }
    }

    $scope.btnDoFileCopy = false;

    $scope.doFileCopy = function (id) {

        $scope.btnDoFileCopy = true;

        var url = "/task//copy/doFileCopy?id=" + id;

        var requestBody = {
            hostGroup: $scope.hostGroup,
            taskScriptId: $scope.nowTaskScript.selected.id,
            params: $scope.params,
            serverList: serverList
        }
        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.taskVO.ansibleTaskDO = body.body;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }
});


/**
 * 新增/编辑服务器同步配置
 */
app.controller('copyInstanceCtrl', function ($scope, $uibModalInstance, httpService, envType, item) {

    $scope.businessKey = "NGINX";
    $scope.isEdit = false;

    $scope.item = item;
    $scope.envType = envType;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.nowServer = {};
    $scope.serverList = [];

    $scope.vhostList = [];
    $scope.nowVhost = {};
    $scope.nowEnv = {};

    $scope.queryServerName = "";

    /////////////////////
    $scope.queryScriptName = "";
    $scope.scriptList = [];
    $scope.nowScript = {};


    $scope.queryVhost = function () {
        var url = "/nginx/vhost/page?"
            + "serverName=" + $scope.queryServerName
            + "&page=0&length=20";

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

    $scope.initEnv = function () {
        if ($scope.nowVhost.selected.envList == null || $scope.nowVhost.selected.envList.length == 0)
            return;
        for (var i = 0; i < $scope.nowVhost.selected.envList.length; i++) {
            for (var j = 0; j < $scope.envType.length; j++) {
                if ($scope.envType[j].code == $scope.nowVhost.selected.envList[i].envType) {
                    $scope.nowVhost.selected.envList[i].envName = $scope.envType[j].name;
                    break;
                }
            }
        }
    }

    /**
     * 编辑初始化选中数据
     */
    var init = function () {
        if (item.id == 0) return;
        $scope.nowVhost.selected = $scope.item.vhostVO;
        $scope.initEnv();
        for (var i = 0; i < $scope.item.vhostVO.envList.length; i++) {
            if ($scope.item.vhostVO.envList[i].id = $scope.item.businessId) {
                $scope.nowEnv.selected = $scope.item.vhostVO.envList[i];
                break;
            }
        }
        // 编辑 无法修改部分配置
        $scope.isEdit = true;

        if ($scope.item.doScript) {
            $scope.nowScript.selected = $scope.item.taskScriptDO;
        }

    }

    init();

    // var queryFilePath = function () {
    //     var url = "/config/file/queryPath?fileGroupId=" + $scope.item.configFileGroupDO.id;
    //     httpService.doGet(url).then(function (data) {
    //         if (data.success) {
    //             $scope.filePathList = data.body;
    //         } else {
    //             toaster.pop("warning", data.msg);
    //         }
    //     }, function (err) {
    //         toaster.pop("error", err);
    //     });
    // }
    //
    //
    // var init = function () {
    //     // edit
    //     if ($scope.item.id != 0) {
    //         $scope.nowServerGroup.selected = $scope.item.serverGruopDO;
    //         $scope.nowServer.selected = $scope.item.serverDO;
    //     }
    //     queryFilePath();
    //
    // }
    //
    //
    // init();


    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
                $scope.queryServer("");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

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
        var url = "/copy/save";

        // if ($scope.nowServer.selected == null) {
        //     $scope.alert.type = 'warning';
        //     $scope.alert.msg = "必须指定服务器";
        //     return;
        // } else {
        //     $scope.item.serverDO = $scope.nowServer.selected;
        // }
        if ($scope.nowEnv.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定VHOST的环境配置";
            return;
        } else {
            $scope.item.businessKey = $scope.businessKey;
            $scope.item.businessId = $scope.nowEnv.selected.id;
        }


        if ($scope.nowEnv.selected.confPath == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定源路径";
            return;
        } else {
            $scope.item.srcPath = $scope.nowEnv.selected.confPath;
        }

        if ($scope.item.destPath == null || $scope.item.destPath == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定目标路径";
            return;
        }

        if ($scope.item.username == '' || $scope.item.usergroup == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Owner/Group";
            return;
        }

        if ($scope.item.doScript) {
            if ($scope.nowScript.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "必须指定Script";
                return;
            } else {
                $scope.item.taskScriptId = $scope.nowScript.selected.id;
            }
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


    $scope.queryScript = function () {
        var url = "/task/script/page?"
            + "scriptName=" + $scope.queryScriptName
            + "&sysScript=1&page=0&length=20";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.scriptList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.queryScript();


    $scope.getCopy = function () {
        var url = "/copy/get?id=" + $scope.item.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.item = body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }


    $scope.addServer = function () {
        var url = "/copy/addServer?"
            + "copyId=" + $scope.item.id
            + "&serverId=" + $scope.nowServer.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "添加成功!";
                $scope.getCopy();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.delServer = function (id) {
        var url = "/copy/delServer?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.getCopy();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }


    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});

/**
 * 同步日志详情页
 */
app.controller('copyLogCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {

    $scope.envType = staticModel.envType;
    $scope.queryServerName = "";
    $scope.nowEnvType = -1;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;

    $scope.reSet = function () {
        $scope.queryServerName = "";
        $scope.nowEnvType = -1;
    };


    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////
    $scope.doQuery = function () {
        var url = "/copy/log/page?"
            + "serverName=" + $scope.queryServerName
            + "&envType=" + $scope.nowEnvType
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

    $scope.delLog = function (id) {
        var url = "/copy/log/del?id=" + id;
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


    /**
     *
     * @param type
     */
    $scope.launchLog = function (type, item) {
        var log = {};
        // copy日志
        if (type == 0) {
            log = {
                success: item.copySuccess,
                path: "",
                msg: item.copyMsg,
                type: type,
                result: item.copyResult
            }
        }

        // script日志
        if (type == 1) {
            log = {
                success: item.scriptSuccess,
                path: item.scriptStdoutPath,
                msg: item.scriptMsg,
                type: type,
                result: item.scriptResult
            }

        }

        var modalInstance = $uibModal.open({
            templateUrl: 'launchLogInfoModal',
            controller: 'launchLogInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                log: function () {
                    return log;
                },
                type: function () {
                    return type;
                }
            }
        });
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

