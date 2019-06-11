'use strict';


app.controller('grafanaCtrl', function ($scope, $state, $uibModal, $sce, toaster, $localStorage, httpService) {


    $scope.user = $localStorage.settings.user;

    $scope.grafanaHelp = "";

    $scope.mdEditing = false;

    $scope.editMD = function () {
        $scope.mdEditing = true;
    }


    $scope.md = {};
    var initMD = function () {

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
        var mdKey = "GRAFANA_README";
        var url = "/readmeMD/get?mdKey=" + mdKey;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.md = data.body;
                $scope.md.preview = $sce.trustAsHtml(marked($scope.md.preview));
            }
        }, function (err) {
            return {};
        });
    }
    initMD();


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
                initMD();
            } else {

            }
        }, function (err) {
        });
        $scope.mdEditing = false;
    }


});





