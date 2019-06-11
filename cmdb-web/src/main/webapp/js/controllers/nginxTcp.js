'use strict';

app.controller('nginxTcpHelpCtrl', function ($scope, $state, $uibModal, $sce, toaster, httpService) {

    $scope.grafanaHelp = "";

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
        var mdKey = "NGINX_TCP_README";
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

});

app.controller('nginxTcpCtrl', function ($scope, $state, $uibModal, httpService, toaster,staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.envType = staticModel.serviceEnvType;

    $scope.btnAuto =false;
    $scope.btnScan =false;

    $scope.queryName = "";
    $scope.nowEnv = -1;

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowEnv = -1;
    }

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.autoNginxTcp = function () {
        $scope.btnAuto =true;
        var url = "/nginx/tcp/auto";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "自动配置完成！");
                $scope.btnAuto =false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnAuto =false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnAuto =false;
        });
    }

    $scope.scanNginxTcp = function () {
        $scope.btnScan =true;
        var url = "/nginx/tcp/scan";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "扫描配置完成！");
                $scope.btnScan =false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.btnScan =false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.btnScan =false;
        });
    }


    $scope.doQuery = function () {
        var url = "/nginx/tcp/page?"
            + "serviceName=" + $scope.queryName
            + "&envType=" + ($scope.nowEnv == null ? -1 : $scope.nowEnv)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    $scope.createNginxTcp = function () {
        var nginxTcp = {
            id: 0,
            serviceName: "",
            serverGroupId: 0,
            envType: 5,
            portName: "debug",
            period: 60
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'tcpCreateInfo',
            controller: 'tcpInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                nginxTcp: function () {
                    return nginxTcp;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    $scope.delNginxTcp = function (id) {
        var url = "/nginx/tcp/del?id=" + id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "销毁服务映射成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }


});

app.controller('nginxTcpDubboHelpCtrl', function ($scope, $state, $uibModal, $sce, toaster, httpService) {


    $scope.grafanaHelp = "";

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
        var mdKey = "NGINX_TCP_DUBBO_README";
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

});

app.controller('tcpInstanceCtrl', function ($scope, $uibModalInstance, httpService, staticModel, nginxTcp) {

    $scope.nginxTcpPeriod = staticModel.nginxTcpPeriod;
    $scope.envType = staticModel.serviceEnvType;
    $scope.servicePortType = staticModel.servicePortType;

    $scope.nginxTcp = nginxTcp;

    $scope.nowServerGroup = {};


    // 保存按钮
    $scope.btnSaveing = false;

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

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


    /**
     * 保存server item信息
     */
    $scope.createNginxTcp = function () {
        var url = "/nginx/tcp/create";

        if ($scope.nginxTcp.serviceName == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "未找到服务";
            return;
        }

        if ($scope.nginxTcp.period <= 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定过期时间";
            return;
        }

        $scope.btnSaveing = true;

        httpService.doPostWithJSON(url, $scope.nginxTcp).then(function (data) {
            if (data.success) {
                if(data.body.id > 0){
                    $scope.alert.type = 'success';
                    $scope.alert.msg = "服务映射成功!";
                    $scope.nginxTcp = data.body;
                }else{
                    $scope.alert.type = 'warning';
                    $scope.alert.msg = "服务映射失败！请稍后重试，若多次失败请联系@白衣";
                }
                $scope.btnSaveing = false;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
                $scope.btnSaveing = false;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
            $scope.btnSaveing = false;
        });
    }

//////////////////////////////////////////////////////////

    $scope.serverGroupList = [];

    $scope.queryServerGroup = function (queryParam) {
        var url = "/servergroup/query/page?page=0&length=10&name=" + queryParam + "&useType=0";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.serverGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doChange = function () {
        if ($scope.nowServerGroup.selected == null)
            return;

        if ($scope.nginxTcp.envType == null)
            return;

        if ($scope.nginxTcp.portName == null && $scope.nginxTcp.portName == "")
            return;

        var url = "/nginx/tcp/query?serverGroupId=" + $scope.nowServerGroup.selected.id
            + "&envType=" + $scope.nginxTcp.envType
            + "&portName=" + $scope.nginxTcp.portName;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                if (data.body == null || data.body.serviceName == "") {
                    $scope.nginxTcp.serviceName = "";
                    $scope.nginxTcp.nginxPort = null;
                    $scope.nginxTcp.content = "";
                } else {
                    $scope.nginxTcp = data.body;
                }

            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });

    }


});
