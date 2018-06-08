'use strict';

app.controller('serverGroupCtrl', function ($scope, $state, $uibModal, $timeout, staticModel, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.useType = [];

    $scope.nowType = 0;

    $scope.queryName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };


    //////////////////////////////// 查看服务器组详情 //////////////////////////////////
    $scope.nowDetailIdx = -1;
    $scope.lookDetail = function (idx, item) {
        if ($scope.nowDetailIdx == -1) {
            $scope.nowDetailIdx = idx + 1;
        } else if ($scope.nowDetailIdx == (idx + 1)) {
            return;
        } else {
            $scope.pageData.splice($scope.nowDetailIdx, 1);

            if ($scope.nowDetailIdx <= idx) {
                $scope.nowDetailIdx = idx == 0 ? 1 : idx;
            } else {
                $scope.nowDetailIdx = idx == 0 ? 1 : (idx + 1);
            }
        }

        var detail = {
            detail: true,
            serverGroup: item
        }
        $scope.pageData.splice($scope.nowDetailIdx, 0, detail);


        $timeout(function () {
            var flag = true;
            while (flag) {
                var elements = angular.element("#cpu0");
                if (elements != null && elements.length != 0) {
                    flag = false;
                }
            }

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

// The speed gauge
            var chartSpeed = Highcharts.chart('cpu0', Highcharts.merge(gaugeOptions, {
                yAxis: {
                    min: 0,
                    max: 200,
                    title: {
                        text: 'CPU'
                    }
                },

                credits: {
                    enabled: false
                },

                series: [{
                    name: 'CPU',
                    data: [80],
                    dataLabels: {
                        format: '<div style="text-align:center"><br/><span style="font-size:12px;color:' +
                        ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y}</span><br/>' +
                        '<span style="font-size:12px;color:silver">km/h</span></div>'
                    },
                    tooltip: {
                        valueSuffix: '%'
                    }
                }]

            }));

// The RPM gauge
            var chartRpm = Highcharts.chart('mem0', Highcharts.merge(gaugeOptions, {
                yAxis: {
                    min: 0,
                    max: 5,
                    title: {
                        text: 'MEM'
                    }
                },

                series: [{
                    name: 'MEM',
                    data: [1],
                    dataLabels: {
                        format: '<div style="text-align:center"><span style="font-size:12px;color:' +
                        ((Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black') + '">{y:.1f}</span><br/>' +
                        '<span style="font-size:12px;color:silver">* 1000 / min</span></div>'
                    },
                    tooltip: {
                        valueSuffix: '%'
                    }
                }]

            }));

            setInterval(function () {
                // Speed
                var point,
                    newVal,
                    inc;

                if (chartSpeed) {
                    point = chartSpeed.series[0].points[0];
                    inc = Math.round((Math.random() - 0.5) * 100);
                    newVal = point.y + inc;

                    if (newVal < 0 || newVal > 200) {
                        newVal = point.y - inc;
                    }

                    point.update(newVal);
                }

                // RPM
                if (chartRpm) {
                    point = chartRpm.series[0].points[0];
                    inc = Math.random() - 0.5;
                    newVal = point.y + inc;

                    if (newVal < 0 || newVal > 5) {
                        newVal = point.y - inc;
                    }

                    point.update(newVal);
                }
            }, 2000);
        }, 100);
    }
    //////////////////////////////// 查看服务器组详情 //////////////////////////////////


    $scope.queryUseType = function () {
        var url = "/servergroup/useType/query";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.useType =  data.body;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryUseType();

    $scope.doQuery = function () {
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }

        var url = "/servergroup/query/page?"
            + "name=" + $scope.queryName + "&"
            + "useType=" + $scope.nowType + "&"
            + "page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
            + "length=" + $scope.pageLength;

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

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.nowType = 0;
        $scope.doQuery();
        $scope.queryUseType();
    }

    ///////////////////////////////////////////////////////

    /**
     * 新增
     */
    $scope.addServerGroup = function () {
        var showItem = {
            id: 0,
            name: "group_",
            useType: 0,
            content: ""
        }

        saveServerGroup(showItem);
    }

    /**
     * 编辑
     * @param item
     */
    $scope.editServerGroup = function (item) {
        var body = {
            id: item.id,
            name: item.name,
            useType: item.useType,
            content: item.content
        }
        saveServerGroup(body);
    }

    /**
     * 删除
     * @param item
     */
    $scope.delServerGroup = function (item) {
        var url = "/servergroup/del?id=" + item.id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    /**
     * 保存
     * @param item
     */
    var saveServerGroup = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'eidtInfo',
            controller: 'editInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                serverGroupItem: function () {
                    return item;
                },
                userType: function () {
                    return $scope.useType;
                }
            }
        });

        modalInstance.result.then(function (serverGroupItem) {
            var url = "/servergroup/update";
            httpService.doPostWithJSON(url, serverGroupItem).then(function (data) {
                if (data.success) {
                    toaster.pop("success", "更新成功！");
                    $scope.doQuery();
                } else {
                    toaster.pop("warning", data.msg);
                }
            }, function (err) {
                toaster.pop("error", err);
            });
        });
    }

    ///////////////////////////////////////////////////////
    /**
     * 增删查改指定服务器组的服务器列表
     * @param item
     */
    $scope.serverList = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'serverList',
            controller: 'serverListInstanceCtrl',
            size: 'lg',
            resolve: {
                toaster: function () {
                    return toaster;
                },
                httpService: function () {
                    return httpService;
                },
                serverGroup: function () {
                    return item;
                },
                userType: function () {
                    return $scope.userType;
                }
            }
        });
    }

    ///////////////////////////////////////////////////////
    $scope.ipnetworkList = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'ipnetworkList',
            controller: 'ipNetworkListInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                serverGroup: function () {
                    return item;
                },
                staticModel: function () {
                    return staticModel;
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////

    $scope.groupPropertyList = function (groupId) {
        var url = "/servergroup/propertygroup/query?"
            + "groupId=" + groupId
            + "&page=" + 0
            + "&length=" + 1;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                var modalInstance = $uibModal.open({
                    templateUrl: 'servergpg',
                    controller: 'servergpgInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        httpService: function () {
                            return httpService;
                        },
                        propertyGroup: function () {
                            return body.data;
                        }
                    }
                });
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////////////
});

app.controller('serverGroupUseTypeCtrl', function ($scope, $state, $uibModal, $timeout, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.queryName = "";

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    //////////////////////////////// 查看服务器组详情 //////////////////////////////////

    $scope.doQuery = function () {

        var url = "/servergroup//useType/page?"
            + "typeName=" + $scope.queryName
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1) + "&"
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

    ////////////////////////////////////////////////////////

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.doQuery();
    }

    ///////////////////////////////////////////////////////
    /**
     * 增删查改服务器组属性
     * @param item
     */
    $scope.editUseType = function (useTypeItem) {
        var modalInstance = $uibModal.open({
            templateUrl: 'servergpgUseType',
            controller: 'useTypeInstanceCtrl',
            size: 'lg',
            resolve: {
                toaster: function () {
                    return toaster;
                },
                httpService: function () {
                    return httpService;
                },
                useTypeItem: function () {
                    return useTypeItem;
                }
            }
        });
    }

    $scope.addUseType = function () {
        var useTypeItem = {
            id: 0,
            useType: 0,
            typeName: "",
            content: ""
        }

        var modalInstance = $uibModal.open({
            templateUrl: 'servergpgUseType',
            controller: 'useTypeInstanceCtrl',
            size: 'lg',
            resolve: {
                toaster: function () {
                    return toaster;
                },
                httpService: function () {
                    return httpService;
                },
                useTypeItem: function () {
                    return useTypeItem;
                }
            }
        });
    }

    /**
     * 删除
     * @param item
     */
    $scope.delUseType = function (item) {
        var url = "/servergroup/useType/del?id=" + item.id;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功！");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////////////////
});

app.controller('servergpgInstanceCtrl', function ($scope, $uibModalInstance, propertyGroup) {
    $scope.propertyGroup = propertyGroup;
});

app.controller('editInfoInstanceCtrl', function ($scope, $uibModalInstance, serverGroupItem, userType) {
    $scope.showItem = serverGroupItem;
    $scope.userType = userType;

    $scope.saveEdit = function () {
        $uibModalInstance.close($scope.showItem);
    };
});

app.controller('serverListInstanceCtrl', function ($scope, $uibModalInstance, toaster, httpService, serverGroup, userType, staticModel) {
    $scope.serverGroup = serverGroup;
    $scope.useType = userType;

    $scope.envType = staticModel.envType;

    //登录类型
    $scope.logType = staticModel.logType;

    //服务器类型
    $scope.serverType = staticModel.serverType;

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function (currentPage) {
        $scope.currentPage = currentPage;
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.queryName = "";
    $scope.nowType = 0;
    $scope.nowEnv = -1;

    var queryServerList = function () {
        if ($scope.nowType == null) {
            $scope.nowType = 0;
        }
        if ($scope.nowEnv == null) {
            $scope.nowEnv = -1;
        }

        var url = "/server/page?"
            + "serverGroupId=" + $scope.serverGroup.id
            + "&serverName=" + ($scope.$$childTail == null ? $scope.queryName : $scope.$$childTail.queryName)
            + "&useType=" + ($scope.$$childTail == null ? $scope.nowType : $scope.$$childTail.nowType)
            + "&envType=" + ($scope.$$childTail == null ? $scope.nowEnv : $scope.$$childTail.nowEnv)
            + "&queryIp="
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

    queryServerList();

    $scope.reSet = function () {
        $scope.queryName = "";
        $scope.$$childTail.queryName = "";

        $scope.nowType = 0;
        $scope.$$childTail.nowType = 0;

        $scope.nowEnv = -1;
        $scope.$$childTail.nowEnv = -1;
        queryServerList();
    }

    $scope.doQuery = function () {
        queryServerList();
    }

    ////////////////////////////////////////////////////////////

    $scope.showEdit = false;

    $scope.addServer = function () {
        $scope.showEdit = true;

        $scope.serverItem = {
            serverGroupId: $scope.serverGroup.id,
            serverName: "",
            serverType: -1,
            loginType: -1,
            loginUser: "",
            envType: -1,
            area: "",
            publicIp: "",
            insideIp: "",
            serialNumber: "",
            ciGroup: "",
            content: ""
        }
    }

    $scope.editServer = function (serverItem) {
        $scope.showEdit = true;
        $scope.serverItem = {
            id: serverItem.id,
            serverGroupId: $scope.serverGroup.id,
            serverName: serverItem.serverName,
            serverType: serverItem.serverType,
            loginType: serverItem.loginType,
            loginUser: serverItem.loginUser,
            envType: serverItem.envType,
            area: serverItem.area,
            publicIp: serverItem.publicIp,
            insideIp: serverItem.insideIp,
            serialNumber: serverItem.serialNumber,
            ciGroup: serverItem.ciGroup,
            content: serverItem.content
        }
    }

    $scope.resetServerItem = function () {
        $scope.serverItem = {
            id: 0,
            serverGroupId: $scope.serverGroup.id,
            serverName: "",
            serverType: -1,
            loginType: -1,
            loginUser: "",
            envType: -1,
            area: "",
            publicIp: "",
            insideIp: "",
            serialNumber: "",
            ciGroup: "",
            content: ""
        }
    }

    /**
     * 保存server item信息
     */
    $scope.saveServerItem = function () {
        var url = "/server/save";

        httpService.doPostWithJSON(url, $scope.serverItem).then(function (data) {
            if (data.success) {
                toaster.pop("success", "保存成功！");
                $scope.resetServerItem();
                $scope.showEdit = false;

                queryServerList();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    /**
     * 删除server 信息
     * @param serverId
     */
    $scope.delServer = function (serverId) {
        var url = "/server/del?serverId=" + serverId;

        httpService.doDelete(url).then(function (data) {
            toaster.pop("success", "删除成功！");
            $scope.resetServerItem();
            $scope.showEdit = false;

            queryServerList();
        }, function (err) {
            toaster.pop("warning", err);
        });
    }
});

app.controller('ipNetworkListInstanceCtrl', function ($scope, $uibModalInstance, httpService, serverGroup, staticModel) {
    $scope.serverGroup = serverGroup;

    $scope.ipType = staticModel.ipType;

    $scope.nowIpType = -1;

    $scope.ipNetwork = "";

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

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 8;

    $scope.pageChanged = function () {
        queryIPGroup();
    };

    /////////////////////////////////////////////////

    var queryIPGroup = function () {
        var url = "/ipgroup/query?" + "page=" + ($scope.currentPage == 0 ? 0 : ($scope.currentPage - 1)) + "&length=" + $scope.pageLength;

        var queryBody = {
            ipNetwork: $scope.ipNetwork,
            serverGroupId: 0,
            ipType: $scope.nowIpType
        }
        httpService.doPostWithJSON(url, queryBody).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    queryIPGroup();


    /////////////////////////////////////////////////

    $scope.joinedPageData = [];
    $scope.joinedTotalItems = 0;
    $scope.joinedCurrentPage = 0;
    $scope.joinedPageLength = 8;

    $scope.joinedPageChanged = function () {
        queryjoinedIPGroup();
    };

    /////////////////////////////////////////////////

    var queryjoinedIPGroup = function () {
        var url = "/ipgroup/serverGroup/query?serverGroupId=" + $scope.serverGroup.id
            + "&page=" + ($scope.joinedCurrentPage == 0 ? 0 : ($scope.joinedCurrentPage - 1))
            + "&length=" + $scope.joinedPageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.joinedPageData = body.data;
                $scope.joinedTotalItems = body.size;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    queryjoinedIPGroup();

    ///////////////////////////////////////////////////////////

    /**
     * 建立服务器组与网段的绑定关系
     * @param item
     */
    $scope.addItem = function (item) {
        var url = "/servergroup/ipgroup/add?"
            + "serverGroupId=" + $scope.serverGroup.id
            + "&ipGroupId=" + item.id
            + "&ipType=" + item.ipType;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "加入成功!";
                queryjoinedIPGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    /**
     * 解除服务器组与网段的绑定关系
     * @param item
     */
    $scope.delItem = function (item) {
        var url = "/servergroup/ipgroup/del?"
            + "serverGroupId=" + $scope.serverGroup.id
            + "&ipGroupId=" + item.id;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "移除成功!";
                queryjoinedIPGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }
});

app.controller('serverGroupPropertyCtrl', function ($scope, $state, $uibModal, httpService, toaster) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.nowShowType = 0; //0:收起;1:展开
    $scope.nowGroupId = 0;

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

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    $scope.reSet = function () {
        $scope.nowServerGroup = {};
    }

    ////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 1;

    $scope.pageChanged = function (currentPage) {
        $scope.currentPage = currentPage;
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/servergroup/propertygroup/query?"
            + "groupId=" + ($scope.nowServerGroup.selected == null ? 0 : $scope.nowServerGroup.selected.id)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }
    $scope.doQuery();

    ////////////////////////////////////////////////////////////

    $scope.addItem = function () {
        var item = {
            serverGroupDO: $scope.nowServerGroup.selected,
            serverPropertyGroup: null,
            type: "new",
            groupProperties: []
        }

        saveItem(item);
    }

    $scope.editItem = function (serverGroupDO, serverPropertyGroup, groupProperties) {
        var item = {
            serverGroupDO: serverGroupDO,
            serverPropertyGroup: serverPropertyGroup,
            type: "edit",
            groupProperties: groupProperties
        }

        saveItem(item);
    }

    $scope.delItem = function (groupId, propertyGroupId) {
        var url = "/config/propertygroup/del?"
            + "serverId=" + 0
            + "&groupId=" + groupId
            + "&propertyGroupId=" + propertyGroupId;

        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "删除成功!");
                $scope.doQuery();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    var saveItem = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'servergpsInfo',
            controller: 'servergpsInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                groupItem: function () {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    ///////////////////////////////////////////////////////

    $scope.previewPropertyGroup = function (serverGroupId, propertyGroupId) {
        var url = "/servergroup/propertygroup/preview?"
            + "serverGroupId=" + serverGroupId
            + "&propertyGroupId=" + propertyGroupId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'propertyLaunch',
                    controller: 'propertyLaunchInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        httpService: function () {
                            return httpService;
                        },
                        propertyInfo: function () {
                            return data.body;
                        }
                    }
                });
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.createConfig = function (serverGroupId, propertyGroupId) {
        var url = "/servergroup/propertygroup/create?"
            + "serverGroupId=" + serverGroupId
            + "&propertyGroupId=" + propertyGroupId;

        httpService.doPost(url).then(function (data) {
            if (data.success) {
                toaster.pop("success", "生成成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.launchPropertyGroup = function (serverGroupId, propertyGroupId) {
        var url = "/servergroup/propertygroup/launch?"
            + "serverGroupId=" + serverGroupId
            + "&propertyGroupId=" + propertyGroupId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'propertyLaunch',
                    controller: 'propertyLaunchInstanceCtrl',
                    size: 'lg',
                    resolve: {
                        httpService: function () {
                            return httpService;
                        },
                        propertyInfo: function () {
                            return data.body;
                        }
                    }
                });
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

});

app.controller('servergpsInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, groupItem) {
    $scope.groupItem = groupItem;

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

    ////////////////////////////////////////////////////////////

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

    if ($scope.groupItem.serverGroupDO != null) {
        $scope.nowServerGroup = {
            selected: $scope.groupItem.serverGroupDO
        }
    } else {
        $scope.nowServerGroup = {};
    }
    $scope.serverGroupList = [];

    ////////////////////////////////////////////////////////////

    if ($scope.groupItem.serverPropertyGroup != null) {
        $scope.propertyGroup = {
            selected: $scope.groupItem.serverPropertyGroup
        }
    } else {
        $scope.propertyGroup = {};
    }
    $scope.propertyGroupList = [];

    $scope.queryGroup = function (groupName) {
        var url = "/config/property/group?"
            + "groupName=" + groupName
            + "&page=" + 0
            + "&length=" + 10;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.propertyGroupList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("warning", err);
        });
    }

    ////////////////////////////////////////////////////////////

    $scope.groupProperties = $scope.groupItem.groupProperties;
    $scope.changePropertyGroup = function () {
        var url = "/config/property?"
            + "proName="
            + "&groupId=" + $scope.propertyGroup.selected.id
            + "&page=" + 0
            + "&length=" + 10000;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.groupProperties = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    ////////////////////////////////////////////////////////////

    $scope.saveData = function () {
        var url = "/config/propertygroup/save";

        if ($scope.nowServerGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定服务器组";
            return;
        }

        if ($scope.propertyGroup.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定属性组";
            return;
        }

        var requestBody = {
            serverGroupDO: $scope.nowServerGroup.selected,
            propertyGroupDO: $scope.propertyGroup.selected,
            propertyDOList: $scope.groupProperties
        }

        httpService.doPostWithJSON(url, requestBody).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";

                $scope.nowServerGroup = {};
                $scope.propertyGroup = {};
                $scope.groupProperties = [];
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

});

app.controller('propertyLaunchInstanceCtrl', function ($scope, $uibModalInstance, $compile, $parse, httpService, propertyInfo) {
    $scope.propertyInfo = propertyInfo;

    $scope.toPrettyJSON = function (objStr, tabWidth) {
        try {
            var obj = $parse(objStr)({});
        } catch (e) {
            // eat $parse error
            return objStr;
        }

        return JSON.stringify(obj, null, Number(tabWidth));
    };

    // $scope.propertyInfo = $sce.trustAsHtml($scope.propertyInfo.replace(/(\r\n|\n|\r)/gm, "<br />"));
});


app.controller('useTypeInstanceCtrl', function ($scope, $uibModalInstance, $compile, $parse, httpService, useTypeItem) {
    $scope.useTypeItem = useTypeItem;

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

    $scope.saveUseType = function () {
        var url = "/servergroup/useType/save";

        if ($scope.useTypeItem.useType == null || $scope.useTypeItem.useType == 0) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定类型值!";
        }
        if ($scope.useTypeItem.typeName == null || $scope.useTypeItem.typeName  == "") {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定类型名称!";
        }

        httpService.doPostWithJSON(url, $scope.useTypeItem ).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功!";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

});