'use strict';


/**
 * 工作流主界面
 */
app.controller('ciAppCtrl', function ($scope, $uibModal, $state, $sce, toaster, httpService, staticModel) {

        $scope.rollbackType = staticModel.rollbackType;
        $scope.jobEnvType = staticModel.envType;
        $scope.ciType = staticModel.ciType;
        $scope.appType = staticModel.appType;

        $scope.authPoint = $state.current.data.authPoint;

        $scope.ciAppList = [];

        $scope.nowCiApp = {};
        $scope.ciJobList = [];

        $scope.btnDefault = {
            background: "white",
            color: "#777"
        }

        $scope.btnClick = {
            background: "green",
            color: "white"
        }

        var initApp = function (ciApp) {
            if ($scope.ciAppList.length != 0) {
                for (var i = 0; i < $scope.ciAppList.length; i++) {
                    if (ciApp != null && ciApp.projectName == $scope.ciAppList[i].projectName) {
                        $scope.ciAppList[i].btn = $scope.btnClick;
                        $scope.ciAppList[i].click = true;
                    } else {
                        $scope.ciAppList[i].btn = $scope.btnDefault;
                        $scope.ciAppList[i].click = false;

                    }
                }
            }
        }

        $scope.queryAppJob = function () {
            var url = "/ci/job/query?appId=" + $scope.nowCiApp.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.ciJobList = data.body;
                }
            });
        }

        $scope.queryMyCiApp = function (queryName) {
            var url = "/ci/app/query?projectName=" + (queryName == null ? "" : queryName);
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.ciAppList = data.body;
                    initApp(null);
                }
            });
        }

        $scope.setCiApp = function (ciApp) {
            $scope.nowCiApp = ciApp;
            initApp(ciApp);
            $scope.queryAppJob();

        }

        $scope.queryMyCiApp();


        $scope.editApp = function (ciApp) {
            appModal(ciApp)
        }

        $scope.addApp = function () {
            var ciApp = {
                id: 0,
                appName: "",
                appType: 0,
                authBranch: false,
                ciType: 0,
                content: "",
                dingtalkId: 0,
                projectId: 0,
                projectName: "",
                serverGroupName: "",
                sshUrl: "",
                userId: 0,
                webUrl: ""
            }

            appModal(ciApp)
        }

        var appModal = function (appItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciAppModal',
                controller: 'ciAppInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    ciType: function () {
                        return $scope.ciType;
                    },
                    appType: function () {
                        return $scope.appType;
                    },
                    appItem: function () {
                        return appItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });

        }

        $scope.addJob = function () {
            var jobItem = {
                id: 0,
                name: $scope.nowCiApp.appName,
                content: "",
                appId: $scope.nowCiApp.id,
                ciType: $scope.nowCiApp.ciType,
                branch: "",
                branchList: $scope.nowCiApp.branchList,
                rollbackType: 0,
                envType: -1,
                jobName: "",
                jobTemplate: "",
                jobVersion: 0,
                jobEnvType: -1,
                deployJobName: "",
                deployJobTemplate: "",
                deployJobVersion: 0
            }
            jobModal(jobItem);
        }

        $scope.editJob = function (jobItem) {
            jobModal(jobItem);
        }

        var jobModal = function (jobItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciJobModal',
                controller: 'ciJobInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    jobEnvType: function () {
                        return $scope.jobEnvType;
                    },
                    ciType: function () {
                        return $scope.ciType;
                    },
                    appType: function () {
                        return $scope.appType;
                    },
                    rollbackType: function () {
                        return $scope.rollbackType;
                    },
                    appItem: function () {
                        return $scope.nowCiApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });

        }

        // TODO Build
        $scope.buildJob = function (jobItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciBuildModal',
                controller: 'ciBuildInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    jobEnvType: function () {
                        return $scope.jobEnvType;
                    },
                    ciType: function () {
                        return $scope.ciType;
                    },
                    appType: function () {
                        return $scope.appType;
                    },
                    rollbackType: function () {
                        return $scope.rollbackType;
                    },
                    appItem: function () {
                        return $scope.nowCiApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });

        }


        // TODO Deploy
        $scope.deployJob = function (jobItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciDeployModal',
                controller: 'ciDeployInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    jobEnvType: function () {
                        return $scope.jobEnvType;
                    },
                    ciType: function () {
                        return $scope.ciType;
                    },
                    appType: function () {
                        return $scope.appType;
                    },
                    rollbackType: function () {
                        return $scope.rollbackType;
                    },
                    appItem: function () {
                        return $scope.nowCiApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });

        }

        /**
         * 创建Jenkins任务
         * @param queryName
         */
        $scope.createJob = function (id) {
            var url = "/ci/job/create?jobId=" + id;

            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "创建成功！");
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        }


    }
);

/**
 * 任务配置
 */
app.controller('ciJobInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;
    $scope.jobItem = jobItem;
    $scope.nowBranch = "";
    $scope.jobEnvType = jobEnvType;

    $scope.templateList = [];
    $scope.nowTemplate = {};

    $scope.deployTemplateList = [];
    $scope.nowDeployTemplate = {};

    $scope.paramList = [];
    $scope.nowParam = {};


    $scope.nowParam = {
        id: 0,
        jobId: 0,
        paramName: "",
        paramValue: "",
        content: ""
    };

    var init = function () {
        if ($scope.jobItem.ciTemplateDO != null) {
            $scope.nowTemplate.selected = $scope.jobItem.ciTemplateDO;
        }

        if ($scope.jobItem.ciDeployTemplateDO != null) {
            $scope.nowDeployTemplate.selected = $scope.jobItem.ciDeployTemplateDO;
        }
    }

    init();

    $scope.setTemplate = function () {
        if ($scope.nowTemplate.selected == null) return;
        $scope.jobItem.jobTemplate = $scope.nowTemplate.selected.name;
        $scope.jobItem.jobVersion = $scope.nowTemplate.selected.version;
    }

    $scope.setDeployTemplate = function () {
        if ($scope.nowDeployTemplate.selected == null) return;
        $scope.jobItem.deployJobTemplate = $scope.nowDeployTemplate.selected.name;
        $scope.jobItem.deployJobVersion = $scope.nowDeployTemplate.selected.version;
    }

    $scope.queryTemplate = function (queryName) {
        var url = "/ci/template/page?"
            + "name=" + queryName
            + "&appType=" + $scope.appItem.appType
            + "&ciType=" + $scope.jobItem.ciType
            + "&page=0&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.templateList = body.data;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryDeployTemplate = function (queryName) {
        var url = "/ci/template/page?"
            + "name=" + queryName
            + "&appType=" + $scope.appItem.appType
            + "&ciType=" + $scope.jobItem.ciType
            + "&page=0&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.deployTemplateList = body.data;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.resetParam = function () {
        $scope.nowParam = {
            id: 0,
            jobId: 0,
            paramName: "",
            paramValue: "",
            content: ""
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


    $scope.saveJob = function () {

        var url = "/ci/job/save";

        if ($scope.jobItem.jobName == null || $scope.jobItem.jobName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "任务名称未指定!";
            return;
        }
        if ($scope.jobItem.envType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "环境类型未指定!";
            return;
        }
        if ($scope.jobItem.buildType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构建类型未指定!";
            return;
        }

        httpService.doPostWithJSON(url, $scope.jobItem).then(function (data) {
            if (data.success) {
                $scope.jobItem = data.body;

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

    //////////////////////////////////////////////////////


    $scope.saveParam = function () {
        var url = "/ci/job/param/save";

        if ($scope.nowParam.paramName == null || $scope.nowParam.paramName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "参数名称未指定!";
            return;
        }
        if ($scope.nowParam.paramValue == null || $scope.nowParam.paramValue == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "参数值未指定!";
            return;
        }
        if ($scope.jobItem.id == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "当前任务未创建!";
            return;
        } else {
            $scope.nowParam.jobId = $scope.jobItem.id;
        }

        httpService.doPostWithJSON(url, $scope.nowParam).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                $scope.jobItem = data.body.body;
                $scope.resetParam();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.editParam = function (item) {
        $scope.nowParam = item;
    }

    $scope.delParam = function (id) {
        var url = "/ci/job/param/del?"
            + "id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                $scope.jobItem = data.body.body;
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


/**
 * 应用配置
 */
app.controller('ciAppInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, ciType, appType, appItem) {

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.nowDingtalk = {};

    var init = function () {
        if ($scope.appItem.serverGroupDO != null) {
            $scope.nowServerGroup.selected = $scope.appItem.serverGroupDO;
        }
        if ($scope.appItem.dingtalkId != 0) {
            for (var i = 0; i < $scope.appItem.dingtalkList.length; i++) {
                if ($scope.appItem.dingtalkList[i].id == $scope.appItem.dingtalkId) {
                    $scope.nowDingtalk.selected = $scope.appItem.dingtalkList[i];
                    break;
                }
            }
        }
    }

    init();

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

    $scope.queryProjectName = "";

    $scope.nowGitlabProject = {};
    $scope.gitlabProjectList = [];

    $scope.queryGitlabProject = function (queryParam) {
        var url = "/gitlab/project/page?"
            + "name=" + queryParam
            + "&username="
            + "&page=0"
            + "&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.gitlabProjectList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.changeGitlabProject = function () {
        if ($scope.nowGitlabProject.selected != null) {
            $scope.appItem.projectName = $scope.nowGitlabProject.selected.name;
            $scope.appItem.sshUrl = $scope.nowGitlabProject.selected.sshUrl;
            $scope.appItem.webUrl = $scope.nowGitlabProject.selected.webUrl;
            $scope.appItem.projectId = $scope.nowGitlabProject.selected.projectId;
        }

    }

    $scope.saveApp = function () {

        var url = "/ci/app/save";

        if ($scope.appItem.appName == null || $scope.appItem.appName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "应用名称未指定!";
            return;
        }

        if ($scope.appItem.projectName == null || $scope.appItem.projectName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "仓库项目未指定!";
            return;
        }

        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "服务器组未指定!";
            return;
        } else {
            $scope.appItem.serverGroupDO = $scope.nowServerGroup.selected;
        }

        if ($scope.appItem.ciType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构建类型未指定!";
            return;
        }
        if ($scope.appItem.appType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "应用类型未指定!";
            return;
        }

        if ($scope.nowDingtalk.selected != null) {
            $scope.appItem.dingtalkId = $scope.nowDingtalk.selected.id;
        } else {
            $scope.appItem.dingtalkId = 0;
        }

        httpService.doPostWithJSON(url, $scope.appItem).then(function (data) {
            if (data.success) {
                $scope.appItem = data.body;

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

});


/**
 * 构建页面
 */
app.controller('ciBuildInstanceCtrl', function ($scope, $sce, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;
    $scope.jobItem = jobItem;
    $scope.nowBranch = "";
    $scope.jobEnvType = jobEnvType;

    $scope.paramList = [];
    $scope.nowParam = {};

    $scope.nowServerGroup = {};

    $scope.hostPatternList = {};
    $scope.nowHostPattern = {};

    $scope.isShowHostPattern = false;

    $scope.commitList = [];

    $scope.showHostPattern = function () {
        $scope.isShowHostPattern = !$scope.isShowHostPattern;
    }

    $scope.showCommit = function () {
        if (jobItem.branch == null || jobItem.branch == '') return;
        var commitHash = "";
        for (var i = 0; i < $scope.jobItem.branchList.length; i++) {
            if ($scope.jobItem.branchList[i].name == jobItem.branch) {
                commitHash = $scope.jobItem.branchList[i].commit.id;
                break;
            }
        }

        // long jobId, String jobName,String branch
        var url = "/ci/build/commits?jobId=" + $scope.jobItem.id
            + "&jobName=" + $scope.jobItem.jobName
            + "&branch=" + jobItem.branch;


        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.commitList = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    var queryHostPattern = function () {
        if ($scope.appItem.serverGroupId == null) return;

        var url = "/ci/hostPattern/get?"
            + "serverGroupId=" + $scope.appItem.serverGroupId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.hostPatternList = data.body;
                for (var i = 0; i < $scope.hostPatternList.length; i++) {
                    if ($scope.hostPatternList[i].hostPattern == $scope.jobItem.hostPattern) {
                        $scope.nowHostPattern.selected = $scope.hostPatternList[i];
                        break;
                    }
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

    var init = function () {
        $scope.nowServerGroup.selected = $scope.jobItem.ciAppVO.serverGroupDO;
        queryHostPattern();
    }

    init();

    $scope.nowParam = {
        id: 0,
        jobId: 0,
        paramName: "",
        paramValue: "",
        content: ""
    };

    $scope.resetParam = function () {
        $scope.nowParam = {
            id: 0,
            jobId: 0,
            paramName: "",
            paramValue: "",
            content: ""
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

    $scope.buildJobById = function () {


        var url = "/ci/job/build?id=" + $scope.jobItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = "success";
                $scope.alert.msg = "后台运行中!";
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.buildJob = function () {

        var url = "/ci/job/build";

        if ($scope.nowHostPattern.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "服务器分组未指定!";
            return;
        } else {
            $scope.jobItem.hostPattern = $scope.nowHostPattern.selected.hostPattern;
        }


        if ($scope.jobItem.jobName == null || $scope.jobItem.jobName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "任务名称未指定!";
            return;
        }
        if ($scope.jobItem.jobEnvType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "环境类型未指定!";
            return;
        }
        if ($scope.jobItem.buildType == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构建类型未指定!";
            return;
        }

        httpService.doPostWithJSON(url, $scope.jobItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "开始执行!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


    $scope.buildJobById = function () {
        var url = "/ci/job/buildById?id=" + $scope.jobItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = "success";
                $scope.alert.msg = "后台运行中!";
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.pageLength = 10;


    $scope.queryBuildPage = function (currentPage) {
        var url = "/ci/build/page?jobId=" + $scope.jobItem.id
            + "&page=" + (currentPage <= 0 ? 0 : currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //  $scope.refreshParamsInfo();
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.queryBuildPage(0);

    $scope.pageChanged = function (currentPage) {
        $scope.queryBuildPage(currentPage);
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});


/**
 * 部署页面
 */
app.controller('ciDeployInstanceCtrl', function ($scope, $sce, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;
    $scope.jobItem = jobItem;
    $scope.nowBranch = "";
    $scope.jobEnvType = jobEnvType;

    $scope.paramList = [];
    $scope.nowParam = {};

    $scope.nowServerGroup = {};

    $scope.hostPatternList = {};
    $scope.nowHostPattern = {};

    $scope.isShowHostPattern = false;

    $scope.commitList = [];

    $scope.showHostPattern = function () {
        $scope.isShowHostPattern = !$scope.isShowHostPattern;
    }

    $scope.showCommit = function () {
        if (jobItem.branch == null || jobItem.branch == '') return;
        var commitHash = "";
        for (var i = 0; i < $scope.jobItem.branchList.length; i++) {
            if ($scope.jobItem.branchList[i].name == jobItem.branch) {
                commitHash = $scope.jobItem.branchList[i].commit.id;
                break;
            }
        }

        var url = "/ci/build/commits?jobId=" + $scope.jobItem.id
            + "&jobName=" + $scope.jobItem.deployJobName
            + "&branch=" + jobItem.branch;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.commitList = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    var queryHostPattern = function () {
        if ($scope.appItem.serverGroupId == null) return;

        var url = "/ci/hostPattern/get?"
            + "serverGroupId=" + $scope.appItem.serverGroupId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.hostPatternList = data.body;
                for (var i = 0; i < $scope.hostPatternList.length; i++) {
                    if ($scope.hostPatternList[i].hostPattern == $scope.jobItem.hostPattern) {
                        $scope.nowHostPattern.selected = $scope.hostPatternList[i];
                        break;
                    }
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

    var init = function () {
        $scope.nowServerGroup.selected = $scope.jobItem.ciAppVO.serverGroupDO;
        queryHostPattern();
    }

    init();

    $scope.nowParam = {
        id: 0,
        jobId: 0,
        paramName: "",
        paramValue: "",
        content: ""
    };

    $scope.resetParam = function () {
        $scope.nowParam = {
            id: 0,
            jobId: 0,
            paramName: "",
            paramValue: "",
            content: ""
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


    $scope.buildArtifactList = [];
    $scope.nowArtifact = {};

    $scope.queryArtifact = function (queryNumber) {
        var url = "/ci/artifact/query?jobId=" + $scope.jobItem.id
            + "&buildNumber=" + -1;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.buildArtifactList = data.body;
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }


    $scope.deployJob = function () {

        var url = "/ci/job/deploy";

        if ($scope.nowArtifact.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构件未指定!";
            return;
        } else {
            $scope.jobItem.deployArtifactId = $scope.nowArtifact.selected.artifactList[0].id;
        }

        if ($scope.nowHostPattern.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "服务器分组未指定!";
            return;
        } else {
            $scope.jobItem.hostPattern = $scope.nowHostPattern.selected.hostPattern;
        }

        httpService.doPostWithJSON(url, $scope.jobItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "开始部署!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


    $scope.buildJobById = function () {
        var url = "/ci/job/buildById?id=" + $scope.jobItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = "success";
                $scope.alert.msg = "后台运行中!";
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;


    $scope.queryDeployPage = function (currentPage) {
        var url = "/ci/deploy/page?jobId=" + $scope.jobItem.id
            + "&page=" + (currentPage <= 0 ? 0 : currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //  $scope.refreshParamsInfo();
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    $scope.queryDeployPage(0);

    $scope.pageChanged = function (currentPage) {
        $scope.queryDeployPage(currentPage);
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});
