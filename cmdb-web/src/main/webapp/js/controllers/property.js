'use strict';

app.controller('propertyGroupCtrl', function($scope, $uibModal, toaster, httpService) {

    $scope.groupName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        $scope.queryPropertyGroup();
    };

    /////////////////////////////////////////////////

    $scope.queryPropertyGroup = function() {
        var url = "/config/property/group?"
            + "groupName=" + $scope.groupName
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
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
            toaster.pop("warning", err);
        });
    }

    $scope.queryPropertyGroup();

    ////////////////////////////////////////////////////////////

    /**
     * 新增配置项
     */
    $scope.addItem = function() {
        var propertyGroupItem = {
            id : 0,
            groupName : "",
            groupDesc : ""
        }

        saveItem(propertyGroupItem);
    }

    /**
     * 更新配置项
     * @param item
     */
    $scope.editItem = function(item) {
        var propertyGroupItem = {
            id : item.id,
            groupName : item.groupName,
            groupDesc : item.groupDesc
        }

        saveItem(propertyGroupItem);
    }

    /**
     * 删除指定属性组
     * @param id
     */
    $scope.delItem = function(id) {
        var url = "/config/property/group/del?id=" + id;
        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryPropertyGroup();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }

    var saveItem = function(propertyGroupItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'propertyGroupModal',
            controller: 'propertyGroupInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                propertyGroupItem : function() {
                    return propertyGroupItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryPropertyGroup();
        }, function() {
            $scope.queryPropertyGroup();
        });
    }
});

app.controller('propertyGroupInstanceCtrl', function ($scope, $uibModalInstance, httpService, propertyGroupItem) {
    $scope.item = propertyGroupItem;

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

    /**
     * 保存配置项
     */
    $scope.saveData = function() {
        var url = "/config/property/group/save";

        if($scope.item.groupName == null || $scope.item.groupName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定属性组名称";
            return;
        }

        httpService.doPostWithJSON(url, $scope.item).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {
                    id : 0,
                    groupName : "",
                    groupDesc : ""
                }
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



app.controller('propertyCtrl', function($scope, $uibModal, toaster, httpService) {

    $scope.propertyGroup = {};
    $scope.propertyGroupList = [];

    $scope.queryGroup = function(groupName) {
        var url = "/config/property/group?"
            + "groupName=" + groupName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.propertyGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }

    ////////////////////////////////////////////////////////////

    $scope.proName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function(currentPage) {
        $scope.currentPage = currentPage;
        $scope.queryProperty();
    };

    /////////////////////////////////////////////////

    $scope.queryProperty = function() {
        var url = "/config/property?"
            + "proName=" + $scope.proName
            + "&groupId=" + ($scope.propertyGroup.selected == null ? 0 : $scope.propertyGroup.selected.id)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
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
            toaster.pop("warning", err);
        });
    }

    $scope.queryProperty();

    /**
     * 新增配置项
     */
    $scope.addItem = function() {
        var propertyItem = {
            id : 0,
            proName : "",
            proValue : "",
            proDesc : "",
            groupDO : null
        }

        saveItem(propertyItem);
    }

    /**
     * 更新配置项
     * @param item
     */
    $scope.editItem = function(item) {
        var propertyItem = {
            id : item.id,
            proName : item.proName,
            proValue : item.proValue,
            proDesc : item.proDesc,
            groupDO : item.groupDO
        }

        saveItem(propertyItem);
    }

    /**
     * 删除指定属性组
     * @param id
     */
    $scope.delItem = function(id) {
        var url = "/config/property/del?id=" + id;
        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.queryProperty();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }

    var saveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'propertyModal',
            controller: 'propertyInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                propertyItem : function() {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryProperty();
        }, function() {
            $scope.queryProperty();
        });
    }
});

app.controller('propertyInstanceCtrl', function ($scope, $uibModalInstance, httpService, propertyItem) {
    $scope.item = propertyItem;

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

    if($scope.item.groupDO == null) {
        $scope.propertyGroup = {};
    } else {
        $scope.propertyGroup = {
            selected : $scope.item.groupDO
        };
    }

    $scope.propertyGroupList = [];

    $scope.queryGroup = function(groupName) {
        var url = "/config/property/group?"
            + "groupName=" + groupName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.propertyGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("warning", err);
        });
    }

    ////////////////////////////////////////////////////////////


    /**
     * 保存配置项
     */
    $scope.saveData = function() {
        var url = "/config/property/save";

        if($scope.propertyGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定属性组";
            return;
        }

        if($scope.item.proName == null || $scope.item.proName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定属性名称";
            return;
        }

        $scope.item.groupId = $scope.propertyGroup.selected.id;

        httpService.doPostWithJSON(url, $scope.item).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.item = {
                    id : 0,
                    proName : "",
                    proDesc : "",
                    groupDO : null
                }
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
