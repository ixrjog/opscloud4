'use strict';

app.controller('resourceCtrl', function($scope, $uibModal, toaster, httpService) {

    $scope.authType = [
        {
            code : 0,
            name : "需要"
        },
        {
            code : 1,
            name : "不需要"
        }
    ];
    $scope.nowAuthType = -1;

    $scope.resourceGroupList = [];
    $scope.resourceGroup = {};

    var initGroup = function(groupCode) {
        var url = "/resource/groups?"
            + "groupCode=" + groupCode + "&page=" + 0 + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;

                $scope.resourceGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryGroup = function(code) {
        initGroup(code);
    }

    $scope.resourceName = "";

    $scope.reSet = function() {
        $scope.resourceGroup = {};
        $scope.resourceName = "";
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 12;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function() {
        var url = "/resource/query?"
            + "groupId=" + ($scope.resourceGroup.selected == null ? 0 : $scope.resourceGroup.selected.id)
            + "&resourceName=" + $scope.resourceName
            + "&authType=" + ($scope.nowAuthType == null ? - 1 : $scope.nowAuthType)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
            var body = data.body;

            $scope.pageData = body.data;
            $scope.totalItems = body.size;
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    $scope.addItem = function() {
        var body = {
            id : 0,
            resourceName : "",
            resourceDesc : "",
            needAuth : 0,
            group : null
        }

        doSaveItem(body);
    }

    $scope.editItem = function(item) {
        var body = {
            id : item.id,
            resourceName : item.resourceName,
            resourceDesc : item.resourceDesc,
            needAuth : item.needAuth,
            group : item.groupDOList
        }

        doSaveItem(body);
    }

    var doSaveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'resource',
            controller: 'resourceInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                resource : function() {
                    return item;
                },
                authType : function() {
                    return $scope.authType;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }

    $scope.deleteItem = function(id) {
        var url = "/resource/del?resourceId=" + id;

        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});

app.controller('resourceGroupCtrl', function($scope, $uibModal, toaster, httpService) {
    $scope.groupCode = "";

    /////////////////////////////////////////////////

    $scope.pageGroupData = [];
    $scope.totalGroupItems = 0;
    $scope.currentGroupPage = 0;
    $scope.pageGroupLength = 10;

    $scope.pageGroupChanged = function(currentGroupPage) {
        $scope.currentGroupPage = currentGroupPage;
        $scope.doQueryGroup();
    };

    /////////////////////////////////////////////////

    var initGroup = function() {
        var url = "/resource/groups?"
            + "groupCode=" + $scope.groupCode
            + "&page=" + ($scope.currentGroupPage <= 0 ? 0 : $scope.currentGroupPage -  1)
            + "&length=" + $scope.pageGroupLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;

                $scope.pageGroupData = body.data;
                $scope.totalGroupItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    initGroup();

    $scope.doQueryGroup = function() {
        initGroup();
    }

    $scope.reSetGroup = function() {
        $scope.groupCode = "";
    }

    $scope.addItem = function() {
        var body = {
            id : 0,
            groupCode : "",
            groupDesc : ""
        }

        doSaveItem(body);
    }

    $scope.editItem = function(item) {
        var body = {
            id : item.id,
            groupCode : item.groupCode,
            groupDesc : item.groupDesc
        }

        doSaveItem(body);
    }

    var doSaveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'resourceGroup',
            controller: 'resourceGroupInstanceCtrl',
            size : 'sm',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                resourceGroup : function() {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            initGroup();
        }, function() {
            initGroup();
        });
    }

    $scope.delItem = function(item) {
        var url = "/resource/group/del?groupId=" + item.id;

        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功!");
                initGroup();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});

app.controller('resourceInstanceCtrl', function ($scope, $uibModalInstance, $timeout, httpService, resource, authType) {
    $scope.resource = resource;

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

    $scope.authType = authType;

    ////////////////////////////////////////////////////////

    $scope.resourceGroupList = [];

    if($scope.resource.group == null) {
        $scope.resourceGroup = {};
    } else {
        $scope.resourceGroup = {
            selected : $scope.resource.group
        };
    }

    var initGroup = function(groupCode) {
        var url = "/resource/groups?"
            + "groupCode=" + groupCode + "&page=" + 0 + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;

                $scope.resourceGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryGroup = function(code) {
        initGroup(code);
    }

    ////////////////////////////////////////////////////////

    $scope.saveData = function() {
        if($scope.resource.resourceName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "资源名称不能为空!";

            return;
        }
        if($scope.resourceGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定资源组!";

            return;
        }

        $scope.resource.groupDOList = $scope.resourceGroup.selected;

        var url = "/resource/save";
        httpService.doPostWithJSON(url, $scope.resource).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.resource = {
                    id : 0,
                    resourceName : "",
                    resourceDesc : "",
                    needAuth : 0,
                    group : null
                }
                $scope.resourceGroup.selected = null;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});


app.controller('resourceGroupInstanceCtrl', function ($scope, $uibModalInstance, httpService, resourceGroup) {
    $scope.resourceGroup = resourceGroup;

    if($scope.resourceGroup.groupCode == '') {
        $scope.canEdit = false;
    } else {
        $scope.canEdit = true;
    }

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

    ////////////////////////////////////////////////////////

    $scope.saveData = function() {
        if($scope.resourceGroup.groupCode == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "code不能为空!";

            return;
        } else if ($scope.resourceGroup.groupDesc == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "名称不能为空!";

            return;
        }

        var url = "/resource/group/save";
        httpService.doPostWithJSON(url, $scope.resourceGroup).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.resourceGroup = {
                    id : 0,
                    groupCode : "",
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

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});