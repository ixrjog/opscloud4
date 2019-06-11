'use strict';


app.controller('labelCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, toaster, httpService, staticModel) {
        $scope.authPoint = $state.current.data.authPoint;

        $scope.labelList = [];

        // background-color: "

        $scope.labelActiveColor = "#EEEEEE";
        $scope.labelDefColor = "white";
        $scope.setLabel = function (labelName) {
            if ($scope.labelList != 0) {
                for (var i = 0; i < $scope.labelList.length; i++) {
                    if (labelName == $scope.labelList[i].labelName) {
                        $scope.labelList[i].active = true;
                        $scope.labelList[i].color = $scope.labelActiveColor;
                        $rootScope.$emit("callQueryMyCiApp",$scope.labelList[i].id);
                    } else {
                        $scope.labelList[i].active = false;
                        $scope.labelList[i].color = $scope.labelDefColor;
                    }
                }
            }
        }


        // active

        $scope.queryLabel = function () {
            var url = "/ci/label/query";
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.labelList = data.body;
                } else {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = data.msg;
                }
            }, function (err) {
                $scope.alert.type = 'danger';
                $scope.alert.msg = err;
            });

        }

        $scope.queryLabel();

        $scope.addLabel = function () {
            var label = {
                id: 0,
                labelName: "",
                content: "",
                labelType: 1
            }
            var modalInstance = $uibModal.open({
                templateUrl: 'addLabelModal',
                controller: 'addLabelInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    label: function () {
                        return label;
                    }
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });
        }

        $scope.editLabel = function () {
            var modalInstance = $uibModal.open({
                templateUrl: 'editLabelModal',
                controller: 'editLabelInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    labelList: function () {
                        return $scope.labelList;
                    }
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });
        }
    }
);


/**
 * 持续集成主界面
 */
app.controller('ciAppCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, toaster, httpService, staticModel) {
        $scope.authPoint = $state.current.data.authPoint;

        $scope.rollbackType = staticModel.rollbackType;
        $scope.jobEnvType = staticModel.envType;
        $scope.ciType = staticModel.ciType;
        $scope.appType = staticModel.appType;

        $scope.autoBuildType = staticModel.autoBuildType;
        $scope.ciAppList = [];

        $scope.btnDefault = {
            background: "white",
            color: "#777"
        }

        $scope.btnClick = {
            background: "#2ea8e5",
            color: "white"
        }

        var initApp = function (ciApp) {
            if ($scope.ciAppList.length != 0) {
                for (var i = 0; i < $scope.ciAppList.length; i++) {
                    if (ciApp != null && ciApp.appName == $scope.ciAppList[i].appName) {
                        $scope.ciAppList[i].btn = $scope.btnClick;
                        $scope.ciAppList[i].click = true;
                    } else {
                        $scope.ciAppList[i].btn = $scope.btnDefault;
                        $scope.ciAppList[i].click = false;
                    }
                }
            }
        }

        $rootScope.$on("callQueryMyCiApp", function (event, labelId) {
            $scope.queryMyCiApp("", labelId);
        });

        $scope.queryMyCiApp = function (queryName, labelId) {
            var url = "/ci/app/query?queryName=" + (queryName == null ? "" : queryName)
                + "&labelId=" + (labelId == null ? 0 : labelId);
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.ciAppList = data.body;
                    initApp(null);
                }
            });
        }

        $scope.setCiApp = function (ciApp) {
            initApp(ciApp);
            $rootScope.$emit("callQueryAppJob", ciApp);
        }

        $scope.queryMyCiApp("", 0);

        $scope.delApp = function (item) {
            var url = "/ci/app/del?id=" + item.id;
            httpService.doDelete(url).then(function (data) {
                if (data.success) {
                    if (data.body.success) {
                        toaster.pop("success", "删除成功！");
                        $scope.queryMyCiApp();
                    } else {
                        toaster.pop("warning", data.body.msg);
                    }
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("warning", err);
            });
        }

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
                //$scope.queryAppJob();
            }, function () {
                //$scope.queryAppJob();
            });

        }

        $scope.editUser = function (ciApp) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciAppUserModal',
                controller: 'ciAppUserInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    ciApp: function () {
                        return ciApp;
                    }
                }
            });

            modalInstance.result.then(function () {
                //$scope.doQuery();
            }, function () {
                //$scope.doQuery();
            });
        }

    }
);

app.controller('ciJobCtrl', function ($scope, $rootScope, $uibModal, $state, $sce, toaster, httpService, staticModel) {
        $scope.authPoint = $state.current.data.authPoint;

        $scope.rollbackType = staticModel.rollbackType;
        $scope.jobEnvType = staticModel.envType;
        $scope.ciType = staticModel.ciType;
        $scope.appType = staticModel.appType;

        $scope.autoBuildType = staticModel.autoBuildType;

        $scope.ciApp = {};
        $scope.ciJobList = [];

        $scope.btnDefault = {
            background: "white",
            color: "#777"
        }

        $scope.btnClick = {
            background: "#2ea8e5",
            color: "white"
        }

        // 订阅
        $rootScope.$on("callQueryAppJob", function (event, ciApp) {
            $scope.ciApp = ciApp;
            $scope.queryAppJob();
        });

        $scope.queryAppJob = function () {
            var url = "/ci/job/query?appId=" + $scope.ciApp.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.ciJobList = data.body;
                    invokeBuildDetailInfo();
                    queryAppVersion();
                }
            });
        }

        var queryAppVersion = function () {
            var url = "/ci/app/version?appId=" + $scope.ciApp.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    hc(data.body);
                    // $scope.versionData = data.body;
                }
            });
        }

        // 生成构建详情
        var invokeBuildDetailInfo = function () {
            if ($scope.ciJobList.length == 0) return;
            for (var i = 0; i < $scope.ciJobList.length; i++) {
                for (var j = 0; j < $scope.ciJobList[i].buildDetails.length; j++) {
                    var item = $scope.ciJobList[i].buildDetails[j];
                    var info = '<b>第</b><b style="color: #20a03f">' + item.buildNumber + '</b><b>次任务</b>';
                    info += '<hr style="margin-bottom: 2px; margin-top: 2px"/>';
                    info += 'PHASE:' + item.buildPhase + '<br/>'
                    if (item.buildPhase != null && item.buildPhase == 'FINALIZED') {
                        info += 'STATUS:' + '<b style="color:' + item.color + '">' + item.buildStatus + '</b>';
                    }
                    item.info = $sce.trustAsHtml(info);
                }

            }
        }

        $scope.addJob = function () {
            var jobItem = {
                id: 0,
                name: $scope.ciApp.appName,
                content: "",
                appId: $scope.ciApp.id,
                ciType: $scope.ciApp.ciType,
                branch: "",
                branchList: $scope.ciApp.branchList,
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
                    autoBuildType: function () {
                        return $scope.autoBuildType;
                    },
                    appItem: function () {
                        return $scope.ciApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                $scope.queryAppJob();
            }, function () {
                $scope.queryAppJob();
            });

        }

        // TODO Build
        $scope.buildJob = function (jobItem) {
            switch (jobItem.ciAppVO.appType) {
                case 0:
                    // java
                    buildJobByType(jobItem, 'ciBuildModal', 'ciBuildInstanceCtrl');
                    break;
                case 3:
                    // Android
                    buildJobByType(jobItem, 'ciBuildAndroidModal', 'ciBuildAndroidInstanceCtrl');
                    break;
                case 4:
                    // Test
                    buildJobByType(jobItem, 'ciBuildTestModal', 'ciBuildTestInstanceCtrl');
                    break;
                default:
                    break;
            }
        }

        var buildJobByType = function (jobItem, templateUrl, controller) {
            var modalInstance = $uibModal.open({
                templateUrl: templateUrl,
                controller: controller,
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
                        return $scope.ciApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                $scope.queryAppJob();
            }, function () {
                $scope.queryAppJob();
            });
        }


        /**
         * Deploy
         * @param jobItem
         */
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
                        return $scope.ciApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                $scope.queryAppJob();
            }, function () {
                $scope.queryAppJob();
            });

        }


        /**
         * 加固(Android)
         * @param jobItem
         */
        $scope.jiaguJob = function (jobItem) {
            var modalInstance = $uibModal.open({
                templateUrl: 'ciJiaguModal',
                controller: 'ciJiaguInstanceCtrl',
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
                        return $scope.ciApp;
                    },
                    jobItem: function () {
                        return jobItem;
                    },
                }
            });

            modalInstance.result.then(function () {
                $scope.queryAppJob();
            }, function () {
                $scope.queryAppJob();
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

        $scope.delJob = function (id) {
            var url = "/ci/job/del?id=" + id;
            httpService.doDelete(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "删除成功！");
                    $scope.queryAppJob();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("warning", err);
            });
        }


        var hc = function (versionData) {
            Highcharts.addEvent(
                Highcharts.seriesTypes.networkgraph,
                'afterSetOptions',
                function (e) {
                    var colors = Highcharts.getOptions().colors,
                        i = 0,
                        nodes = {};
                    e.options.data.forEach(function (link) {
                        if (link[0] === versionData.appName) {
                            nodes[versionData.appName] = {
                                id: versionData.appName,
                                marker: {
                                    radius: 20
                                }
                            };
                            nodes[link[1]] = {
                                id: link[1],
                                marker: {
                                    radius: 10
                                },
                                color: colors[i++]
                            };
                        } else if (nodes[link[0]] && nodes[link[0]].color) {
                            nodes[link[1]] = {
                                id: link[1],
                                color: nodes[link[0]].color
                            };
                        }
                    });
                    e.options.nodes = Object.keys(nodes).map(function (id) {
                        return nodes[id];
                    });
                }
            );

            // container
            Highcharts.chart('deployInfo', {
                chart: {
                    type: 'networkgraph',
                    height: '100%'
                },
                title: {
                    text: versionData.appName + '应用部署版本详情'
                },
                subtitle: {
                    text: '纬度:应用-环境-分组-版本-服务器'
                },
                plotOptions: {
                    networkgraph: {
                        keys: ['from', 'to'],
                        layoutAlgorithm: {
                            enableSimulation: true
                        }
                    }
                },
                series: [{
                    dataLabels: {
                        enabled: true
                    },
                    data: versionData.versionData
                }]
            });

        }

        ////////////////////////////////// 维护公告

        $scope.mdEditing = false;

        $scope.editMD = function () {
            $scope.mdEditing = true;
        }


        $scope.md = {};
        var initMD = function () {

            marked.setOptions({
                gfm: true,
                tables: true,
                breaks: false,
                pedantic: false,
                sanitize: false,
                smartLists: true,
                smartypants: false,
                highlight: function (code) {
                    return hljs.highlightAuto(code).value;
                }
            });
            var mdKey = "CI_MAINTENANCE_NOTICE";
            var url = "/readmeMD/get?mdKey=" + mdKey;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    $scope.md = data.body;
                    $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
                }
            }, function (err) {
                return {};
            });
        }
        initMD();


        $scope.aceOption = {
            useWrapMode: true,
            mode: 'markdown'
        };

        $scope.editorOptions = {
            lineWrapping: true,
            lineNumbers: true,
            readOnly: 'nocursor'
        };

        $scope.saveMD = function () {
            var url = "/readmeMD/save";
            httpService.doPostWithJSON(url, $scope.md).then(function (data) {
                if (data.success) {
                    initMD();
                } else {

                }
            }, function (err) {
            });
            $scope.mdEditing = false;
        }
    }
);


/**
 * 任务配置
 */
app.controller('ciJobInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, autoBuildType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.atAll = staticModel.atAll;

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

    $scope.autoBuildType = autoBuildType;

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
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


    $scope.alert = {
        type: "",
        msg: "",
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

    $scope.saveParams = function () {
        var url = "/ci/job/params/save";

        httpService.doPostWithJSON(url, $scope.jobItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
                //$scope.jobItem = data.body.body;
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

    ////////////////////////////// 复制配置

    $scope.nowCiApp = {};
    $scope.nowCiJob = {};
    $scope.myAppList = [];
    $scope.ciJobList = [];

    $scope.queryMyAppByType = function (queryName) {
        // 已有参数配置则不执行
        if ($scope.jobItem.paramsYaml != "") return;

        var url = "/ci//app/queryByType?"
            + "queryName=" + queryName
            + "&appType=" + $scope.appItem.appType;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.myAppList = data.body;
                //$scope.deployTemplateList = body.data;
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.setJob = function () {
        if ($scope.nowCiApp.selected == null) return;
        var url = "/ci/job/query?appId=" + $scope.nowCiApp.selected.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ciJobList = data.body;
            }
        });
    }

    $scope.setParams = function () {
        if ($scope.nowCiJob.selected == null) return;
        $scope.jobItem.paramsYaml = $scope.nowCiJob.selected.paramsYaml;
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

        /**
         * Java & Python 需要指定服务器组
         */
        if ($scope.appItem.appType == 0 || $scope.appItem.appType == 1) {
            if ($scope.nowServerGroup.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "服务器组未指定!";
                return;
            } else {
                $scope.appItem.serverGroupDO = $scope.nowServerGroup.selected;
            }
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

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
    };

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

    var setHostPattern = function () {
        // 从自定义参数查询默认分组
        var hostPattern = "";
        for (var i = 0; i < $scope.jobItem.paramList.length; i++) {
            var item = $scope.jobItem.paramList[i];
            if (item.paramName == "hostPattern") {
                hostPattern = item.paramValue;
                break;
            }
        }

        if (hostPattern == "") {
            hostPattern = $scope.jobItem.hostPattern;
        }

        for (var i = 0; i < $scope.hostPatternList.length; i++) {
            if ($scope.hostPatternList[i].hostPattern == hostPattern) {
                $scope.nowHostPattern.selected = $scope.hostPatternList[i];
                break;
            }
        }

    }

    var queryHostPattern = function () {
        if ($scope.appItem.serverGroupId == null || $scope.appItem.serverGroupId == 0) return;

        var url = "/ci/hostPattern/get?"
            + "serverGroupId=" + $scope.appItem.serverGroupId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.hostPatternList = data.body;
                setHostPattern();
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

        // 0:CI+CD 需要选择服务器分组
        if ($scope.jobItem.ciAppVO.ciType == 0) {
            if ($scope.nowHostPattern.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "服务器分组未指定!";
                return;
            } else {
                $scope.jobItem.hostPattern = $scope.nowHostPattern.selected.hostPattern;
            }
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
 * CI-ANDROID
 */
app.controller('ciBuildAndroidInstanceCtrl', function ($scope, $sce, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;
    $scope.jobItem = jobItem;
    $scope.nowBranch = "";
    $scope.jobEnvType = jobEnvType;
    $scope.androidBuildEnv = staticModel.androidBuildEnv;
    $scope.androidBuildProductFlavor = staticModel.androidBuildProductFlavor;
    $scope.paramList = [];
    $scope.nowParam = {};

    $scope.commitList = [];

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
    };

    var getCodeByName = function (instance, name) {
        for (var i = 0; i < instance.length; i++) {
            if (instance[i].name == name)
                return instance[i].code;
        }
    }

    /**
     * 初始化参数
     */
    var setDefParams = function () {
        if (jobItem.paramList.length != 0) {

            if ($scope.jobItem.androidBuild == null) {
                $scope.jobItem.androidBuild = {
                    env: "",
                    productFlavor: ""
                }
            }

            for (var i = 0; i < $scope.jobItem.paramList.length; i++) {
                // 构建环境
                if ($scope.jobItem.paramList[i].paramName == 'ENVIRONMENT_BUILD') {
                    if ($scope.jobItem.paramList[i].paramValue == '') continue;
                    //var code = getCodeByName($scope.androidBuildEnv, $scope.jobItem.paramList[i].paramValue)
                    $scope.jobItem.androidBuild.env = $scope.jobItem.paramList[i].paramValue;
                }

                // 构建渠道
                if ($scope.jobItem.paramList[i].paramName == 'PRODUCT_FLAVOR_BUILD') {
                    if ($scope.jobItem.paramList[i].paramValue == '') continue;
                    //var code = getCodeByName($scope.androidBuildProductFlavor, $scope.jobItem.paramList[i].paramValue)
                    $scope.jobItem.androidBuild.productFlavor = $scope.jobItem.paramList[i].paramValue;
                }
            }
        }

    }


    setDefParams();

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

        // 0:CI+CD 需要选择服务器分组
        if ($scope.jobItem.ciAppVO.ciType == 0) {
            if ($scope.nowHostPattern.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "服务器分组未指定!";
                return;
            } else {
                $scope.jobItem.hostPattern = $scope.nowHostPattern.selected.hostPattern;
            }
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

    $scope.checkBuildDetail = function (id) {
        var url = "/ci/build/check?buildId=" + id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                if (data.body.success) {
                    $scope.alert.type = "success";
                    $scope.alert.msg = "校验成功！";
                } else {
                    $scope.alert.type = "warning";
                    $scope.alert.msg = "校验失败！";
                }
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
 * CI-TEST
 */
app.controller('ciBuildTestInstanceCtrl', function ($scope, $sce, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
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

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
    };

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

        // 0:CI+CD 需要选择服务器分组
        if ($scope.jobItem.ciAppVO.ciType == 0) {
            if ($scope.nowHostPattern.selected == null) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "服务器分组未指定!";
                return;
            } else {
                $scope.jobItem.hostPattern = $scope.nowHostPattern.selected.hostPattern;
            }
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
 * CD 部署页面
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

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
    };

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

    $scope.queryArtifact = function (versionName) {
        var url = "/ci/artifact/queryVersion?jobId=" + $scope.jobItem.id
            + "&versionName=" + versionName;
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

/**
 * CD 加固页面
 */
app.controller('ciJiaguInstanceCtrl', function ($scope, $sce, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, ciType, appType, rollbackType, appItem, jobItem) {
    $scope.rollbackType = rollbackType;

    $scope.channelGroupList = staticModel.androidBuildProductFlavor;

    $scope.channelType = staticModel.channelType;
    $scope.nowChannelType = {
        selected: 0
    };
    $scope.channelGroup = {};

    $scope.ciType = ciType;
    $scope.appType = appType;

    $scope.appItem = appItem;
    $scope.jobItem = jobItem;
    $scope.nowBranch = "";
    $scope.jobEnvType = jobEnvType;

    $scope.paramList = [];
    $scope.nowParam = {};

    $scope.commitList = [];

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'yaml'
    };

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

    $scope.queryArtifact = function (versionName) {
        var url = "/ci/artifact/queryVersion?jobId=" + $scope.jobItem.id
            + "&versionName=" + versionName;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.buildArtifactList = data.body;
                if ($scope.nowArtifact.selected == null) {
                    $scope.nowArtifact.selected = $scope.buildArtifactList[0];
                }
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

        // 选择渠道
        if ($scope.nowChannelType.selected == 1) {
            if ($scope.channelGroup.selected == null || $scope.channelGroup.selected.length == 0) {
                $scope.alert.type = 'warning';
                $scope.alert.msg = "未选择渠道!";
                return;
            } else {
                $scope.jobItem.channelType = 1;
                $scope.jobItem.channelGroup = $scope.channelGroup.selected;
            }
        } else {
            $scope.jobItem.channelType = 0;
        }

        if ($scope.nowArtifact.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "构件未指定!";
            return;
        } else {
            $scope.jobItem.deployArtifactId = $scope.nowArtifact.selected.artifactList[0].id;
        }

        httpService.doPostWithJSON(url, $scope.jobItem).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "开始加固!";
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


/**
 * CD-AUTH 持续集成应用-授权配置
 */
app.controller('ciAppUserInstanceCtrl', function ($scope, $uibModalInstance, httpService, ciApp) {
    $scope.nowUser = {};
    $scope.userList = [];
    $scope.ciApp = ciApp;


    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        doQuery();
    };

    var doQuery = function () {
        var url = "/app/user/query?appId=" + $scope.ciApp.id;
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

    doQuery();

    $scope.queryUser = function (username) {
        var url = "/app/user/exclude?username=" + username +
            "&appId=" + $scope.ciApp.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.userList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.addUser = function () {
        if ($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        var url = "/app/user/add?&appId=" + $scope.ciApp.id +
            "&userId=" + $scope.nowUser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delUser = function (userId) {
        var url = "/app/user/del?appId=" + $scope.ciApp.id +
            "&userId=" + userId;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                doQuery();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
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

    //////////////////////////////////////////////////////


    $scope.delItem = function (username) {
        var url = "/box/user/group/del?"
            + "groupId=" + ""
            + "&username=" + username;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "删除成功!";
                init();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    /////////////////////////////////////////////////


    /////////////////////////////////////////////////


});


app.controller('addLabelInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, label) {
    $scope.label = label;

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

    $scope.addLabel = function () {

        if ($scope.label.labelName == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "标签名称未指定!";
            return;
        }

        var url = "/ci/label/add";
        httpService.doPostWithJSON(url, $scope.label).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "新建完成!";
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


app.controller('editLabelInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, labelList) {

    $scope.labelList = labelList;

    $scope.nowLabel = {};

    $scope.appList = [];
    $scope.nowApp = {};

    $scope.alert = {
        type: "",
        msg: ""
    };


    var getMember = function () {
        var url = "/ci/label/member/get?id=" + $scope.nowLabel.selected.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.nowLabel.selected.memberList = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });

    }

    $scope.closeAlert = function () {
        $scope.alert = {
            type: "",
            msg: ""
        };
    }

    $scope.queryLabelApp = function (queryName) {
        if ($scope.nowLabel.selected == null || $scope.nowLabel.selected.id == null) return;

        var url = "/ci/app/label/query?"
            + "labelId=" + $scope.nowLabel.selected.id
            + "&queryName=" + (queryName == null ? "" : queryName);
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.appList = data.body;
            }
        });
    }


    $scope.saveLabel = function () {

        if ($scope.nowLabel.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "标签未指定!";
            return;
        }

        if ($scope.nowLabel.selected.labelName == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "标签名称未指定!";
            return;
        }

        var url = "/ci/label/save";
        httpService.doPostWithJSON(url, $scope.nowLabel.selected).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存完成!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });

    }


    $scope.addMember = function () {
        if ($scope.nowLabel.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "标签未指定!";
            return;
        }

        if ($scope.nowApp.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "应用未指定!";
            return;
        }

        var member = {
            id: 0,
            labelId: $scope.nowLabel.selected.id,
            appId: $scope.nowApp.selected.id
        }

        var url = "/ci/label/member/add";
        httpService.doPostWithJSON(url, member).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "添加完成!";
                getMember();
                $scope.queryLabelApp("");
                $scope.nowApp.selected = null;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.delMember = function (id) {
        var url = "/ci/label/member/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                if (data.body.success) {
                    $scope.alert.type = 'success';
                    $scope.alert.msg = "删除成功！";
                    getMember();
                    $scope.queryLabelApp("");
                } else {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = data.msg;
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }


});