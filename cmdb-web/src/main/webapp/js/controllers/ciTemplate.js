'use strict';

app.controller('ciVersionCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService) {
    $scope.version = {};


    var version = function () {
        var url = "/ci/version";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.version = data.body;
            }
        });
    }
    version();
});

app.controller('templateCtrl', function ($scope, $state, $uibModal, $parse, $sce, $interval, $localStorage, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.ciType = staticModel.ciType;
    $scope.appType = staticModel.appType;
    $scope.rollbackType = staticModel.rollbackType;
    $scope.envType = staticModel.envType;

    $scope.previewItem = {};

    $scope.queryName = "";
    $scope.queryAppType = -1;
    $scope.queryCiType = -1;


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

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;


    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.queryAppType = -1;
        $scope.queryCiType = -1;
    }

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    $scope.doQuery = function () {
        var url = "/ci/template/page?"
            + "name=" + $scope.queryName
            + "&appType=" + $scope.queryAppType
            + "&ciType=" + $scope.queryCiType
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

    $scope.updateTemplate = function (jobId, type) {
        var url = "/ci/template/update?jobId=" + jobId + "&type=" + type;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop('success',"更新成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.scanTemplate = function () {
        var url = "/ci/template/scan";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop('success',"模版扫描成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.updateTemplates = function (id) {
        var url = "/ci/template/updates?templateId=" + id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop('success',"更新所有成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.viewTemplate = function (item) {
        var url = "/ci/template/preview?id=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.previewItem = data.body;
                var modalInstance = $uibModal.open({
                    templateUrl: 'previewTemplateModal',
                    controller: 'previewInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        httpService: function () {
                            return httpService;
                        },
                        modeType: function () {
                            return "xml";
                        },
                        previewItem: function () {
                            return $scope.previewItem;
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

    $scope.delTemplate = function (item) {
        var url = "/ci/template/del?id=" + item.id;

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


    /////////////////////////////////////////////////

    $scope.addTemplate = function () {
        var item = {
            id: 0,
            name: "",
            version: 1,
            appType: -1,
            ciType: -1,
            rollbackType: -1,
            envType: -1,
            content: ""
        }
        templateModal(item);
    }

    $scope.editTemplate = function (item) {
        templateModal(item);
    }


    var templateModal = function (templateItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'templateInfoModal',
            controller: 'templateInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                appType: function () {
                    return $scope.appType;
                },
                ciType: function () {
                    return $scope.ciType;
                },
                rollbackType: function () {
                    return $scope.rollbackType;
                },
                envType: function () {
                    return $scope.envType;
                },
                templateItem: function () {
                    return templateItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }


    $scope.editScript = function (scriptItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'scriptInfoModal',
            controller: 'scriptInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                sysScriptType: function () {
                    return $scope.sysScriptType;
                },
                scriptType: function () {
                    return $scope.scriptType;
                },
                isView: function () {
                    return false;
                },
                scriptItem: function () {
                    return scriptItem;
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


app.controller('templateInfoInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, ciType, appType, rollbackType, envType, templateItem) {

    $scope.ciType = ciType;
    $scope.appType = appType;
    $scope.rollbackType = rollbackType;
    $scope.envType = envType;

    $scope.templateItem = templateItem;

    $scope.templateList = [];

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

    $scope.resetApp = function () {
        $scope.jobItem = {
            id: 0,
            jobName: "",
            jobEnvType: -1,
            repositoryUrl: "",
            buildType: -1,
            content: ""
        }
    }

    var queryJenkinsTemplate = function () {
        if ($scope.templateItem.id != 0) return;

        var url = "/ci/jenkins/template";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.templateList = data.body;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    queryJenkinsTemplate();

    $scope.setTemplate = function (template) {
        $scope.templateItem.name = template.name;
    }

    $scope.saveTemplate = function () {

        var url = "/ci/template/save";

        if ($scope.templateItem.name == null || $scope.templateItem.name == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "模版名称未指定!";
            return;
        }
        if ($scope.templateItem.ciType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构建类型未指定!";
            return;
        }
        if ($scope.templateItem.appType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "应用类型未指定!";
            return;
        }

        // if ($scope.templateItem.rollbackType == -1) {
        //     $scope.alert.type = 'warning';
        //     $scope.alert.msg = "回滚类型未指定!";
        //     return;
        // }

        httpService.doPostWithJSON(url, $scope.templateItem).then(function (data) {
            if (data.success) {
                $scope.templateItem = data.body;

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




});


app.controller('previewInstanceCtrl', function ($scope, $uibModalInstance, $uibModal, $sce, staticModel, toaster, httpService, modeType, previewItem) {

    $scope.modeType = modeType;
    $scope.previewItem = previewItem;
    $scope.modes = ['sh', 'yaml', 'python', 'php', 'lua', 'scheme'];


    var init = function () {
        $scope.aceOption = {
            useWrapMode: true,
            mode: $scope.modeType
        };
    }

    init();

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});