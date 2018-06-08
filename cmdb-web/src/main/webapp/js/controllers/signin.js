'use strict';

/* Controllers */
// signin controller
app.controller('loginController', function ($scope, httpService, $state) {

    $scope.btnDoLogin = false;

    $scope.user = {
        userName: "",
        pwd: ""
    };
    $scope.authError = null;

    var logout = function (username) {
        var url = "/logout?username=" + username;

        httpService.doPost(url);
    }

    if ($scope.app.settings.user != null && $scope.app.settings.user.username != null && $scope.app.settings.user.username != '') {
        logout($scope.app.settings.user.username);
    }

    //重置登录信息
    $scope.app.settings.user = {};

    $scope.login = function () {

        $scope.authError = null;

        if ($scope.user.userName == '' || $scope.user.pwd == '') {
            return;
        }
        $scope.btnDoLogin = true;
        var url = "/login";

        httpService.doPostWithForm(url, $scope.user).then(function (data) {

            if (data.success) {
                var body = data.body;
                $scope.app.settings.user = body;
                $state.go('app.dashboard');
                $scope.btnDoLogin = false;
            } else {
                $scope.authError = data.msg;
                $scope.btnDoLogin = false;
            }
        }), function (err) {
            $scope.authError = 'Server Error';
            $scope.btnDoLogin = false;
        };
    };
});