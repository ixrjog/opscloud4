'use strict';

app.controller('nginxLogCtrl', function ($scope, $state, $uibModal, httpService, toaster) {
    //   $scope.authPoint = $state.current.data.authPoint;

    $scope.logServiceCfgList = [];
    $scope.nowLogServiceCfg = {};
    $scope.logServiceVO = {};
    $scope.searchStatusOpen = true;
    $scope.logHistogramsStatusOpen = true;

    $scope.queryItem = {};
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    var init = function () {
        var beginDt = new Date();
        beginDt.setHours(beginDt.getHours() -1);

        var queryItem = {
            logServiceCfg: {},
            uri: "",
            args: "",
            status: 200,
            sourceIp: "",
            mobile: "",
            requestTime: "",
            upstreamResponseTime: "",
            beginDate: beginDt,
            beginTime: beginDt,
            endDate: new Date(),
            endTime: new Date(),
            queryBeginDate: "",
            queryBeginTime: "",
            queryEndDate: "",
            queryEndTime: "",
            //toMinutes: 5
        }

        $scope.queryItem = queryItem;
    }

    init();

    $scope.queryLogServiceCfg = function (queryParam) {
        var url = "/logService/nginx/cfg/page?page=0&length=10&serverName=" + queryParam;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.logServiceCfgList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /////////////////////////////////////////////////

    // $scope.today = function () {
    //     $scope.queryItem.beginDate = new Date();
    // };

    //$scope.today();

    $scope.popupBeginDate = {
        opened: false
    };

    $scope.openBeginDate = function () {
        $scope.popupBeginDate.opened = true;
    };

    $scope.popupEndDate = {
        opened: false
    };

    $scope.openEndDate = function () {
        $scope.popupEndDate.opened = true;
    };

    // $scope.setDate = function (year, month, day) {
    //     $scope.dt = new Date(year, month, day);
    // };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['M!/d!/yyyy'];
    ////////////////////////////////////////
    $scope.hstep = 1;
    $scope.mstep = 5;
    $scope.queryToMinutes = 5;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function (currentPage) {
        $scope.currentPage = currentPage;
        $scope.doQueryLogHistograms();
    };

    /////////////////////////////////////////////////

    /**
     * 查询日志分布视图详情页
     */
    $scope.doQueryLogHistograms = function () {
        var url = "/logService/logHistograms/page?"
            + "logServiceId=" + ($scope.logServiceVO == null ? -1 : $scope.logServiceVO.id)
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

    ///////////////////////////////////////////////////////////


    /**
     * 查询日志分布视图
     */
    $scope.doQuery = function () {
        var url = "/logService/nginx/query";

        var queryBody = $scope.queryItem;

        queryBody.queryBeginDate = new Date($scope.queryItem.beginDate).Format("yyyy-MM-dd");
        queryBody.queryBeginTime = new Date($scope.queryItem.beginTime).Format("hh:mm:ss");

        queryBody.queryEndDate = new Date($scope.queryItem.endDate).Format("yyyy-MM-dd");
        queryBody.queryEndTime = new Date($scope.queryItem.endTime).Format("hh:mm:ss");

        queryBody.logServiceCfg = $scope.nowLogServiceCfg.selected;

        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                $scope.logServiceVO = data.body;
                $scope.doQueryLogHistograms();
                $scope.searchStatusOpen = false;
            } else {

            }
        });
    }


    $scope.nginxLogList = function (logHistograms) {
        var modalInstance = $uibModal.open({
            templateUrl: 'nginxLogInfo',
            controller: 'nginxLogListInstanceCtrl',
            size: 'lg',
            resolve: {
                toaster: function () {
                    return toaster;
                },
                httpService: function () {
                    return httpService;
                },
                logHistograms: function () {
                    return logHistograms;
                }
            }
        });
    }


});


app.controller('nginxLogListInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, logHistograms) {
    $scope.logHistograms = logHistograms;
    $scope.pageData = [];

    /////////////////////////////////////////////////

    $scope.viewLog = function () {
        var url = "/logService/nginx/viewLog";

        var queryBody = $scope.logHistograms;

        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.refreshServerInfo();
                $scope.refreshArgsInfo();
            }
        });
    }

    $scope.viewLog();


    // 生成后端服务器信息
    $scope.refreshServerInfo = function () {
        if ($scope.pageData.length == 0) return;
        for (var i = 0; i < $scope.pageData.length; i++) {
            if ($scope.pageData[i].serverDO == null) return;

            var info = '<b style="color: #286090">服务器信息</b>';
            var item = $scope.pageData[i].serverDO;
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
            var publicIp = "";
            if (item.publicIp != null && item.publicIp != '') {
                publicIp = item.publicIp;
            }
            var envType = "";
            switch (item.envType) {
                case 1:
                    envType = "<b style=\"color: #5bc0de\">dev</b>";
                    break;
                case 2:
                    envType = "<b style=\"color: #449d44\">daily</b>";
                    break;
                case 3:
                    envType = "<b style=\"color: #ec971f\">gray</b>";
                    break;
                case 4:
                    envType = "<b style=\"color: #d9534f\">prod</b>";
                    break;
                case 5:
                    envType = "<b style=\"color: #5e5e5e\">test</b>";
                    break;
                case 6:
                    envType = "<b style=\"color: #286090\">back</b>";
                    break;
                default:
            }

            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
                "名称:" + item.serverName + "-" + item.serialNumber + "<br/>" +
                "公网:" + publicIp + "<br/>" +
                "内网:" + item.insideIp + "<br/>" +
                "环境:" + envType;

            $scope.pageData[i].serverInfo = $sce.trustAsHtml(
                info
            );
        }
    }


    // 生成后端服务器信息
    $scope.refreshArgsInfo = function () {
        if ($scope.pageData.length == 0) return;
        for (var i = 0; i < $scope.pageData.length; i++) {
            if ($scope.pageData[i].args == '-') continue;

            var argsList = $scope.pageData[i].argsList;
            //if ($scope.pageData[i].argsList.length == 0) continue;
            var info = '<b style="color: #286090">参数信息</b>';
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
            for (var j = 0; j < argsList.length; j++) {
                var arg = $scope.pageData[i].argsList[j];
                info += arg + "<br/>";
            }
            $scope.pageData[i].argsInfo = $sce.trustAsHtml(
                info
            );
        }
    }

});