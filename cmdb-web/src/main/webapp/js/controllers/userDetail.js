'use strict';

app.controller('userCtrl', function($scope, $uibModal, $localStorage, $state, toaster, httpService) {
    $scope.authPoint = $state.current.data.authPoint;

    $scope.userDetail = {};

    $scope.queryName = "";

 

    var init = function(username) {
        var url = "/user/query?username=" + username;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                $scope.userDetail = data.body;
                $scope.userDetail.vPwd = "******************";
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    init($localStorage.settings.user.username);

    $scope.queryUserInfo = function() {
        if($scope.queryName == '') {
            toaster.pop("warning", "查询用户名不为空!");
            return;
        }
        init($scope.queryName);
    }


    $scope.viewPwd = function() {
        $scope.userDetail.vPwd = $scope.userDetail.pwd ;
    }
    
    
    $scope.saveUserInfo = function() {
        //if($scope.userDetail.mobile == '') {
        //    toaster.pop("warning", "请补充电话信息!");
        //    return;
        //}
        var requestBody = {
            username : $scope.userDetail.username,
            mobile : $scope.userDetail.mobile,
            pwd : $scope.userDetail.pwd,
            rsaKey : $scope.userDetail.rsaKey
        }

        var url = "/user/save";

        httpService.doPostWithJSON(url, requestBody).then(function(data) {
            if(data.success) {
                toaster.pop("success", "更新成功!");
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }
});