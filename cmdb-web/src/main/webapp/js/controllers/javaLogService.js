'use strict';

app.controller('javaLogCtrl', function ($scope, $state, $uibModal, httpService, $sce, toaster) {
    $scope.nowLogServiceServerGroupCfgVO = {};
    $scope.logServiceServerGroupCfgVOList = [];
    $scope.queryItem = {};
    $scope.serverGroupCfgInfo = "";

    $scope.isException = false;


    $scope.nowLogServicePath = {};
    $scope.logServicePathList = [];
    $scope.logServiceServerGroupCfg = {};
    /////////////////////////////////////////////////
    $scope.logServiceVO = {};
    $scope.searchStatusOpen = true;
    $scope.logHistogramsStatusOpen = true;

    $scope.javaLogHelp = {};

    // 渲染帮助项
    $scope.trustAsHtmlJavaLogHelps = function () {

        // 日志路径 tagPath
        var tagPath = '<div style="width: 400px;"><b style="color: #286090">日志路径</b>';
        tagPath += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">服务器本地日志绝对路径</b><br/>' +
            '<b style="color: #777">e.g. </b><b style="color: darkred">/data/www/logs/apps/8083-trade/app/trade.log</b><br/></div>'
        $scope.javaLogHelp.tagPath = $sce.trustAsHtml(
            tagPath
        );

        // 常用日志 history
        var history = '<div style="width: 200px;"><b style="color: #286090">常用日志</b>';
        history += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">按历史搜索热度排行</b><br/>' +
            '<b style="color: #777;">按选中日志搜索</b><br/></div>'
        $scope.javaLogHelp.history = $sce.trustAsHtml(
            history
        );

        // 搜索内容 search
        var search = '<div style="width: 400px;"><b style="color: #286090">搜索内容</b>';
        search += '<hr style="margin-bottom: 2px; margin-top: 2px" />' +
            '<b style="color: #777;">* </b>多个关键字搜索用空格分割<br/>' +
            '<b style="color: #777;">* </b>\":\"等特殊字符请使用正则\:<br/>' +
            '<b style="color: #777">e.g. </b><b style="color: darkred">Exception\\:</b><br/>' +
            '<b style="color: #777;">* </b>可在搜索内容最后加上*<br/>' +
            '<b style="color: #777">e.g. </b><b style="color: darkred">com.shandiangou*</b><br/></div>'
        $scope.javaLogHelp.search = $sce.trustAsHtml(
            search
        );
    }

    $scope.reSet = function () {
        $scope.nowLogServiceServerGroupCfgVO = {};
        $scope.nowLogServicePath = {};
        $scope.logServiceServerGroupCfg = {};
        $scope.queryServerGroup("");
        $scope.queryLogServicePath("");
        $scope.queryItem = {};
        init();
    }

    $scope.onSelected = function () {
        $scope.queryLogServicePath('');
        $scope.refreshServerGroupCfgInfo();
    }



    $scope.setException = function () {
        if($scope.isException){
            $scope.queryItem.search = 'Exception\\:';
        }else{
            $scope.queryItem.search = '';
        }
    }

    $scope.chgLogPathSelected = function () {
        if($scope.nowLogServicePath.selected != null){
            $scope.queryItem.tagPath = $scope.nowLogServicePath.selected.tagPath ;
        }
    }

    // 生成日志查询服务器组信息
    $scope.refreshServerGroupCfgInfo = function () {

        var item = $scope.nowLogServiceServerGroupCfgVO.selected;

        if (item.serverGroupId == null || item.serverGroupId == '') {
            $scope.serverGroupCfgInfo = "未配置(请联系运维)";
            return;
        }

        if (!item.authed) {
            $scope.serverGroupCfgInfo = "未授权(请申请堡垒机权限)";
        } else {
            $scope.serverGroupCfgInfo = "";
        }

        var info = '<b style="color: #286090">配置信息</b>';

        info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
        info += "project:" + '<b style="color: #286090">' + item.project + "</b><br/>";
        info += "logstore:" + '<b style="color: #286090">' + item.logstore + "</b><br/>";
        info += "topic:" + '<b style="color: #286090">' + item.topic + "</b><br/>";
        item.info = $sce.trustAsHtml(
            info);

    }


    $scope.queryServerGroup = function (queryParam) {
        var url = "/logService/servergroup/query?page=0&length=10&name=" + queryParam +
            "&isUsername=true&useType=2&page=0&length=10";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.logServiceServerGroupCfgVOList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryLogServicePath = function (queryParam) {
        var url = "/logService/java/path/query/page?page=0&length=10" +
            "&tagPath=" + queryParam +
            "&serverGroupId=" + ($scope.nowLogServiceServerGroupCfgVO.selected == null ? -1 : $scope.nowLogServiceServerGroupCfgVO.selected.serverGroupDO.id);

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.logServicePathList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    Date.prototype.Format = function (fmt) {
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
            logServiceServerGroupCfg: {},
            logServicePath: {},
            tagPath: "",
            search:
                "",
            beginDate: beginDt,
            beginTime: beginDt,
            endDate: new Date(),
            endTime: new Date(),
            queryBeginDate: "",
            queryBeginTime: "",
            queryEndDate: "",
            queryEndTime: "",
            toMinutes: 5
        }

        $scope.queryItem = queryItem;

        $scope.trustAsHtmlJavaLogHelps();
    }

    init();

/////////////////////////////////////////////////

    // $scope.today = function () {
    //     $scope.queryItem.dt = new Date();
    // };
    //
    // $scope.today();

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
        var url = "/logService/java/query";

        var queryBody = $scope.queryItem;

        queryBody.queryBeginDate = new Date($scope.queryItem.beginDate).Format("yyyy-MM-dd");
        queryBody.queryBeginTime = new Date($scope.queryItem.beginTime).Format("hh:mm:ss");

        queryBody.queryEndDate = new Date($scope.queryItem.endDate).Format("yyyy-MM-dd");
        queryBody.queryEndTime = new Date($scope.queryItem.endTime).Format("hh:mm:ss");

        // queryBody.queryDate = new Date($scope.queryItem.dt).Format("yyyy-MM-dd");
        // queryBody.queryTime = new Date($scope.queryItem.time).Format("hh:mm:ss");

        queryBody.logServiceServerGroupCfg = $scope.nowLogServiceServerGroupCfgVO.selected;

        //$scope.nowLogServicePath = {};
        // ($scope.nowLogServiceServerGroupCfgVO.selected == null ? -1 : $scope.nowLogServiceServerGroupCfgVO.selected.serverGroupDO.id);
        if ($scope.nowLogServicePath.selected != null) {
            queryBody.logServicePath = $scope.nowLogServicePath.selected;
        }

        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                $scope.logServiceVO = data.body;
                $scope.doQueryLogHistograms();
                $scope.searchStatusOpen = false;
                $scope.queryLogServicePath("");
            }
        });
    }


    $scope.javaLogList = function (logHistograms) {
        var modalInstance = $uibModal.open({
            templateUrl: 'javaLogInfo',
            controller: 'javaLogListInstanceCtrl',
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
                },
                queryItem: function () {
                    return $scope.queryItem;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.queryLogServicePath('');
        }, function () {
            $scope.queryLogServicePath('');
        });
    }


})
;


app.filter("highlight", function ($sce, $log) {
    var fn = function (text, search) {
        //$log.info("text: " + text);
        //$log.info("search: " + search);
        if (!search) {
            return $sce.trustAsHtml(text);
        }
        text = encodeURIComponent(text);
        search = encodeURIComponent(search);
        var regex = new RegExp(search, 'gi')
        var result = text.replace(regex, '<span style="color: #990000">$&</span>');
        result = decodeURIComponent(result);
        $log.info("result: " + result);
        return $sce.trustAsHtml(result);
    };
    return fn;
});


app.controller('javaLogListInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, logHistograms, queryItem) {
    $scope.logHistograms = logHistograms;
    $scope.queryItem = queryItem;
    $scope.pageData = [];

    /////////////////////////////////////////////////

    $scope.viewLog = function () {
        var url = "/logService/java/viewLog";

        var queryBody = $scope.logHistograms;

        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.refreshServerInfo();
                //$scope.refreshArgsInfo();
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


});