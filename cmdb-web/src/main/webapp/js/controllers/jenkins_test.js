'use strict';

app.controller('jenkinsTestCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    //$scope.hookType = staticModel.hookType;
    $scope.jobEnvType = staticModel.jenkinsJobEnvType;
    $scope.buildTools = staticModel.jenkinsBuildTools;

    // 构建类型 自动化测试=3
    $scope.thisBuildType = 3;

    $scope.buildType = staticModel.jenkinsBuildType;
    $scope.nowJobEnvType = -1;
    $scope.nowBuildType = -1;
    $scope.queryJobName = "";
    /////////////////////////////////////////////////

    // 执行按钮
    $scope.butBuildSpinDisabled = false;
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
        var url = "/jenkins/jobs/page?"
            + "jobName=" + ($scope.queryJobName == null ? "" : $scope.queryJobName) + "&"
            + "jobEnvType=" + ($scope.nowJobEnvType == null ? -1 : $scope.nowJobEnvType) + "&"
            + "buildType=" + $scope.thisBuildType + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                //  $scope.refreshLeaderUserInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.nowJobEnvType = -1;
        $scope.nowBuildType = -1;
        $scope.queryJobName = "";
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////////////////

    $scope.addJob = function () {
        var jobItem = {
            id: 0,
            jobName: "",
            jobEnvType: 2,
            repositoryUrl: "",
            buildType: 3,
            content: ""
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'jenkinsJobModal',
            controller: 'jenkinsJobInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                jobEnvType: function () {
                    return $scope.jobEnvType;
                },
                buildType: function () {
                    return $scope.buildType;
                },
                buildTools: function () {
                    return $scope.buildTools;
                },
                jobItem: function () {
                    return jobItem;
                },
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.editJob = function (jobItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'jenkinsJobModal',
            controller: 'jenkinsJobInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                jobEnvType: function () {
                    return $scope.jobEnvType;
                },
                buildType: function () {
                    return $scope.buildType;
                },
                buildTools: function () {
                    return $scope.buildTools;
                },
                jobItem: function () {
                    return jobItem;
                },
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.createJob = function (item) {
        var url = "/jenkins/jobs/create?"
            + "id=" + item.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "创建成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.delJob = function (id) {
        var url = "/jenkins/jobs/del?"
            + "id=" + id;

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

    $scope.buildJob = function (item) {
        // buildType 2 = ios job
        if (item.buildType == 2) {

            var modalInstance = $uibModal.open({
                templateUrl: 'jenkinsJobBuildModal',
                controller: 'jenkinsJobBuildInstanceCtrl',
                size: 'lg',
                resolve: {
                    httpService: function () {
                        return httpService;
                    },
                    jobEnvType: function () {
                        return $scope.jobEnvType;
                    },
                    buildType: function () {
                        return $scope.buildType;
                    },
                    jobItem: function () {
                        return item;
                    },
                }
            });

            modalInstance.result.then(function () {
                $scope.doQuery();
            }, function () {
                $scope.doQuery();
            });
        } else {
            $scope.butBuildSpinDisabled = true;
            var url = "/jenkins/jobs/build?"
                + "id=" + item.id;
            httpService.doGet(url).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "执行成功!");
                    $scope.doQuery();
                    $scope.butBuildSpinDisabled = false;
                } else {
                    toaster.pop("warning", data.msg);
                    $scope.butBuildSpinDisabled = false;
                }
            }, function (err) {
                toaster.pop("error", err);
                $scope.butBuildSpinDisabled = false;
            });
        }
    }


    $scope.viewBuilds = function (jobItem) {

        var modalInstance = $uibModal.open({
            templateUrl: 'jenkinsIosJobBuildsModal',
            controller: 'jenkinsIosJobBuildsInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                jobEnvType: function () {
                    return $scope.jobEnvType;
                },
                buildType: function () {
                    return $scope.buildType;
                },
                buildTools: function () {
                    return $scope.buildTools;
                },
                jobItem: function () {
                    return jobItem;
                },
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

});

app.controller('jenkinsJobInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, $sce, httpService, jobEnvType, buildType, buildTools, jobItem) {
    $scope.jobItem = jobItem;
    $scope.jobEnvType = jobEnvType;
    $scope.buildTools = buildTools;
    $scope.buildType = buildType;
    $scope.paramList = [];
    $scope.nowParam = {};
    //ssh://git@stash.51xianqu.net:7999/kamt/kamembertest.git
    $scope.repositoryUrl = "ssh://git@stash.51xianqu.net:7999/";

    $scope.nowBuildTools = {};
    //$scope.buildTool = "";

    // branches
    $scope.refs = [];
    $scope.nowStashProject = {};
    $scope.nowStashRepository = {};
    $scope.stashProjectList = [];
    $scope.stashRepositoryList = [];
    $scope.nowBranch = {};
    $scope.jobName = "";
    $scope.nowFullJobName = "";

    // 当前选中的用户
    $scope.nowUser = {};
    // 查询接口得到的用户列表
    $scope.userList = [];
    // job邮件通知列表用户
    $scope.mailUserList = [];

    $scope.params = {
        htmlReport: "",
        junitTestResultReport: "",
        mbranch: "",
        buildTool: "",
        buildParams: "",
        buildPeriodically: "",
        pollSCM: ""
    };

    $scope.help = {};


    $scope.resetJob = function () {
        $scope.params = {
            htmlReport: "",
            junitTestResultReport: "",
            mbranch: "",
            buildTool: "",
            buildParams: "",
            buildPeriodically: "",
            pollSCM: ""
        };
        $scope.mailUserList = [];
        $scope.nowUser = {};
        $scope.refs = [];
        $scope.nowStashProject = {};
        $scope.nowStashRepository = {};
        $scope.stashProjectList = [];
        $scope.stashRepositoryList = [];
        $scope.nowBranch = {};
        $scope.jobName = "";
        $scope.nowBuildTools = {};

        $scope.jobItem = {
            id: 0,
            jobName: "",
            jobEnvType: 2,
            repositoryUrl: "",
            buildType: 3,
            content: ""
        }

        init();
    }

    // 渲染帮助项
    $scope.trustAsHtmlHelps = function () {

        var buildPeriodically = '<div style="width: 400px;"><b style="color: #286090">Build periodically</b>';
        buildPeriodically += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">周期进行项目构建</b>（源码是否发生变化没有关系）<br/>' +
            '<b style="color: #777">每15分钟构建一次 : </b>H/15 * * * * 或 */15 * * * *<br/></div>' +
            '<b style="color: #777">每天8点构建一次 : </b>0 8 * * *<br/></div>' +
            '<b style="color: #777">每天8点~17点，两小时构建一次 : </b>0 8-17/2 * * *<br/></div>' +
            '<b style="color: #777">周一到周五，8点~17点，两小时构建一次 : </b>0 8-17/2 * * 1-5<br/></div>' +
            '<b style="color: #777">每月1号、15号各构建一次，除12月 : </b>H H 1,15 1-11 *<br/></div>'
        $scope.help.buildPeriodically = $sce.trustAsHtml(
            buildPeriodically
        );

        var pollSCM = '<div style="width: 400px;"><b style="color: #286090">Poll SCM</b>';
        pollSCM += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">定时检查源码变更，如果有更新就checkout最新code下来执行构建动作。如果没有更新就不会执行构建</b> <br/>' +
            '<b style="color: #777">每15分钟构建一次 : </b>H/15 * * * * 或 */15 * * * *<br/></div>' +
            '<b style="color: #777">每天8点构建一次 : </b>0 8 * * *<br/></div>' +
            '<b style="color: #777">每天8点~17点，两小时构建一次 : </b>0 8-17/2 * * *<br/></div>' +
            '<b style="color: #777">周一到周五，8点~17点，两小时构建一次 : </b>0 8-17/2 * * 1-5<br/></div>' +
            '<b style="color: #777">每月1号、15号各构建一次，除12月 : </b>H H 1,15 1-11 *<br/></div>'

        $scope.help.pollSCM = $sce.trustAsHtml(
            pollSCM
        );


    }
    $scope.trustAsHtmlHelps();

    // 初始化数据
    var init = function () {
        if ($scope.jobItem.id == null || $scope.jobItem.id == 0) {
            $scope.params.buildPeriodically = "H 7 * * *";
            $scope.params.pollSCM = "H 18 * * *";
            return;
        }

        $scope.mailUserList = $scope.jobItem.emailUsers;
        $scope.nowFullJobName = $scope.jobItem.jobName;

        if ($scope.jobItem.params.mbranch != null) {
            var nowBranch = {
                selected: {
                    name: item.paramValue,
                    type: ""
                }
            }
            $scope.nowBranch = nowBranch;
        }

        if ($scope.jobItem.params.buildTool != null) {
            for (var i = 0; i < $scope.buildTools.length; i++) {
                var tool = $scope.buildTools[i];
                if (tool.name == item.paramValue) {
                    $scope.nowBuildTools.selected = tool.code;
                    break;
                }
            }
        }

        if ($scope.jobItem.params != null && $scope.jobItem.params.length > 0) {
            for (var i = 0; i < $scope.jobItem.params.length; i++) {
                var item = $scope.jobItem.params[i];
                switch (item.paramName) {
                    case "htmlReport":
                        $scope.params.htmlReport = item.paramValue;
                        break;
                    case "junitTestResultReport":
                        $scope.params.junitTestResultReport = item.paramValue;
                        break;
                    case "mbranch":
                        var nowBranch = {
                            selected: {
                                name: item.paramValue,
                                type: ""
                            }
                        }
                        $scope.nowBranch = nowBranch;
                        break;

                    case "buildTool":
                        for (var j = 0; j < $scope.buildTools.length; j++) {
                            var tool = $scope.buildTools[j];
                            if (tool.name == item.paramValue) {
                                $scope.nowBuildTools.selected = tool.code;
                                $scope.params.buildTool = item.paramValue;
                                break;
                            }
                        }
                        break;
                    case "buildParams":
                        $scope.params.buildParams = item.paramValue;
                        break;
                    case "buildPeriodically":
                        $scope.params.buildPeriodically = item.paramValue;
                        break;
                    case "pollSCM":
                        $scope.params.pollSCM = item.paramValue;
                        break;
                }
            }
        }

    }

    init();

    /**
     * 查询用户列表
     * @param queryParam
     */
    $scope.queryUsers = function (queryParam) {
        var url = "/safe/users?page=0&length=10&username=" + queryParam;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /**
     * 添加用户按钮
     */
    $scope.addUser = function () {
        if ($scope.nowUser.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须选择用户才能添加!";
        } else {
            $scope.alert.type = '';
        }

        if ($scope.mailUserList != 0) {
            for (var i = 0; i < $scope.mailUserList.length; i++) {
                var username = $scope.mailUserList[i].username;
                if (username === $scope.nowUser.selected.username) {
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = "重复添加!";
                    return;
                }
            }
        }
        $scope.mailUserList.push($scope.nowUser.selected);
    }

    /**
     * 删除用户按钮
     * @param user
     */
    $scope.delUser = function (user) {
        if ($scope.mailUserList != 0) {
            for (var i = 0; i < $scope.mailUserList.length; i++) {
                var username = $scope.mailUserList[i].username;
                if (username === user.username) {
                    $scope.mailUserList.splice(i, 1);
                    return;
                }
            }
        }
    }

    $scope.initRefs = function (refs) {
        if (refs == null) return;
        $scope.refs = [];
        if (refs.branches.length != 0) {
            for (var i = 0; i < refs.branches.length; i++) {
                var ref = {
                    type: "branches",
                    name: refs.branches[i]
                }
                $scope.refs.push(ref);
            }
        }
        if (refs.tags.length != 0) {
            for (var i = 0; i < refs.tags.length; i++) {
                var ref = {
                    type: "tags",
                    name: refs.tags[i]
                }
                $scope.refs.push(ref);
            }
        }
    }

    $scope.queryStashProject = function (queryParam) {
        var url = "/todo/todoNewProject/stash/project/query?page=0&length=10&name=" + queryParam;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashProjectList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

// 选择仓库
    $scope.changeStashRepository = function () {
        $scope.getStashProject();
        $scope.queryRefs();
        // 生成Jenkins完整任务名称
        $scope.setJenkinsFullJobName();
        // 设置仓库地址
        $scope.setRepositoryUrl();
    }

    $scope.getStashProject = function () {
        if ($scope.nowStashRepository.selected == null) return;
        if ($scope.nowStashProject.selected != null) return;
        var url = "/todo/todoNewProject/stash/project/get?id=" + $scope.nowStashRepository.selected.project_id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.nowStashProject.selected = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.changeStashProject = function () {
        $scope.queryStashRepository("");
        //$scope.getScmPermissions();
    }

    $scope.setJenkinsFullJobName = function () {
        var projectKey = $scope.nowStashProject.selected.project_key.toLowerCase();
        var repository = $scope.nowStashRepository.selected.name;

        if ($scope.jobName == '') {
            $scope.jobName = repository;
        }

        var envName = "";
        for (var i = 0; i < $scope.jobEnvType.length; i++) {
            var env = $scope.jobEnvType[i];
            if (env.code == $scope.jobItem.jobEnvType) {
                envName = env.name;
                break;
            }
        }

        $scope.nowFullJobName = "test_" + projectKey + "." + repository + "." + $scope.jobName + "." + envName;
        $scope.jobItem.jobName = $scope.nowFullJobName;
    }

    $scope.setRepositoryUrl = function () {
        var projectKey = $scope.nowStashProject.selected.project_key.toLowerCase();
        var repository = $scope.nowStashRepository.selected.name;
        $scope.jobItem.repositoryUrl = $scope.repositoryUrl + projectKey + "/" + repository + ".git";
    }


    $scope.queryStashRepository = function (queryParam) {

        // ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
        var url = "/todo/todoNewProject/stash/repository/query?page=0&length=10" +
            "&id=" + ($scope.nowStashProject.selected == null ? -1 : $scope.nowStashProject.selected.id) +
            "&name=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.stashRepositoryList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

// 查询分支 数据库缓存
    $scope.queryRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/query?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
                //$scope.refs = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
        });
    }

// 查询分支
    $scope.getRefs = function () {

        if ($scope.nowStashProject.selected == null) return;
        if ($scope.nowStashRepository.selected == null) return;
        $scope.doQueryRefs = true;

        var url = "/git/refs/get?"
            + "project=" + $scope.nowStashProject.selected.name + "&"
            + "repo=" + $scope.nowStashRepository.selected.name;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQueryRefs = false;
        }, function (err) {
            toaster.pop("error", err);
            $scope.doQueryRefs = false;
        });
    }


    $scope.changeBuildTools = function () {
        if ($scope.nowBuildTools.selected == 0) {
            $scope.params.buildTool = "Gradle";
        }
        if ($scope.nowBuildTools.selected == 1) {
            $scope.params.buildTool = "Maven";
        }

        if ($scope.nowStashRepository.selected == null) {
            return;
        }

        var repository = $scope.nowStashRepository.selected.name;

        if ($scope.params.buildParams != null && $scope.params.buildParams != "") return;
        if ($scope.nowBuildTools.selected == 0) {
            $scope.params.buildParams = "clean test --stacktrace --info";
            $scope.params.junitTestResultReport = repository + "-test/build/test-results/**/*.xml";
            $scope.params.htmlReport = repository + "-test/build/reports/tests/";
        }
        if ($scope.nowBuildTools.selected == 1) {
            $scope.params.buildParams = "clean surefire-report:report";
            $scope.params.junitTestResultReport = repository + "-test/target/surefire-reports/*.xml";
            $scope.params.htmlReport = repository + "-test/build/reports/tests/test/";
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
        var url = "/jenkins/jobs/save";
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

        var params = [
            {
                paramName: "htmlReport",
                paramValue: $scope.params.htmlReport
            },
            {
                paramName: "junitTestResultReport",
                paramValue: $scope.params.junitTestResultReport

            },
            {
                paramName: "mbranch",
                paramValue: $scope.nowBranch.selected.name

            },
            {
                paramName: "buildTool",
                paramValue: $scope.params.buildTool

            },
            {
                paramName: "buildParams",
                paramValue: $scope.params.buildParams

            },
            {
                paramName: "buildPeriodically",
                paramValue: $scope.params.buildPeriodically

            },
            {
                paramName: "pollSCM",
                paramValue: $scope.params.pollSCM

            }
        ]
        $scope.jobItem.params = params;

        // 添加邮件用户
        $scope.jobItem.emailUsers = $scope.mailUserList;


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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

})
;

