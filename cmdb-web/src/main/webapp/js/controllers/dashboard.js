'use strict';

app.controller('dashboardCtrl', function ($scope, $state, $uibModal, $timeout, $sce, staticModel, httpService) {
    $scope.logServiceStatusVO = {};
    $scope.keyboxStatusVO = {};
    $scope.ciStatusVO = {};
    $scope.serverStatusVO = {}
    //$scope.topProjectData = [];

    // Radialize the colors
    Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function (color) {
        return {
            radialGradient: {
                cx: 0.5,
                cy: 0.3,
                r: 0.7
            },
            stops: [
                [0, color],
                [1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
            ]
        };
    });

    // var _lastGoodResult = '';
    // $scope.toPrettyJSON = function (objStr, tabWidth) {
    //     try {
    //         var obj = $parse(objStr)({});
    //     } catch (e) {
    //         // eat $parse error
    //         return _lastGoodResult;
    //     }
    //
    //     var result = JSON.stringify(obj, null, Number(tabWidth));
    //     _lastGoodResult = result;
    //
    //     return result;
    // };

    // 生成部署详情
    $scope.refreshCiDeployInfo = function () {

        if ($scope.ciStatusVO.ciDeployList.length == 0) return;

        for (var i = 0; i < $scope.ciStatusVO.ciDeployList.length; i++) {
            var item = $scope.ciStatusVO.ciDeployList[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
            var info = '<b style="color: #286090">部署详情</b>';
            info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
            info += '<b style="color: #d75f00">' + item.projectName + "</b><br/>";
            info += '<b style="color: #777">' + item.version + "</b><br/>";
            info += '<b style="color: #286090">' + item.groupName + "</b><br/>";

            if (item.servers.length != 0) {
                info += '<hr style="margin-bottom: 2px; margin-top: 2px" />';
                for (var j = 0; j < item.servers.length; j++) {
                    var server = item.servers[j];
                    info += server.serverName + "-" + server.serialNumber + " " + server.insideIp + "<br/>";
                }
            }

            item.deployInfo = $sce.trustAsHtml(
                info
            );
        }
    }

    $scope.initCiTopProjectData = function () {
        //$scope.ciStatusVO.topProjectList
        var item = $scope.ciStatusVO.topProjectList;
        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            member = [item[i].projectName, item[i].cnt];
            data.push(member);
        }

        $scope.topProjectData = data;

        Highcharts.chart('ciTopProject', {
            credits: {enabled: false},
            chart: {
                type: 'column',
                // 横向图表
                inverted: true
            },
            title: {
                text: '活跃项目 <span style="color: blue">Top20</span>'
            },
            subtitle: {
                // text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
                //text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
            },
            xAxis: {
                type: 'category',
                labels: {
                    // 字段名角度 -45
                    rotation: 0,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '累计部署'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '部署次数: <b>{point.y}</b>'
            },
            series: [{
                name: '累计部署',
                data:
                data
                ,
                dataLabels: {
                    enabled: true,
                    rotation: 0,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y}', // one decimal
                    y: 0, // 10 pixels down from the top
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        });
    }

    $scope.initCiTopUserData = function () {
        //$scope.ciStatusVO.topProjectList
        var item = $scope.ciStatusVO.topUserList;
        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            member = [item[i].username, item[i].cnt];
            data.push(member);
        }

        $scope.topProjectData = data;

        Highcharts.chart('ciTopUser', {
            credits: {enabled: false},
            chart: {
                type: 'column',
                // 横向图表
                inverted: true
            },
            title: {
                text: '活跃用户 <span style="color: blue">Top20</span>'
            },
            subtitle: {
                // text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
                //text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
            },
            xAxis: {
                type: 'category',
                labels: {
                    // 字段名角度 -45
                    rotation: 0,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '累计部署'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '部署次数: <b>{point.y}</b>'
            },
            series: [{
                name: '累计部署',
                data:
                data
                ,
                dataLabels: {
                    enabled: true,
                    rotation: 0,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y}', // one decimal
                    y: 0, // 10 pixels down from the top
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        });
    }

    $scope.initKeyboxTopUserData = function () {
        //$scope.ciStatusVO.topProjectList
        var item = $scope.keyboxStatusVO.topUserList;
        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            member = [item[i].username, item[i].cnt];
            data.push(member);
        }

        $scope.topProjectData = data;

        Highcharts.chart('keyboxTopUser', {
            credits: {enabled: false},
            chart: {
                type: 'column',
                // 横向图表
                inverted: true
            },
            title: {
                text: '活跃用户 <span style="color: blue">Top10</span>'
            },
            subtitle: {
                // text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
                //text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
            },
            xAxis: {
                type: 'category',
                labels: {
                    // 字段名角度 -45
                    rotation: 0,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '累计登陆'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '累计登陆: <b>{point.y}</b>'
            },
            series: [{
                name: '累计登陆',
                data:
                data
                ,
                dataLabels: {
                    enabled: true,
                    rotation: 0,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y}', // one decimal
                    y: 0, // 10 pixels down from the top
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        });
    }

    $scope.initLogServiceTopUserData = function () {
        var item = $scope.logServiceStatusVO.topUserList;
        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            member = [item[i].username, item[i].cnt];
            data.push(member);
        }

        Highcharts.chart('lsTopUser', {
            credits: {enabled: false},
            chart: {
                type: 'column',
                // 横向图表
                inverted: true
            },
            title: {
                text: '活跃用户 <span style="color: blue">Top10</span>'
            },
            subtitle: {
                // text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
                //text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
            },
            xAxis: {
                type: 'category',
                labels: {
                    // 字段名角度 -45
                    rotation: 0,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '累计查询'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '累计查询: <b>{point.y}</b>'
            },
            series: [{
                name: '累计查询',
                data:
                data
                ,
                dataLabels: {
                    enabled: true,
                    rotation: 0,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y}', // one decimal
                    y: 0, // 10 pixels down from the top
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        });
    }

    $scope.initLogServiceTopProjectData = function () {
        var item = $scope.logServiceStatusVO.topGroupList;
        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            member = [item[i].groupName, item[i].cnt];
            data.push(member);
        }

        Highcharts.chart('lsTopProject', {
            credits: {enabled: false},
            chart: {
                type: 'column',
                // 横向图表
                inverted: true
            },
            title: {
                text: '活跃项目 <span style="color: blue">Top10</span>'
            },
            subtitle: {
                // text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
                //text: 'Source: <a href="http://en.wikipedia.org/wiki/List_of_cities_proper_by_population">Wikipedia</a>'
            },
            xAxis: {
                type: 'category',
                labels: {
                    // 字段名角度 -45
                    rotation: 0,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '累计查询'
                }
            },
            legend: {
                enabled: false
            },
            tooltip: {
                pointFormat: '累计查询: <b>{point.y}</b>'
            },
            series: [{
                name: '累计查询',
                data:
                data
                ,
                dataLabels: {
                    enabled: true,
                    rotation: 0,
                    color: '#FFFFFF',
                    align: 'right',
                    format: '{point.y}', // one decimal
                    y: 0, // 10 pixels down from the top
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        });
    }

    $scope.initLogServiceStatusData = function () {
        var item = $scope.logServiceStatusVO.cntMap;
        if (item.length == 0) return;

        Highcharts.chart('lgStatus', {
            credits: {enabled: false},
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: 0,
                plotShadow: false
            },
            title: {
                text: '月汇总',
                align: 'center',
                verticalAlign: 'middle',
                y: 40
            },
            plotArea: {
                shadow: null,
                borderWidth: null,
                backgroundColor: null
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage, 1) + '% (' +
                        Highcharts.numberFormat(this.y, 0, ',') + ' 次)';
                }
            },
            //tooltip: {
            //    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            //},
            plotOptions: {
                pie: {
                    dataLabels: {
                        enabled: true,
                        color: '#FFFFFF',
                        distance: -50,
                        style: {
                            fontWeight: 'bold',
                            color: 'white'
                        }
                    },
                    startAngle: -90,
                    endAngle: 90,
                    center: ['50%', '75%']
                }
            },
            series: [{
                type: 'pie',
                name: '占比',
                innerSize: '50%',
                data: [
                    ['Today', item.day],
                    ['Week', item.week],
                    ['Month', item.month]
                ]
            }]
        });


    }

    $scope.initServerEnvTypeData = function () {

        var item = $scope.serverStatusVO.serverEnvTypeList;

        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            if (item[i].envType == 4) {
                member = [item[i].name, item[i].cnt, true, true];
            } else {
                member = [item[i].name, item[i].cnt, false];
            }
            data.push(member);
        }


        Highcharts.chart('serverEnvType', {
            credits: {enabled: false},
            title: {
                text: '按环境统计'
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y}' + '(台)'
                    }
                }
            },
            tooltip: {
                formatter: function () {
                    return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage, 1) + '% (' +
                        Highcharts.numberFormat(this.y, 0, ',') + ' 台)';
                }
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            series: [{
                type: 'pie',
                allowPointSelect: true,
                keys: ['name', 'y', 'selected', 'sliced'],
                data: data,
                //[
                //['Apples', 29.9, false],
                //['Pears', 71.5, false],
                //['Oranges', 106.4, false],
                //['Plums', 129.2, false],
                //['Bananas', 144.0, false],
                //['Peaches', 176.0, false],
                //['Prunes', 135.6, true, true],
                //['Avocados', 148.5, false]
                //],
                showInLegend: true
            }]
        });

    }

    $scope.initServerTypeData = function () {

        var item = $scope.serverStatusVO.serverTypeList;

        if (item.length == 0) return;

        var data = [];

        for (var i = 0; i < item.length; i++) {
            var member = [];
            if (item[i].serverType == 2) {
                member = [item[i].name, item[i].cnt, true, true];
            } else {
                member = [item[i].name, item[i].cnt, false];
            }
            data.push(member);
        }


        Highcharts.chart('serverType', {
            credits: {enabled: false},
            title: {
                text: '按类型统计'
            },
            plotOptions: {
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '{point.name}: {point.y}' + '(台)'
                    }
                }
            },
            tooltip: {
                //headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                //pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.2f}%</b> of total<br/>'
                formatter: function () {
                    return '<b>' + this.point.name + '</b>: ' + Highcharts.numberFormat(this.percentage, 1) + '% (' +
                        Highcharts.numberFormat(this.y, 0, ',') + ' 台)';
                }
            },
            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },
            series: [{
                type: 'pie',
                allowPointSelect: true,
                keys: ['name', 'y', 'selected', 'sliced'],
                data: data,
                //[
                //['Apples', 29.9, false],
                //['Pears', 71.5, false],
                //['Oranges', 106.4, false],
                //['Plums', 129.2, false],
                //['Bananas', 144.0, false],
                //['Peaches', 176.0, false],
                //['Prunes', 135.6, true, true],
                //['Avocados', 148.5, false]
                //],
                showInLegend: true
            }]
        });

    }

    $scope.initServerCreateData = function () {

        var item = $scope.serverStatusVO.serverCreateList;

        if (item.length == 0) return;

        var dateCat = [];
        var ecsData = [];
        var vmData = [];

        for (var i = 0; i < item.length; i++) {
            dateCat.push(item[i].dateCat);
            ecsData.push(item[i].ecsCnt);
            vmData.push(item[i].vmCnt);
        }

        Highcharts.chart('serverCreate', {
            credits: {enabled: false},
            chart: {
                type: 'column'
            },
            title: {
                text: '月新增统计'
            },
            subtitle: {
                //text: 'Source: WorldClimate.com'
            },
            xAxis: {
                categories: dateCat,
                crosshair: true,
                labels: {
                    // 字段名角度 -45
                    rotation: -45,
                    style: {
                        fontSize: '11px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: '月新增 (台)'
                }
            },
            tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
            },
            plotOptions: {
                column: {
                    pointPadding: 0.2,
                    borderWidth: 0
                },
                series: {
                    dataLabels: {
                        enabled: true,
                        format: '{point.y}'
                    }
                }
            },
            series: [{
                name: 'Ecs',
                data: ecsData

            }, {
                name: 'Vm',
                data: vmData

            }]
        });

    }

    // keybox周登陆统计
    $scope.initKeyboxLoginData = function () {

        var item = $scope.keyboxStatusVO.keyboxLoginCharts;

        if (item.length == 0) return;

        var dateCat = [];
        var keyboxData = [];
        var getwayData = [];

        for (var i = 0; i < item.length; i++) {
            dateCat.push(item[i].dateCat);
            keyboxData.push(item[i].keyboxCnt);
            getwayData.push(item[i].getwayCnt);
        }

        Highcharts.chart('keyboxLoginByWeek', {
            chart: {
                type: 'column'
            },
            title: {
                text: '堡垒机登陆统计'
            },
            xAxis: {
                categories: dateCat
            },
            yAxis: {
                min: 0,
                title: {
                    text: '总登陆次数'
                },
                stackLabels: {
                    enabled: true,
                    style: {
                        fontWeight: 'bold',
                        color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
                    }
                }
            },
            legend: {
                align: 'right',
                x: -30,
                verticalAlign: 'top',
                y: 25,
                floating: true,
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
                borderColor: '#CCC',
                borderWidth: 1,
                shadow: false
            },
            tooltip: {
                headerFormat: '<b>{point.x}</b><br/>',
                pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
            },
            plotOptions: {
                column: {
                    stacking: 'normal',
                    dataLabels: {
                        enabled: true,
                        //color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
                    }
                }
            },
            series: [{
                name: 'Keybox',
                data: keyboxData
            }, {
                name: 'Getway',
                data: getwayData
            }]
        });

    }

    $scope.statusLogService = function () {

        var url = "/logService/status";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.logServiceStatusVO = data.body;
                $scope.initLogServiceStatusData();
                $scope.initLogServiceTopUserData();
                $scope.initLogServiceTopProjectData();
            }
        });
    }

    $scope.statusKeybox = function () {

        var url = "/box/status";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.keyboxStatusVO = data.body;
                $scope.initKeyboxTopUserData();
                $scope.initKeyboxLoginData();
            }
        });
    }

    $scope.statusCi = function () {

        var url = "/statistics/ci/status";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.ciStatusVO = data.body;
                $scope.refreshCiDeployInfo();
                $scope.initCiTopProjectData();
                $scope.initCiTopUserData();
            }
        });
    }

    $scope.statusServer = function () {

        var url = "/server/status";

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.serverStatusVO = data.body;
                $scope.initServerEnvTypeData();
                $scope.initServerTypeData();
                $scope.initServerCreateData();
            }
        });
    }

    var init = function () {
        $scope.statusLogService();
        $scope.statusCi();
        $scope.statusKeybox();
        $scope.statusServer();
    }

    init();


});



