'use strict';

app.controller('ansibleVersionCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.version = {};


    $scope.ansibleVersion = function () {
        if ($scope.version == null || $scope.version.version == null) {
            var url = "/task/ansible/version";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.version = data.body;
                    $scope.viewVersion();
                }
            });
        } else {
            $scope.viewVersion();
        }
    }

    $scope.viewVersion = function () {

        var modalInstance = $uibModal.open({
            templateUrl: 'versionInfoModal',
            controller: 'versionInstanceCtrl',
            size: 'lg',
            resolve: {
                versionItem: function () {
                    return $scope.version;
                }
            }
        });
    }

});


app.controller('ansibleHostConfigCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {

    $scope.envType = staticModel.envType;

    $scope.useType = [];

    $scope.fileGroupList = [];

    $scope.filegroupName = "filegroup_ansible"

    $scope.filegroup = {};


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

    //导航条按钮控制
    $scope.butSaveDisabled = false;
    $scope.butSaveSpinDisabled = false;
    $scope.butInvokeDisabled = false;
    $scope.butInvokeSpinDisabled = false;
    $scope.butEditDisabled = false;
    $scope.butDelDisabled = false;

    var butSaveRunning = function (isRun) {
        $scope.butSaveDisabled = isRun;
        $scope.butInvokeDisabled = isRun;
        $scope.butEditDisabled = isRun;
        $scope.butDelDisabled = isRun;

        $scope.butSaveSpinDisabled = isRun;
    }

    var butInvokeRunning = function (isRun) {
        $scope.butSaveDisabled = isRun;
        $scope.butCheckDisabled = isRun;
        $scope.butSearchDisabled = isRun;
        $scope.butRefreshDisabled = isRun;

        $scope.butInvokeSpinDisabled = isRun;
    }


    var queryGroup = function () {
        var url = "/config/fileGroup/query?"
            + "groupName=" + $scope.filegroupName
            + "&page=" + 0
            + "&length=" + 20;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.fileGroupList = body.data;
                init();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.fileName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 15;

    $scope.pageChanged = function () {
        doQuery();
    };

    /////////////////////////////////////////////////
    var doQuery = function () {
        var url = "/config/file/query?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
            + "&length=" + $scope.pageLength;

        var requestBody = {
            fileName: $scope.fileName,
            fileType: 1,
            fileGroupId: $scope.filegroup.id
        }

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
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

    queryGroup();

    var init = function () {
        if ($scope.fileGroupList.length == 0) return;

        for (var i = 0; i < $scope.fileGroupList.length; i++) {
            if ($scope.fileGroupList[i].groupName == $scope.filegroupName) {
                $scope.filegroup = $scope.fileGroupList[i];
                doQuery();
                break;
            }
        }
    }

    init();

    $scope.addItem = function () {
        var body = {
            id: 0,
            fileName: "",
            fileDesc: "",
            filePath: "",
            fileType: 1,
            useType: -1,
            fileGroupId: 0,
            fileGroup: $scope.filegroup,
            invokeCmd: "",
            params: null,
            paramList: []
        }

        doSaveItem(body);
    }

    $scope.editItem = function (item) {
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

        doSaveItem(body);
    }

    var doSaveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configFile',
            controller: 'configFileInstanceCtrl',
            size: 'md',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                useType: function () {
                    return $scope.useType;
                },
                item: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            doQuery();
        }, function () {
            doQuery();
        });
    }

    $scope.delItem = function (id) {
        var url = "/config/file/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

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


app.controller('ansibleConfigCopyCtrl', function ($scope, $state, $uibModal, toaster, $interval, httpService, staticModel) {

    $scope.envType = staticModel.envType;
    $scope.useType = [];
    $scope.fileGroupList = [];
    $scope.filegroupName = "filegroup_ansible"
    $scope.filegroup = {};

    var queryGroup = function () {
        var url = "/config/fileGroup/query?"
            + "groupName=" + $scope.filegroupName
            + "&page=" + 0
            + "&length=" + 20;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.fileGroupList = body.data;
                init();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    queryGroup();

    var init = function () {
        if ($scope.fileGroupList.length == 0) return;

        for (var i = 0; i < $scope.fileGroupList.length; i++) {
            if ($scope.fileGroupList[i].groupName == $scope.filegroupName) {
                $scope.filegroup = $scope.fileGroupList[i];
                $scope.doQuery();
                break;
            }
        }
    }

    init();

    //导航条按钮控制
    $scope.butRunCopyItem = false;

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
        var url = "/config/fileCopy/query?"
            + "groupName=" + $scope.filegroupName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.pageData = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.addItem = function () {
        var item = {
            id: 0,
            groupId: 0,
            groupName: "",
            serverId: 0,
            src: "",
            dest: "",
            username: "root",
            usergroup: "root",
            serverDO: {},
            configFileGroupDO: $scope.filegroup
        }

        doSaveItem(item);
    }

    $scope.editItem = function (item) {
        doSaveItem(item);
    }

    var doSaveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configCopyInfo',
            controller: 'configCopyInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                useType: function () {
                    return $scope.useType;
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

    $scope.delItem = function (id) {
        var url = "/config/fileCopy/del?id=" + id;
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

    $scope.copyItem = function (id) {
        $scope.butRunCopyItem = true;
        var url = "/task/copy/doFileCopy?id=" + id;


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

// 新建/编辑配置文件
app.controller('configFileInstanceCtrl', function ($scope, $uibModalInstance, httpService, useType, item) {
    $scope.useType = useType;
    $scope.item = item;
    $scope.paramList = [];
    //$scope.envTypes = staticModel.envType;

    for (var i = 0; i < $scope.item.paramList.length; i++) {
        $scope.paramList.push($scope.item.paramList[i]);
    }

    if ($scope.item.fileGroup == null) {
        $scope.nowFileGroup = {};
    } else {
        $scope.nowFileGroup = {
            selected: $scope.item.fileGroup
        };
    }
    $scope.fileGroupList = [];

    $scope.queryGroup = function (param) {
        var url = "/config/fileGroup/query?"
            + "groupName=" + param
            + "&page=" + 0
            + "&length=" + 15;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.fileGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
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


    $scope.saveData = function () {
        var url = "/config/file/save";

        if ($scope.nowFileGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定文件组名";
            return;
        }
        $scope.item.fileGroupId = $scope.nowFileGroup.selected.id;

        if ($scope.item.fileName == null || $scope.item.fileName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定文件名";
            return;
        }

        if ($scope.item.useType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定使用类型";
            return;
        }

        if ($scope.item.filePath == null || $scope.item.filePath == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定路径";
            return;
        }

        // if ($scope.item.fileType == 1 && ($scope.item.invokeCmd == null || $scope.item.invokeCmd == '')) {
        //     $scope.alert.type = 'warning';
        //     $scope.alert.msg = "必须指定执行命令";
        //     return;
        // }

        $scope.item.params = JSON.stringify($scope.paramList);
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

// 新增/编辑服务器同步配置
app.controller('configCopyInstanceCtrl', function ($scope, $uibModalInstance, httpService, item) {

    $scope.item = item;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.nowServer = {};
    $scope.serverList = [];

    $scope.filePathList = [];


    var queryFilePath = function () {
        var url = "/config/file/queryPath?fileGroupId=" + $scope.item.configFileGroupDO.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.filePathList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }


    var init = function () {
        // edit
        if ($scope.item.id != 0) {
            $scope.nowServerGroup.selected = $scope.item.serverGruopDO;
            $scope.nowServer.selected = $scope.item.serverDO;
        }
        queryFilePath();

    }


    init();


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
        var url = "/config/fileCopy/save";

        if ($scope.nowServer.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器";
            return;
        } else {
            $scope.item.serverDO = $scope.nowServer.selected;
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

        $scope.item.groupId = $scope.item.configFileGroupDO.id;
        $scope.item.groupName = $scope.item.configFileGroupDO.groupName;

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
 * Ansible参数配置
 */
app.controller('ansibleConfigCtrl', function ($scope, $state, $uibModal, $sce, httpService, toaster) {

        $scope.configMap = {};

        $scope.itemGroup = "ANSIBLE";

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

        $scope.describeRegions = function () {
            var url = "/aliyun/api/describeRegions";

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    var regions = data.body;
                    viewRegions(regions);
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }


        // 查看regions
        var viewRegions = function (regions) {
            var modalInstance = $uibModal.open({
                templateUrl: 'regionsInfo',
                controller: 'regionsInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    regions: function () {
                        return regions;
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

app.controller('versionInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, toaster, versionItem) {
    //$scope.localAuthPoint = authPoint;
    $scope.versionItem = versionItem;

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});