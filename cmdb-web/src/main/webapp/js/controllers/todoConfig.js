'use strict';

app.controller('todoConfigCtrl', function($scope, $uibModal, $state, toaster, httpService) {
    $scope.todoLevels = [
        {
            code : 0,
            name : "第一类目"
        },
        {
            code : 1,
            name : "第二类目"
        }
    ];

    $scope.nowParent = -1;
    $scope.configName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;
    
    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        $scope.queryConfig(currentPage);
    };
    
    /////////////////////////////////////////////////

    /**
     * 查询指定条件的配置项
     */
    $scope.queryConfig = function(currentPage) {
        var url = "/todo/config/query?"
            + "queryName=" + $scope.configName
            + "&parent=" + $scope.nowParent
            + "&valid=-1"
            + "&page=" + (currentPage == null ? 0 : (currentPage <= 0 ? 0 : (currentPage - 1)))
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
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

    $scope.queryConfig();

    /**
     * 新增配置项
     */
    $scope.addConfig = function() {
        var configItem = {
            id : 0,
            configName : "",
            parent : null,
            role : null
        }

        saveConfig(configItem);
    }

    /**
     * 更新配置项
     * @param item
     */
    $scope.editConfig = function(item) {
        var configItem = {
            id : item.id,
            configName : item.configName,
            parent : item.parentConfigDO,
            role : item.roleDO
        }

        saveConfig(configItem);
    }

    /**
     * 删除指定的配置项
     * @param id
     */
    $scope.delConfig = function(id) {
        var url = "/todo/config/del?itemId=" + id;
        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryConfig();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    var saveConfig = function(configItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'todoConfigModal',
            controller: 'todoConfigInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                configItem : function() {
                    return configItem;
                },
                todoLevels : function() {
                    return $scope.todoLevels;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryConfig();
        }, function() {
            $scope.queryConfig();
        });
    }
});

app.controller('todoConfigInstanceCtrl', function ($scope, $uibModalInstance, httpService, configItem, todoLevels) {
    $scope.item = configItem;
    $scope.todoLevels = todoLevels;

    $scope.nowLevel = -1;

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

    if($scope.item.parent == null) {
        $scope.nowParentConfig = {};

        if($scope.item.id == 0) {
            $scope.nowLevel = -1;
        } else {
            $scope.nowLevel = 0;
        }
    } else {
        $scope.nowLevel = 1;
        var parentConfig = {
            selected : $scope.item.parent
        }
        $scope.nowParentConfig = parentConfig;
    }
    $scope.configList = [];

    $scope.queryParentConfig = function(queryParam) {
        var url = "/todo/config/query?"
            + "queryName=" + queryParam
            + "&parent=" + 0
            + "&valid=0"
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.configList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////

    if($scope.item.role == null) {
        $scope.nowRole = {};
    } else {
        var role = {
            selected : $scope.item.role
        }
        $scope.nowRole = role;
    }

    $scope.nowRole = {};
    $scope.roleList = [];
    $scope.queryRole = function(queryParam) {
        var url = "/role/query?"
            + "resourceId=" + 0
            + "&roleName=" + queryParam
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.roleList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////

    $scope.changeLevel = function(nowLevel) {
        $scope.nowLevel = nowLevel;
    }

    /**
     * 保存配置项
     */
    $scope.saveData = function() {
        var url = "/todo/config/save";

        if($scope.item.configName == null || $scope.item.configName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定配置项名称";
            return;
        }

        if($scope.nowLevel == -1) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定配置项层级";
            return;
        }

        if($scope.nowLevel == 1 && $scope.nowParentConfig.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定配置项父级";
            return;
        }

        if($scope.nowLevel == 0 && $scope.nowRole.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定处理角色";
            return;
        }

        if($scope.nowLevel == 1) {
            $scope.item.parentId = $scope.nowParentConfig.selected.id;
        }

        if($scope.nowLevel == 0) {
            $scope.item.roleId = $scope.nowRole.selected.id;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.nowLevel = -1;
                $scope.nowParentConfig = {};
                $scope.item.configName = "";
                $scope.nowRole = {};
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    //////////////////////////////////////////////////////////////

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});