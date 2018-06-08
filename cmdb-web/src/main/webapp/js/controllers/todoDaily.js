'use strict';

app.controller('todoDailyCtrl', function($scope, $uibModal, $state, toaster, httpService) {
    $scope.level1List = [];
    $scope.level2List = [];

    $scope.queryDO = {
        levelOne : -1,
        levelTwo : -1,
        sponsor : "all",
        nowUser : $scope.app.settings.user.username,
        privacy : -1,
        urgents : -1,
        todoStatus : -1
    }

    var getLevel1 = function() {
        var url = "/todo/config/query?queryName=&parent=0&valid=0&page=0&length=100";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level1List = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    getLevel1();

    $scope.changeLevel1 = function() {
        if($scope.queryDO.levelOne == null) {
            $scope.queryDO.levelOne = -1;
            return;
        }
        var url = "/todo/config/children/query?parentId=" + $scope.queryDO.levelOne;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level2List = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////////

    $scope.typeList = [
        {
            code : 0,
            name : "公开"
        },
        {
            code : 1,
            name : "私密"
        }
    ];

    $scope.urgentList = [
        {
            code : 0,
            name : "一般"
        },
        {
            code : 1,
            name : "重要"
        },
        {
            code : 2,
            name : "紧急"
        }
    ];

    $scope.userTypeList = [
        {
            code : "all",
            name : "所有"
        },
        {
            code : "now",
            name : "仅本人"
        }
    ];

    $scope.todoStatusList = [
        {
            code : 0,
            name : "处理中"
        },
        {
            code : 1,
            name : "完成"
        },
        {
            code : 2,
            name : "待反馈"
        }
    ];

    //////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        $scope.queryTodo();
    };

    /////////////////////////////////////////////////

    $scope.queryTodo = function() {
        var url = "/todo/daily/query?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doPostWithJSON(url, $scope.queryDO).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
    $scope.queryTodo();

    //////////////////////////////////////////////////////////////

    /**
     * 新增工单
     */
    $scope.addItem = function() {
        var todoItem = {
            id : 0,
            levelOne : -1,
            levelTwo : -1,
            sponsor : $scope.app.settings.user.username,
            privacy : 0,
            urgents : 0,
            contents : "",
            feedbackContent : ""
        }

        saveConfig(todoItem);
    }

    /**
     * 编辑工单
     * @param item
     */
    $scope.editItem = function(item) {
        var todoItem = {
            id : item.id,
            levelOne : item.levelOne.id,
            levelTwo : item.levelTwo,
            sponsor : $scope.app.settings.user.username,
            privacy : item.privacy,
            urgents : item.urgents,
            contents : item.contents,
            feedbackContent : item.feedbackContent
        }

        saveConfig(todoItem);
    }

    var saveConfig = function(todoItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'todoDailyModal',
            controller: 'todoDailyInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                todoItem : function() {
                    return todoItem;
                },
                typeList : function() {
                    return $scope.typeList;
                },
                urgentList : function() {
                    return $scope.urgentList;
                },
                todoType : function() {
                    return "daily";
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryTodo();
        }, function() {
            $scope.queryTodo();
        });
    }

    //////////////////////////////////////////////////////////////

    $scope.reSet = function() {
        $scope.queryDO = {
            levelOne : -1,
            levelTwo : -1,
            sponsor : "all",
            nowUser : $scope.app.settings.user.username,
            privacy : -1,
            urgents : -1,
            todoStatus : -1
        }
    }
});

app.controller('todoDailyPendProcessCtrl', function($scope, $uibModal, $state, toaster, httpService) {
    $scope.level1List = [];
    $scope.level2List = [];

    $scope.queryDO = {
        levelOne : -1,
        levelTwo : -1,
        nowUser : $scope.app.settings.user.username,
        privacy : -1,
        urgents : -1,
        todoStatus : -1
    }

    var getLevel1 = function() {
        var url = "/todo/config/query?queryName=&parent=0&valid=0&page=0&length=100";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level1List = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    getLevel1();

    $scope.changeLevel1 = function() {
        if($scope.queryDO.levelOne == null) {
            $scope.queryDO.levelOne = -1;
            return;
        }
        var url = "/todo/config/children/query?parentId=" + $scope.queryDO.levelOne;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level2List = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////////

    $scope.typeList = [
        {
            code : 0,
            name : "公开"
        },
        {
            code : 1,
            name : "私密"
        }
    ];

    $scope.urgentList = [
        {
            code : 0,
            name : "一般"
        },
        {
            code : 1,
            name : "重要"
        },
        {
            code : 2,
            name : "紧急"
        }
    ];

    $scope.todoStatusList = [
        {
            code : 0,
            name : "处理中"
        },
        {
            code : 1,
            name : "完成"
        },
        {
            code : 2,
            name : "待反馈"
        }
    ];

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        $scope.queryTodo();
    };

    /////////////////////////////////////////////////

    $scope.queryTodo = function() {
        var url = "/todo/process/query?"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doPostWithJSON(url, $scope.queryDO).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
    $scope.queryTodo();

    //////////////////////////////////////////////////////////////

    /**
     * 编辑工单
     * @param item
     */
    $scope.editItem = function(item) {
        var todoItem = {
            id : item.id,
            levelOne : item.levelOne.id,
            levelTwo : item.levelTwo,
            sponsor : item.sponsor,
            privacy : item.privacy,
            urgents : item.urgents,
            contents : item.contents,
            feedbackContent : item.feedbackContent
        }

        saveConfig(todoItem, "process");
    }

    $scope.processFinish = function(item) {
        var todoItem = {
            id : item.id,
            levelOne : item.levelOne.id,
            levelTwo : item.levelTwo,
            sponsor : item.sponsor,
            privacy : item.privacy,
            urgents : item.urgents,
            contents : item.contents,
            feedbackContent : "处理完成!"
        }

        saveConfig(todoItem, "finish");
    }

    var saveConfig = function(todoItem, todoType) {
        var modalInstance = $uibModal.open({
            templateUrl: 'todoDailyModal',
            controller: 'todoDailyInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                todoItem : function() {
                    return todoItem;
                },
                typeList : function() {
                    return $scope.typeList;
                },
                urgentList : function() {
                    return $scope.urgentList;
                },
                todoType : function() {
                    return todoType;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryTodo();
        }, function() {
            $scope.queryTodo();
        });
    }

    //////////////////////////////////////////////////////////////
});

app.controller('todoDailyInstanceCtrl', function ($scope, $uibModalInstance, httpService, todoItem, typeList, urgentList, todoType) {
    $scope.item = todoItem;
    $scope.typeList = typeList;
    $scope.urgentList = urgentList;
    $scope.todoType = todoType;

    $scope.alert = {
        type : "",
        msg : ""
    };

    $scope.closeAlert = function() {
        $scope.alert = {
            type : "",
            msg : ""
        };
    }

    //////////////////////////////////////////////////////////////

    $scope.level1List = [];
    $scope.level2List = [];

    var getLevel1 = function() {
        var url = "/todo/config/query?queryName=&parent=0&valid=0&page=0&length=100";

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level1List = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    getLevel1();

    $scope.changeLevel1 = function(nowLevel1) {
        $scope.item.levelOne = nowLevel1;
        if($scope.item.levelOne == null) {
            $scope.item.levelOne = -1;
            return;
        }
        var url = "/todo/config/children/query?parentId=" + $scope.item.levelOne;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.level2List = body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////

    var disabledRepeat = false;

    /**
     * 保存配置项
     */
    $scope.saveData = function() {
        if(!disabledRepeat) {
            disabledRepeat = true;
        } else {
            return;
        }
        var url = "/todo/daily/save";

        if($scope.item.levelOne == null || $scope.item.levelOne == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定工单主类别";
            return;
        }

        if($scope.item.levelTwo == null || $scope.item.levelTwo == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定工单具体分类";
            return;
        }
        if($scope.item.id != 0) {
            $scope.item.levelTwo = $scope.item.levelTwo.id;
        }

        if($scope.todoType == 'process') {
            $scope.item.todoStatus = 2;
        } else if ($scope.todoType == 'finish') {
            $scope.item.todoStatus = 1;
        } else {
            $scope.item.todoStatus = 0;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {
                    id : 0,
                    levelOne : -1,
                    levelTwo : -1,
                    privacy : 0,
                    urgents : 0,
                    contents : ""
                }
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
            disabledRepeat = false;
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
            disabledRepeat = false;
        });
    }

    //////////////////////////////////////////////////////////////

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});