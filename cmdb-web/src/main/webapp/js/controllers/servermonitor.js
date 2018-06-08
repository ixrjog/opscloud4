'use strict';

app.controller('serverMonitorCtrl', function($scope, $state, $uibModal, $timeout, $localStorage, staticModel, httpService, toaster) {
    $scope.userType = staticModel.userType;
    $scope.envType = staticModel.envType;

    $scope.nowType = 0;

    $scope.queryName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    var gaugeOptions = {
        chart: {
            type: 'solidgauge'
        },
        title: null,
        pane: {
            center: ['50%', '50%'],
            size: '100%',
            startAngle: -90,
            endAngle: 90,
            background: {
                backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || '#EEE',
                innerRadius: '60%',
                outerRadius: '100%',
                shape: 'arc'
            }
        },
        tooltip: {
            enabled: false
        },
        // the value axis
        yAxis: {
            stops: [
                [0.1, '#55BF3B'], // green
                [0.5, '#DDDF0D'], // yellow
                [0.9, '#DF5353'] // red
            ],
            lineWidth: 0,
            minorTickInterval: null,
            tickAmount: 2,
            title: {
                y: -30
            },
            labels: {
                y: 16
            }
        },

        plotOptions: {
            solidgauge: {
                dataLabels: {
                    y: 5,
                    borderWidth: 0,
                    useHTML: true
                }
            }
        },
        credits: {
            enabled: false
        }
    };

    //////////////////////////////// 获取服务器信息 /////////////////////////////////

    var server = null;

    var subFlag = false;

    var login = function(body) {
        var subContext = {
            id : $localStorage.settings.user.token,
            token : $localStorage.settings.user.token,
            requestType : "serverGroup",
            body : body
        };

        server.send(JSON.stringify(subContext));
    }

    /**
     * 初始化server ws
     */
    var initServer = function() {
        if (server != null) {
            return;
        }

        server = httpService.wsInstance();

        server.onMessage(function(data) {
            var responseBody = JSON.parse(data.data);
            var data = responseBody.data;

            if(data == null || data == undefined) {
                return;
            }

            for(var i = 0; i < data.length; i++) {
                var item = data[i];
                var cpuEle = $("#cpu" + item.serverId).highcharts();
                var memEle = $("#mem" + item.serverId).highcharts();

                setChartInterval(cpuEle, item.history.cpuUser, memEle, item.history.memoryAvailable / item.history.memoryTotal * 100);
            }
        });

        server.onError(function(data) {
            toaster.pop("error", "WS异常连接关闭");
            server = null;
        });

        server.onClose(function(data) {
            toaster.pop("warning", "WS连接关闭");
            server = null;
        });
    }
    //////////////////////////////// 获取服务器信息 /////////////////////////////////

    //////////////////////////////// 查看服务器组详情 //////////////////////////////////

    /**
     * 更新表格行列
     * @param idx
     * @param item
     */
    var rowsUpdate = function(idx, item, serverList) {
        if($scope.nowDetailIdx == -1) {
            $scope.nowDetailIdx = idx + 1;
        } else if ($scope.nowDetailIdx == (idx + 1)) {
            return;
        } else {
            $scope.pageData.splice($scope.nowDetailIdx, 1);

            if($scope.nowDetailIdx <= idx) {
                $scope.nowDetailIdx = idx == 0 ? 1 : idx;
            } else {
                $scope.nowDetailIdx = idx == 0 ? 1 : (idx + 1);
            }
        }

        var detail = {
            detail : true,
            serverGroup : item,
            serverList : serverList
        }
        $scope.pageData.splice($scope.nowDetailIdx, 0, detail);
    }

    $scope.nowDetailIdx = -1;
    $scope.lookDetail = function(idx, item) {
        var url = "/servergroup/servers?groupId=" + item.id;
        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                rowsUpdate(idx, item, body);

                queryDetail(body, item.id);
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    var queryDetail = function(serverList, serverGroupId) {
        if(serverList.length == 0) {
            return;
        }
        $timeout(function() {
            var flag = true;
            while(flag) {
                var elements = angular.element("#cpu" + serverList[0].id);
                if(elements != null && elements.length != 0) {
                    flag = false;
                }
            }

            initServer();
            login(serverGroupId);

            if(!subFlag) {

            }

            for(var index = 0; index <serverList.length; index++) {
                var cpuChart = initCPU(serverList[index].id);
                var memChart = initMEM(serverList[index].id);

                setChartInterval(cpuChart, 0, memChart, 0);
            }
        }, 100);
    }
    //////////////////////////////// 查看服务器组详情 //////////////////////////////////

    $scope.doQuery = function() {
        if($scope.nowType == null) {
            $scope.nowType = 0;
        }

        var url = "/servergroup/query/page?"
            + "name=" + $scope.queryName + "&"
            + "useType=" + $scope.nowType + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    ////////////////////////////////////////////////////////

    $scope.reSet = function() {
        $scope.queryName = "";
        $scope.nowType = 0;
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////


    /////////////////////////// 初始化图表 ////////////////////////////

    var initCPU = function(index) {
        var chart = Highcharts.chart('cpu' + index, Highcharts.merge(gaugeOptions, {
            yAxis: {
                min: 0,
                max: 100,
                title: {
                    text: 'CPU'
                }
            },

            credits: {
                enabled: false
            },

            series: [{
                name: 'CPU',
                data: [0],
                dataLabels: {
                    format: '<div style="text-align:center"><span style="font-size:12px;color:' +
                    ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y:.2f}</span>' +
                    '<span style="font-size:12px;color:silver">%</span></div>'
                },
                tooltip: {
                    valueSuffix: '%'
                }
            }]
        }));
        return chart;
    }

    var initMEM = function(index) {
        var chart = Highcharts.chart('mem' + index, Highcharts.merge(gaugeOptions, {
            yAxis: {
                min: 0,
                max: 100,
                title: {
                    text: 'MEM'
                }
            },

            series: [{
                name: 'MEM',
                data: [0],
                dataLabels: {
                    format: '<div style="text-align:center"><span style="font-size:12px;color:' +
                    ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y:.2f}</span>' +
                    '<span style="font-size:12px;color:silver">%</span></div>'
                },
                tooltip: {
                    valueSuffix: '%'
                }
            }]
        }));
        return chart;
    }

    var setChartInterval = function(cpuChart, newCpuVal, memChart, newMemVal) {
        //setInterval(function () {
            var point,
                newVal,
                inc;

            // CPU
            if (cpuChart && cpuChart != null && cpuChart.series != null) {
                point = cpuChart.series[0].points[0];
                //inc = Math.round((Math.random() - 0.5) * 100);
                //newVal = point.y + inc;
                //
                //if (newVal < 0 || newVal > 100) {
                //    newVal = point.y - inc;
                //}

                point.update(newCpuVal);
            }

            // MEM
            if (memChart && memChart != null && memChart.series != null) {
                point = memChart.series[0].points[0];
                //inc = Math.random() - 0.5;
                //newVal = point.y + inc;
                //
                //if (newVal < 0 || newVal > 5) {
                //    newVal = point.y - inc;
                //}

                point.update(newMemVal);
            }
        //}, 2000);
    }
    /////////////////////////// 初始化图表 ////////////////////////////
});