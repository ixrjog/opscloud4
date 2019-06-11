'use strict';


app.controller('jmsCtrl', function ($scope, $state, $uibModal, $sce, toaster, $localStorage, httpService) {

    $scope.btnSyncAssets = false;
    $scope.btnSyncUsers = false;

    $scope.user = $localStorage.settings.user;

    $scope.jumpserver = {};

    $scope.systemuserList = [];
    $scope.nowSystemuser = {};

    $scope.adminuserList = [];
    $scope.nowAdminuser = {};


    $scope.nowUser = {};
    $scope.userList = [];

    $scope.jmsHelp = "";

    $scope.mdEditing = false;

    $scope.editMD = function () {
        $scope.mdEditing = true;
    }

    $scope.queryUser = function (queryParam) {
        var url = "/users?username=" + queryParam + "&page=0&length=10";
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


    // 授权用户
    $scope.authAdmin = function () {
        if ($scope.nowUser == null || $scope.nowUser.selected == null)
            return;
        var url = "/jumpserver/authAdmin?userId=" + $scope.nowUser.selected.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "授权完成！");
                getJumpserver();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
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
        var mdKey = "JMS_README";
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


    $scope.getJumpserver = function () {
        var url = "/jumpserver/get"

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.jumpserver = data.body;
                if ($scope.jumpserver == null) return;
                if ($scope.jumpserver.assetsAdminuserDO != null) {
                    $scope.nowAdminuser.selected = $scope.jumpserver.assetsAdminuserDO;
                }
                if ($scope.jumpserver.assetsSystemuserDO != null) {
                    $scope.nowSystemuser.selected = $scope.jumpserver.assetsSystemuserDO;
                }
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.getJumpserver();

    $scope.querySystemuser = function (queryName) {
        var url = "/jumpserver/assetsSystemuser/query?name=" + (queryName == null ? "" : queryName);

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.systemuserList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryAdminuser = function (queryName) {
        var url = "/jumpserver/assetsAdminuser/query?name=" + (queryName == null ? "" : queryName);

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.adminuserList = data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveAdminuser = function () {
        if ($scope.nowAdminuser.selected == null) return;
        var url = "/jumpserver/assetsAdminuser/save?id=" + $scope.nowAdminuser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存管理账户成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.saveSystemuser = function () {
        if ($scope.nowSystemuser.selected == null) return;
        var url = "/jumpserver/assetsSystemuser/save?id=" + $scope.nowSystemuser.selected.id;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存系统账户成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.syncAssets = function () {
        var url = "/jumpserver/assets/sync";
        $scope.btnSyncAssets = true;


        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "资产同步成功！");
                $scope.btnSyncAssets = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncAssets = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncAssets = false;
        });
    }

    $scope.syncUsers = function () {
        var url = "/jumpserver/users/sync";
        $scope.btnSyncUsers = true;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "用户同步成功！");
                $scope.btnSyncUsers = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnSyncUsers = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnSyncUsers = false;
        });
    }


});





