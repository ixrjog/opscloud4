'use strict';

app.controller('keyboxCtrl', function ($scope, $localStorage, $state, $compile, $sce, $timeout, toaster, httpService, staticModel) {
    $scope.envType = staticModel.envType;

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

    var init = function() {
        $scope.envChoose = {
            prod: false,
            back: false,
            gray: false,
            daily: false
        };
    }

    $scope.nowServerGroup = {};
    $scope.serverGroupList = [];

    /////////////////////////////////////////////////////////////////////

    $scope.allChoose = false;
    $scope.chooseAllItem = function () {
        if ($scope.allChoose) {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = true;
            }
        } else {
            for (var i = 0; i < $scope.pageData.length; i++) {
                $scope.pageData[i].choose = false;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////
    // 4 6 3 2
    $scope.envChoose = {
        prod: false,
        back: false,
        gray: false,
        daily: false
    };

    $scope.chooseEnv = function (envCode) {

        for (var i = 0; i < $scope.pageData.length; i++) {
            //var item = $scope.pageData[i];
            if ($scope.pageData[i].envType == envCode) {
                switch (envCode) {
                    case 4:
                        $scope.pageData[i].choose = $scope.envChoose.prod;
                        break;
                    case 6:
                        $scope.pageData[i].choose = $scope.envChoose.back;
                        break;
                    case 3:
                        $scope.pageData[i].choose = $scope.envChoose.gray;
                        break;
                    case 2:
                        $scope.pageData[i].choose = $scope.envChoose.daily;
                        break;
                    default:
                        return;
                }
            }

        }
    }

    ////////////////////////////////////////////////////////////////////
    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 20;

    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/box/server/query?"
            + "serverGroupId=" + ($scope.nowServerGroup.selected == null ? -1 : $scope.nowServerGroup.selected.id)
            + "&envType=" + ($scope.nowEnv == null ? -1 : $scope.nowEnv)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : $scope.currentPage - 1)
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.totalItems = body.size;
                $scope.pageData = body.data;
                $scope.refreshServerInfo();
                init();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();

    /**
     * table or xterm
     * @type {string}
     */
    $scope.showType = "table";
    $scope.nowEnv = -1;

    ////////////////////////////////////////////////////////////////////////

    var server = null;

    $scope.boxList = [];


    var login = function (boxItem) {
        var initContext = {
            id: boxItem.id,
            token: $localStorage.settings.user.token,
            requestType: "init",
            body: boxItem.instance.id
        };

        server.send(JSON.stringify(initContext));
    }

    /**
     * 重新调整大小
     * @param boxItem
     */
    var resize = function (boxItem) {
        var height = boxItem.height - 38;
        var newWidth = Math.floor(boxItem.width / 7.2981);
        var newHeight = Math.floor(height / 17);
        boxItem.height = newHeight * 17 + 38 + 6 + 2;

        var resizeBody = {
            id: boxItem.id,
            token: $localStorage.settings.user.token,
            requestType: "resize",
            body: {
                width: boxItem.width,
                height: height
            }
        }

        server.send(JSON.stringify(resizeBody));

        boxItem.xterm.resize(newWidth, newHeight);
    }

    /**
     * 发出键入命令
     * @param boxItem
     * @param data
     */
    var sendCmd = function (boxItem, data) {
        var requestBody = {
            id: boxItem.id,
            token: $localStorage.settings.user.token,
            requestType: "command",
            body: data
        }

        server.send(JSON.stringify(requestBody));
    }

    var doCloseRemote = function (boxItem) {
        var requestBody = {
            id: boxItem.id,
            token: $localStorage.settings.user.token,
            requestType: "close"
        }

        server.send(JSON.stringify(requestBody));
    }

    $scope.closeAll = function () {
        for (var i = 0; i < $scope.boxList.length;) {
            var boxItem = $scope.boxList[i];
            var xterm = boxItem.xterm;

            xterm.destroy();

            doCloseRemote(boxItem);
            $scope.boxList.splice(i, 1);
        }

        $scope.showType = "table";
        server.close(true);
    }

    $scope.closeItem = function (id) {
        for (var i = 0; i < $scope.boxList.length; i++) {
            var boxItem = $scope.boxList[i];
            var xterm = boxItem.xterm;

            if (boxItem.id == id) {
                xterm.destroy();

                doCloseRemote(boxItem);
                $scope.boxList.splice(i, 1);
                break;
            }
        }

        if ($scope.boxList.length == 0) {
            $scope.showType = "table";
            server.close(true);
        }
    }

    /**
     * 初始化server ws
     */
    var initServer = function () {
        server = httpService.wsInstance();

        server.onMessage(function (data) {
            var responseBody = JSON.parse(data.data);

            var id = responseBody.id;
            var type = responseBody.type;
            for (var i = 0; i < $scope.boxList.length; i++) {
                var boxItem = $scope.boxList[i];
                if (id == boxItem.id) {
                    if (type == 1) {
                        resize(boxItem);
                    } else if (type == 0) {
                        boxItem.xterm.write(responseBody.data);
                    } else if (type == 2) {
                        boxItem.xterm.destroy();
                        boxItem.showError = true;
                        boxItem.errorInfo = responseBody.data;
                    }
                }
            }
        });

        server.onError(function (data) {
            toaster.pop("error", "WS异常连接关闭");
        });

        server.onClose(function (data) {
            toaster.pop("warning", "WS连接关闭");
        });
    }

    $scope.loginBoxList = function () {
        var boxList = [];
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            if (item.choose) {
                boxList.push(item);
            }
        }

        if (boxList.length == 0) {
            toaster.pop("warning", "需要选中至少一个server!");
            return;
        }
        initServer();
        for (var i = 0; i < boxList.length; i++) {
            loginSSH(boxList[i]);
        }

        $scope.app.settings.asideFolded = true;
        $scope.allChoosed = true;
        $scope.showType = "xterm";
    }

    var loginSSH = function (box) {
        var boxItemDom = "<div id='" + ($localStorage.settings.user.username + "-" + $scope.boxList.length) + "'></div>";

        var term = new Terminal({
            colors: Terminal.tangoColors,
            theme: 'default',
            convertEol: false,
            termName: 'xterm',
            geometry: [80, 20],
            cursorBlink: true,
            visualBell: false,
            popOnBell: false,
            scrollback: 1000,
            screenKeys: false,
            debug: false,
            cancelEvents: false
        });

        var boxItem = {
            id: ($localStorage.settings.user.username + "-" + $scope.boxList.length),
            'width': 600,
            'height': 384,
            instance: box,
            xterm: term,
            errorInfo: "",
            xtermDom: $sce.trustAsHtml(boxItemDom),
            showError: false,
            hasChoose: false
        }

        $scope.boxList.push(boxItem);

        $timeout(function () {
            var element = document.getElementById(boxItem.id);
            if (element == null) {
                toaster.pop("warning", "未找到元素!");
                return;
            }

            var parentWidth = element.parentNode.offsetWidth;
            boxItem.width = (parentWidth - 40) / 2;

            term.open(element);
            term.focus();

            term.on("data", function (data) {
                if ($scope.allChoosed) {
                    for (var i = 0; i < $scope.boxList.length; i++) {
                        var tmpItem = $scope.boxList[i];

                        if (tmpItem.showError) {
                            continue;
                        }
                        sendCmd(tmpItem, data);
                    }
                } else {
                    sendCmd(boxItem, data);
                }
            });

            login(boxItem);
        }, 1000);
    }

    ///////////////////////////////////////////////////////////////

    $scope.allChoosed = true;
    $scope.needDigest = true;
    $scope.chooseAll = function () {
        if ($scope.boxList.length > 0) {
            $scope.needDigest = false;
            $scope.boxList[0].xterm.focus();
            $scope.allChoosed = true;
            $scope.needDigest = true;
        }
    }

    $scope.chooseOne = function (boxItem) {
        $scope.allChoosed = false;
        for (var i = 0; i < $scope.boxList.length; i++) {
            var tmpItem = $scope.boxList[i];
            tmpItem.hasChoose = false;
        }
        boxItem.hasChoose = true;
    }

    ///////////////////////////////////////////////////////////////
    $scope.$on("angular-resizable.resizeEnd", function (event, args) {
        var boxItem = null;
        for (var i = 0; i < $scope.boxList.length; i++) {
            var boxTmp = $scope.boxList[i];
            if (boxTmp.id == args.id) {
                boxItem = boxTmp;
                break;
            }
        }
        if (boxItem != null) {
            var element = document.getElementById(boxItem.id);
            if (element == null) {
                toaster.pop("warning", "未找到元素!");
                return;
            }
            var parentWidth = element.parentNode.offsetWidth;

            if (args.width) {
                var nowWidth = args.width;
                if (nowWidth >= (parentWidth - 30)) {
                    nowWidth = parentWidth - 30;
                }
                boxItem.width = nowWidth;
            }
            if (args.height)
                boxItem.height = args.height;

            resize(boxItem);
        }
    });

    $(window).keypress(function (e) {
        if (e.which == 32) {
            return false;
        }
    });


    $scope.refreshServerInfo = function () {
        for (var i = 0; i < $scope.pageData.length; i++) {
            var item = $scope.pageData[i];
            // "<b style='color: red'>I can</b> have <div class='label label-success'>HTML "
            var info = "";
            if (item.serverType == 1) {
                info = '<b style="color: #286090">' + "VM Server" + "</b><br/>";
                info += "cpu : " + item.vmServerDO.cpu + "<br/>";
                info += "memory : " + item.vmServerDO.memory / 1024 + "G<br/>";
            }
            if (item.serverType == 2) {
                info = '<b style="color: #286090">' + "ECS Server" + "</b><br/>";
                info += "cpu : " + item.ecsServerDO.cpu + "<br/>";
                info += "memory : " + item.ecsServerDO.memory / 1024 + "G<br/>";
                info += "可用区 : " + item.ecsServerDO.area + "<br/>";
                info += "带宽 : " + item.ecsServerDO.internetMaxBandwidthOut + "Mbps<br/>";
                if (item.ecsServerDO.ioOptimized == true) {
                    info += '<b>(IO优化实例)</b><br/>'
                }
                info += "instanceId : " + item.ecsServerDO.instanceId + "<br/>";
                info += "创建时间 : " + item.ecsServerDO.creationTime + "<br/>";
                info += "Disk1 : " + item.ecsServerDO.systemDiskSize + "G(" + item.ecsServerDO.systemDiskCategory + ")<br/>";
                info += "Disk2 : " + item.ecsServerDO.dataDiskSize + "G(" + item.ecsServerDO.dataDiskCategory + ")<br/>";
            }

            item.serverInfo = $sce.trustAsHtml(
                info
            );
        }
    }


});