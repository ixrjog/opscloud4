'use strict';

app.controller('jenkinsIosCtrl', function ($scope, $state, $uibModal, $sce, $timeout, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    //$scope.hookType = staticModel.hookType;
    $scope.jobEnvType = staticModel.jenkinsJobEnvType;

    $scope.thisBuildType = 2;

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
            jobEnvType: -1,
            repositoryUrl: "",
            buildType: 2,
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

app.controller('jenkinsJobInstanceCtrl', function ($scope, $uibModalInstance, toaster, staticModel, httpService, jobEnvType, buildType, jobItem) {
    $scope.jobItem = jobItem;
    $scope.jobEnvType = jobEnvType;
    $scope.buildType = buildType;
    $scope.paramList = [];
    $scope.nowParam = {};


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

    $scope.resetJob = function () {
        $scope.jobItem = {
            id: 0,
            jobName: "",
            jobEnvType: -1,
            repositoryUrl: "",
            buildType: -1,
            content: ""
        }
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

    $scope.queryParams = function () {
        if ($scope.jobItem.id == 0) return;

        var url = "/jenkins/jobs/params/query?"
            + "jobId=" + $scope.jobItem.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.paramList = data.body;
                //$scope.totalItems = body.size;
                //$scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryParams();


    $scope.saveParam = function () {
        var url = "/jenkins/jobs/params/save";

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
                $scope.resetParam();
                $scope.queryParams();
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
        var url = "/jenkins/jobs/params/del?"
            + "id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryParams();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }

});

app.controller('jenkinsJobBuildInstanceCtrl', function ($scope, $uibModalInstance, toaster, httpService, jobEnvType, jobItem) {
    $scope.jobItem = jobItem;
    $scope.jobEnvType = jobEnvType;
    $scope.refs = [];
    // $scope.listRefsSpinDisabled = true;
    $scope.doQueryRefs = true;

    $scope.showConfigRefsMenu = false;
    $scope.nowBranches = "";
    $scope.nowTags = "";

    $scope.runBuilding = false;
    //$scope.butBuildSpinDisabled = false;
    $scope.nowMbranch = {};
    // iOS默认构建环境daily
    $scope.nowEnvType = {};


    $scope.showConfigRefs = function () {
        $scope.showConfigRefsMenu = !$scope.showConfigRefsMenu;
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

    $scope.resetJob = function () {
        $scope.jobItem = {
            id: 0,
            jobName: "",
            jobEnvType: -1,
            repositoryUrl: "",
            buildType: -1,
            content: ""
        }
    }


    //////////////////////////////////////////////////////


    $scope.chgRefs = function (ref, type) {
        var url = "/jenkins/job/refs/change?"
            + "id=" + $scope.jobItem.id
            + "&ref=" + ref
            + "&type=" + type;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.initRefs(data.body);
                toaster.pop("success", "变更成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    // 查询分支 数据库缓存
    $scope.queryRefs = function () {
        $scope.doQueryRefs = true;

        var url = "/jenkins/job/refs/query?"
            + "id=" + $scope.jobItem.id;

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

    $scope.queryRefs();

    // 获取最新分支
    $scope.getRefs = function () {
        $scope.doQueryRefs = true;
        var url = "/jenkins/job/refs/get?"
            + "id=" + $scope.jobItem.id;

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

    $scope.buildJob = function () {

        if ($scope.nowMbranch.selected == null || $scope.nowMbranch.selected.name == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定分支";
            return;
        }

        if ($scope.nowEnvType.selected == null || $scope.nowEnvType.selected == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定环境";
            return;
        }


        $scope.closeAlert();
        $scope.runBuilding = true;
        var url = "/jenkins/jobs/ios/build?"
            + "id=" + $scope.jobItem.id
            + "&mbranch=" + $scope.nowMbranch.selected.name
            + "&buildType=" + $scope.nowEnvType.selected;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                //toaster.pop("success", "执行成功!");
                $scope.alert.type = 'success';
                $scope.alert.msg = "执行成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                //toaster.pop("warning", data.msg);

            }
            $scope.runBuilding = false;
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
            $scope.runBuilding = false;
        });


    }

});

/**
 * 任务详情
 */
app.controller('jenkinsIosJobBuildsInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, jobEnvType, jobItem) {

    $scope.jobItem = jobItem;
    $scope.jobEnvType = jobEnvType;

    $scope.butBuildSpinDisabled = false;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;


    // $("#code").qrcode({
    //     render: "table", //table方式
    //     width: 200, //宽度
    //     height:200, //高度
    //     text: "www.helloweba.net" //任意内容
    // });


    // 生成参数
    $scope.refreshParamsInfo = function () {

        if ($scope.pageData.length == 0) return;

        for (var i = 0; i < $scope.pageData.length; i++) {
            var info = '<b style="color: #286090">执行参数</b>';
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
            var item = $scope.pageData[i];
            //var params = $scope.pageData[i].paramList;
            if (item.paramList.length == 0) return;
            for (var j = 0; j < item.paramList.length; j++) {
                var param = item.paramList[j];
                info += '<b style="color: red">' + param.paramName + "</b>:";
                info += '<b style="color: green">' + param.paramValue + "</b> <br/>";
            }
            item.paramsInfo = $sce.trustAsHtml(
                info
            );
        }

    }

    $scope.pageChanged = function (currentPage) {
        $scope.currentPage = currentPage;
        $scope.doQuery();
    };

    // 判断任务是否结束
    $scope.checkBuildCompleted = function (jobNotes) {

        if (jobNotes == null || jobNotes.length == 0) return true;
        for (var i = 0; i < jobNotes.length; i++) {
            var item = jobNotes[i];
            if (item.buildPhase == "COMPLETED") return false;
        }
        return true;
    }


    $scope.doQuery = function () {
        var url = "/jenkins/job/builds/page?"
            + "jobName=" + $scope.jobItem.jobName + "&"
            + "buildNumber=0&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1)) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshParamsInfo();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    $scope.rebuildJob = function (id) {
        $scope.butBuildSpinDisabled = true;
        var url = "/jenkins/jobs/rebuild?"
            + "id=" + id;
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
});