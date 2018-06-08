'use strict';

app.controller('roleCtrl', function($scope, $uibModal, toaster, httpService) {

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

    /////////////////////////////////////////////////

    $scope.resourceList = [];
    $scope.roleName = "";
    $scope.resource = {};

    $scope.queryResource = function(resourceName) {
        var url = "/resource/query?"
            + "groupId=" + $scope.resourceGroup.selected.id
            + "&resourceName=" + resourceName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            var body = data.body;

            $scope.resourceList = body.data;
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.reSet = function() {
        $scope.roleName = "";
        $scope.resource = {};
    }

    $scope.doQuery = function() {
        var url = "/role/query?"
            + "resourceId=" + ($scope.resource.selected == null ? 0 : $scope.resource.selected.id)
            + "&roleName=" + $scope.roleName
            + "&page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
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

    $scope.doQuery();


    $scope.addItem = function() {
        var body = {
            id : 0,
            roleName : "",
            roleDesc : ""
        }

        doSaveItem(body);
    }

    $scope.editItem = function(item) {
        var body = {
            id : item.id,
            roleName : item.roleName,
            roleDesc : item.roleDesc
        }

        doSaveItem(body);
    }

    var doSaveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'role',
            controller: 'roleInstanceCtrl',
            size : 'sm',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                role : function() {
                    return item;
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
        var url = "/role/del?roleId=" + id;

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

app.controller('roleInstanceCtrl', function ($scope, $uibModalInstance, httpService, role) {
    $scope.role = role;

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
        if($scope.role.roleName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "角色名称不能为空!";

            return;
        }

        var url = "/role/save";
        httpService.doPostWithJSON(url, $scope.role).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.role = {
                    id : 0,
                    roleName : "",
                    roleDesc : ""
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


app.controller('roleResourceCtrl', function($scope, $uibModal, toaster, httpService) {
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

    /////////////////////////////////////////////////

    $scope.roleList = [];
    $scope.role = {};

    $scope.queryRole = function(roleCode) {
        var url = "/role/query?"
            + "resourceId=" + 0
            + "&roleName=" + roleCode
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function(data) {
            var body = data.body;

            $scope.roleList = body.data;
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////

    $scope.pageBindData = [];
    $scope.totalBindItems = 0;
    $scope.currentBindPage = 0;
    $scope.pageBindLength = 10;

    $scope.pageBindChanged = function(currentBindPage) {
        $scope.currentBindPage = currentBindPage;
        queryBind($scope.role.selected.id);
    };

    /////////////////////////////////////////////////

    /////////////////////////////////////////////////

    $scope.pageUnbindData = [];
    $scope.totalUnbindItems = 0;
    $scope.currentUnbindPage = 0;
    $scope.pageUnbindLength = 10;

    $scope.pageUnbindChanged = function(currentUnbindPage) {
        $scope.currentUnbindPage = currentUnbindPage
        queryUnBind($scope.role.selected.id);
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function() {
        if($scope.role.selected == null) {
            toaster.pop("warning", "必须选择角色!");
            return;
        }

        var roleId = $scope.role.selected.id;
        queryBind(roleId);
        queryUnBind(roleId);
    }

    var queryUnBind = function(roleId) {
        var url = "/role/resource/unbind/query?roleId=" + roleId
            + "&resourceGroupId=" + ($scope.resourceGroup.selected == null ? 0 : $scope.resourceGroup.selected.id)
            + "&page=" + ($scope.currentUnbindPage <= 0 ? 0 : $scope.currentUnbindPage - 1)
            + "&length=" + $scope.pageUnbindLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;

                $scope.pageUnbindData = body.data;
                $scope.totalUnbindItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    var queryBind = function(roleId) {
        var url = "/role/resource/bind/query?roleId=" + roleId
            + "&resourceGroupId=" + ($scope.resourceGroup.selected == null ? 0 : $scope.resourceGroup.selected.id)
            + "&page=" + ($scope.currentBindPage <= 0 ? 0 : $scope.currentBindPage - 1)
            + "&length=" + $scope.pageBindLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;

                $scope.pageBindData = body.data;
                $scope.totalBindItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.bindItem = function(item) {
        var url = "/role/resource/bind";
        var body = {
            roleId : $scope.role.selected.id,
            resourceId : item.id
        }

        httpService.doPostWithForm(url, body).then(function(data) {
            if(data.success) {
                toaster.pop("success", "绑定成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.unbindItem = function(item) {
        var url = "/role/resource/unbind";
        var body = {
            roleId : $scope.role.selected.id,
            resourceId : item.id
        }

        httpService.doPostWithForm(url, body).then(function(data) {
            if(data.success) {
                toaster.pop("success", "绑定成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});