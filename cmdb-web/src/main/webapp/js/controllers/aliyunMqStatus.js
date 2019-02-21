'use strict';

app.controller('aliyunMqStatusCtrl', function ($scope, $state, $uibModal, httpService, toaster, staticModel) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.consumer = {};


    $scope.consumerQueryName = "";
    $scope.topicList = [];

    $scope.subscribeList = [];


    $scope.butSearchSubscribe = false;

    // {{consumer.lastTimestamp}}
    var initGmtLastTime = function () {
        $scope.consumer.gmtLastTime = formatTimestamp($scope.consumer.lastTimestamp);

        // for (var i = 0; i < $scope.topicList.length; i++) {
        //     $scope.topicList[i].gmtCreate = formatTimestamp($scope.topicList[i].createTime)
        //     $scope.topicList[i].gmtModify = formatTimestamp($scope.topicList[i].updateTime)
        // }
    }


    $scope.queryConsumer = function () {
        if ($scope.consumerQueryName == null || $scope.consumerQueryName == '')
            return;
        $scope.consumer.cid = ''
        var url = "/aliyun/mq/consumer/status?cid=" + $scope.consumerQueryName;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.consumer = body;
                $scope.consumer.cid = $scope.consumerQueryName;
                initGmtLastTime();
                // initTopic();
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
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

