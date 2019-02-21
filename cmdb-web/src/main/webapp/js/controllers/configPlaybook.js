'use strict';

// ansible-playbook 版本信息
app.controller('playbookVersionCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster) {
    $scope.version = {};


    $scope.playbookVersion = function () {
        if ($scope.version == null || $scope.version.version == null) {
            var url = "/task/ansible/playbook/version";
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

// 主配置页面
app.controller('configPlaybookCtrl', function ($scope, $state, $uibModal, toaster, $interval, httpService) {

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
        var url = "/config/filePlaybook/page";
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

    $scope.addConfigPlaybook = function () {
        var body = {
            id: 0,
            fileId: 0,
            fileName: "",
            serverGroupId: 0,
            serverGroupName: "",
            hostPattern: "",
            src: "",
            dest: "",
            username: "root",
            usergroup: "root",
            playbookId: 0
        }
        doSaveConfigPlaybook(body);
    }

    $scope.editConfigPlaybook = function (item) {
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

    var doSaveConfigPlaybook = function (configPlaybook) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configPlaybookModal',
            controller: 'configPlaybookInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                configPlaybook: function () {
                    return configPlaybook;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryPlaybook();
        }, function () {
            $scope.queryPlaybook();
        });
    }

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

    /**
     * 执行
     * @param id
     */
    $scope.doPlaybook = function (id) {
        // butInvokeRunning(true);
        var url = "/config/filePlaybook/do?id=" + id;

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


    $scope.playbookName = "";
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
app.controller('configPlaybookInstanceCtrl', function ($scope, $uibModalInstance, httpService, configPlaybook) {

    $scope.item = configPlaybook;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

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

    var getConfigFile = function () {
        var url = "/config/file/get";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.configFileList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    getConfigFile();

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
        var url = "/config/filePlaybook/save";

        // $scope.nowServerGroup = {};

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

        if ($scope.nowConfigFile.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定配置文件";
            return;
        } else {
            $scope.item.fileId = $scope.nowConfigFile.selected.id;
            $scope.item.fileName = $scope.nowConfigFile.selected.fileName;
            $scope.item.src = $scope.nowConfigFile.selected.filePath
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




app.controller('versionInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, toaster, versionItem) {
    //$scope.localAuthPoint = authPoint;
    $scope.versionItem = versionItem;

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});