'use strict';

app.controller('aliyunMqMgmtCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.messageType = staticModel.aliyunMqMessageType;

    $scope.topicQueryName = "";
    $scope.topicList = [];

    $scope.subscribeList = [];


    $scope.butSearchSubscribe = false;

    var initTopic = function () {
        for (var i = 0; i < $scope.topicList.length; i++) {
            $scope.topicList[i].gmtCreate = formatTimestamp($scope.topicList[i].createTime)
            $scope.topicList[i].gmtModify = formatTimestamp($scope.topicList[i].updateTime)
        }
    }

    // var initSubscribe = function () {
    //     for (var i = 0; i < $scope.subscribeList.length; i++) {
    //         $scope.subscribeList[i].gmtCreate = formatTimestamp($scope.subscribeList[i].createTime)
    //         $scope.subscribeList[i].gmtModify = formatTimestamp($scope.subscribeList[i].updateTime)
    //     }
    // }

    $scope.queryTopic = function () {
        var url = "/aliyun/mq/topic/query?topic=" + ($scope.topicQueryName == null ? "" : $scope.topicQueryName);
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.topicList = body;
                initTopic();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
        });
    }

    $scope.queryTopic();


    $scope.createTopic = function (templateUrl, controller) {
        var topic = {
            topic: "GGJ_TOPIC_",
            remark: "",
            messageType: 0
        }

        var modalInstance = $uibModal.open({
            templateUrl: "createTopicModal",
            controller: "createTopicInstanceCtrl",
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                messageTypes: function () {
                    return $scope.messageType;
                },
                topic: function () {
                    return topic;
                }
            }
        });
    }

    $scope.createGroup = function () {
        var group = {
            groupId: "GID_",
            remark: ""
        }

        var modalInstance = $uibModal.open({
            templateUrl: "createGroupModal",
            controller: "createGroupInstanceCtrl",
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                group: function () {
                    return group;
                }
            }
        });
    }

    $scope.editGroup = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: "editGroupModal",
            controller: "editGroupInstanceCtrl",
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                group: function () {
                    return item;
                }
            }
        });
    }

    $scope.editGroupUser = function (item) {
        var modalInstance = $uibModal.open({
            templateUrl: "editGroupUserModal",
            controller: "editGroupUserInstanceCtrl",
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                group: function () {
                    return item;
                }
            }
        });
    }

    $scope.queryGroup = function () {
        $scope.butSearchSubscribe = true;
        var url = "/aliyun/mq/group/query";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.subscribeList = body;
                $scope.butSearchSubscribe = false;
            } else {
                toaster.pop("warning", data.msg);
                $scope.butSearchSubscribe = false;
            }
        }, function (err) {
            toaster.pop("error", err);
            $scope.butSearchSubscribe = false;
        });
    }


    var formatTimestamp = function (timestamp) {
        var timestamp = new Date(timestamp);
        var year = 1900 + timestamp.getYear();
        var month = "0" + (timestamp.getMonth() + 1);
        var date = "0" + timestamp.getDate();
        var hour = "0" + timestamp.getHours();
        var minute = "0" + timestamp.getMinutes();
        var second = "0" + timestamp.getSeconds();
        return year + "-" + month.substring(month.length - 2, month.length) + "-" + date.substring(date.length - 2, date.length)
            + " " + hour.substring(hour.length - 2, hour.length) + ":"
            + minute.substring(minute.length - 2, minute.length) + ":"
            + second.substring(second.length - 2, second.length);
    }

});


app.controller('createTopicInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, messageTypes, topic) {
    $scope.topic = topic;
    $scope.messageTypes = messageTypes;

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

    //////////////////////////////////////////////////////

    $scope.createTopic = function () {
        if ($scope.topic.topic == null || $scope.topic.topic == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定Topic名称!";
            return;
        } else {
            $scope.alert.type = '';
        }

        if ($scope.topic.remark == null || $scope.topic.remark == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定备注!";
            return;
        } else {
            $scope.alert.type = '';
        }
        var url = "/aliyun/mq/topic/create";

        httpService.doPostWithJSON(url, $scope.topic).then(function (data) {
            if (data.body.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "创建Topic成功！";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.body.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});

// subscribe
app.controller('createGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, group) {
    $scope.group = group;


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

    //////////////////////////////////////////////////////

    $scope.createGroup = function () {
        if ($scope.group.groupId == null || $scope.group.groupId == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定订阅信息(GID)名称!";
            return;
        } else {
            $scope.alert.type = '';
        }

        var url = "/aliyun/mq/group/create";

        httpService.doPostWithJSON(url, $scope.group).then(function (data) {
            if (data.body.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "创建消费者成功！";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.body.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});


app.controller('editGroupInstanceCtrl', function ($scope, $uibModalInstance, $sce, toaster, httpService, group) {
    $scope.group = group;

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

    //////////////////////////////////////////////////////

    $scope.saveGroup = function () {
        if ($scope.group.groupId == null || $scope.group.groupId == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必须指定订阅信息(GID)名称!";
            return;
        } else {
            $scope.alert.type = '';
        }

        var url = "/aliyun/mq/group/save";

        httpService.doPostWithJSON(url, $scope.group).then(function (data) {
            if (data.body.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "配置订阅信息告警成功！";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.body.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }


});

// TODO 编辑告警接受人
app.controller('editGroupUserInstanceCtrl', function ($scope, $uibModalInstance, httpService, group) {

    $scope.group = group;

    $scope.nowUser = {};
    $scope.userList = [];

    $scope.queryUser = function (queryParam) {
        var url = "/users?username=" + queryParam + "&page=0&length=10";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.getGroup = function () {
        var url = "/aliyun/mq/group/get?id=" + $scope.group.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.group = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.addGroupUser = function () {
        if ($scope.nowUser.selected == null) return;
        var groupUser = {
            groupId: $scope.group.id,
            userId: $scope.nowUser.selected.id
        }

        var url = "/aliyun/mq/group/user/add";
        httpService.doPostWithJSON(url, groupUser).then(function (data) {
            if (data.success) {
                $scope.getGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.delGroupUser = function (id) {
        var url = "/aliyun/mq/group/user/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.getGroup();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = err;
        });
    }

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
});