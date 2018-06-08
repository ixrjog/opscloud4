'use strict';

app.controller('configFileCtrl', function ($scope, $state, $uibModal, toaster, httpService, staticModel) {
    $scope.nowFileGroup = {};
    $scope.fileGroupList = [];
    $scope.envType = staticModel.envType;

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

    $scope.fileName = "";

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
        var url = "/config/file/query?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
            + "&length=" + $scope.pageLength;

        var requestBody = {
            fileName: $scope.fileName,
            fileType: 1,
            fileGroupId: $scope.nowFileGroup.selected == null ? 0 : $scope.nowFileGroup.selected.id
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

    $scope.doQuery();

    $scope.addItem = function () {
        var body = {
            id: 0,
            fileName: "",
            fileDesc: "",
            filePath: "",
            fileType: 1,
            fileGroupId: 0,
            fileGroup: null,
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
        var url = "/config/file/del?id=" + id;
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

app.controller('configFileDivCtrl', function ($scope, $state, $uibModal, toaster, httpService) {
    $scope.nowFileGroup = {};
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

    $scope.fileName = "";

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
        var url = "/config/file/query?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
            + "&length=" + $scope.pageLength;

        var requestBody = {
            fileName: $scope.fileName,
            fileType: 0,
            fileGroupId: $scope.nowFileGroup.selected == null ? 0 : $scope.nowFileGroup.selected.id
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

    $scope.doQuery();

    $scope.addItem = function () {
        var body = {
            id: 0,
            fileName: "",
            fileDesc: "",
            filePath: "",
            fileType: 0,
            fileGroupId: 0,
            fileGroup: null,
            invokeCmd: "",
            params: null,
            fileContent: "",
            paramList: []
        }

        doSaveItem(body);
    }

    $scope.editItem = function (item) {
        var body = {
            id: item.id,
            fileName: item.fileName,
            envType: item.envType,
            fileDesc: item.fileDesc,
            filePath: item.filePath,
            fileType: item.fileType,
            fileGroupId: item.fileGroupId,
            fileGroup: item.fileGroupDO,
            invokeCmd: item.invokeCmd,
            params: item.params,
            fileContent: item.fileContent,
            paramList: item.params
        }

        doSaveItem(body);
    }

    var doSaveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'configFile',
            controller: 'configFileInstanceCtrl',
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

    $scope.delItem = function (id) {
        var url = "/config/file/del?id=" + id;
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

    $scope.createItem = function (id) {
        var url = "/config/file/create?id=" + id;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "创建成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.invokeItem = function (id) {
        var url = "/config/file/invoke?id=" + id;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "执行成功!", data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
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
                    size: 'md',
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

app.controller('configFileInstanceCtrl', function ($scope, $uibModalInstance, httpService, item, staticModel) {
    $scope.item = item;
    $scope.paramList = [];
    $scope.envTypes = staticModel.envType;

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

    $scope.addParam = function (index) {
        $scope.paramList.splice(index + 1, 0, "");
    }

    $scope.delParam = function (index) {
        $scope.paramList.splice(index, 1);
    }

    $scope.changeParam = function (index, paramItem) {
        $scope.paramList[index] = paramItem;
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

        if ($scope.item.envType == null || $scope.item.envType == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定环境类型";
            return;
        }

        if ($scope.item.filePath == null || $scope.item.filePath == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定路径";
            return;
        }

        if ($scope.item.fileType == 1 && ($scope.item.invokeCmd == null || $scope.item.invokeCmd == '')) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定执行命令";
            return;
        }

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