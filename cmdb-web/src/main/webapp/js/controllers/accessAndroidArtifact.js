'use strict';

app.controller('accessAndroidJobCtrl', function ($scope, $location, $sce, $uibModal, $state, toaster, httpService) {
    //$scope.authPoint = $state.current.data.authPoint;

    $scope.buildDetail = {};
    $scope.md = {};

    $scope.mdEditing = false;

    $scope.editMD = function () {
        $scope.mdEditing = true;
    }

    var doQuery = function () {
        var url = "/ci/build/get?buildId=" + $location.search().buildId;

        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.buildDetail = data.body;
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    doQuery();

    $scope.androidArtifactHelp = "";

    var initReadmeMD = function () {

        marked.setOptions({
            gfm: true,
            tables: true,
            breaks: false,
            pedantic: false,
            sanitize: false,
            smartLists: true,
            smartypants: false,
            highlight: function (code) {
                return hljs.highlightAuto(code).value;
            }
        });

        var url = "/readmeMD/get?mdKey=ANDROID_CI_README";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.androidArtifactHelp = $sce.trustAsHtml(marked($scope.md.mdBody));
            } else {
                $scope.alert.type = "warning";
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = "error";
            $scope.alert.msg = err;
        });
    }

    initReadmeMD();

    $scope.aceOption = {
        useWrapMode: true,
        mode: 'markdown'
    };

    $scope.editorOptions = {
        lineWrapping: true,
        lineNumbers: true,
        readOnly: 'nocursor'
    };

    $scope.saveMD = function () {
        var url = "/readmeMD/save";
        httpService.doPostWithJSON(url, $scope.md).then(function (data) {
            if (data.success) {
                initReadmeMD();
            } else {

            }
        }, function (err) {
        });
        $scope.mdEditing = false;
    }

    $scope.goBack = function () {
        $state.go('app.dashboard');
    }

});