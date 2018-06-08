'use strict';

app.controller('explainCtrl', function($scope, $state, $uibModal, toaster, httpService) {

    $scope.repoList = [];
    $scope.nowRepo = {};

    $scope.queryRepo = function(queryParam) {
        var url = "/explain/repo/query?page=0&length=10&repo=" + queryParam;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.repoList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    ///////////////////////////////////////////////////////
    $scope.addItem = function() {
        var item = {
            id : 0,
            repo : "",
            scanPathList : [""],
            notifyEmailList : [""],
            cdlAppId : "",
            cdlGroup : ""
        }

        saveItem(item);
    }

    $scope.editItem = function(item) {
        var innerItem = {
            id : item.id,
            repo : item.repo,
            scanPathList : item.scanPathList,
            notifyEmailList : item.notifyEmailList,
            cdlAppId : item.cdlAppId,
            cdlGroup : item.cdlGroup
        }

        saveItem(innerItem);
    }

    $scope.delItem = function(id) {
        var url = "/explain/del?id=" + id;

        httpService.doDelete(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "删除成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQuery();
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.invokeItem = function(id) {
        var url = "/explain/repo/scan?id=" + id;

        httpService.doPost(url).then(function(data) {
            if(data.success) {
                toaster.pop("success", "执行成功！");
            } else {
                toaster.pop("warning", data.msg);
            }
            $scope.doQuery();
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    var saveItem = function(item) {
        var modalInstance = $uibModal.open({
            templateUrl: 'explainModal',
            controller: 'explainInstanceCtrl',
            size : 'md',
            resolve: {
                httpService : function() {
                    return httpService;
                },
                item : function() {
                    return item;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function() {
            $scope.doQuery();
        });
    }
    ///////////////////////////////////////////////////////

    /////////////////////////////////////////////////

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;

    $scope.pageChanged = function() {
        $scope.doQuery($scope.currentPage - 1);
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function() {
        var url = "/explain/query?"
            + "repo=" + ($scope.nowRepo.selected == null ? "" : $scope.nowRepo.selected)
            + "&page=" + ($scope.currentPage <= 0 ? 0 : ($scope.currentPage - 1))
            + "&length=" + $scope.pageLength;

        httpService.doGet(url).then(function(data) {
            if(data.success) {
                var body = data.body;
                $scope.pageData = body.data;
                $scope.totalItems = body.size;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function(err) {
            toaster.pop("error", err);
        });
    }

    $scope.doQuery();
});

app.controller('explainInstanceCtrl', function ($scope, $uibModalInstance, httpService, item) {
    $scope.innerItem = {
        id : item.id,
        repo : item.repo,
        scanPathList : item.scanPathList,
        notifyEmailList : item.notifyEmailList,
        cdlAppId : item.cdlAppId,
        cdlGroup : item.cdlGroup
        };

    $scope.alert = null;

    $scope.closeAlert = function() {
        $scope.alert = null;
    }

    /////////////////////////// scanPathList /////////////////////////////

    $scope.addScanPath = function(idx) {
        idx = idx + 1;
        $scope.innerItem.scanPathList.splice(idx, 0, '');
    }

    $scope.delScanPath = function(idx) {
        $scope.innerItem.scanPathList.splice(idx, 1);
        if($scope.innerItem.scanPathList.length == 0) {
            $scope.innerItem.scanPathList.push("");
        }
    }

    /////////////////////////// scanPathList /////////////////////////////

    /////////////////////////// notifyEmailList /////////////////////////////

    $scope.addNotifyEmail = function(idx) {
        idx = idx + 1;
        $scope.innerItem.notifyEmailList.splice(idx, 0, '');
    }

    $scope.delNotifyEmail = function(idx) {
        $scope.innerItem.notifyEmailList.splice(idx, 1);
        if($scope.innerItem.notifyEmailList.length == 0) {
            $scope.innerItem.notifyEmailList.push("");
        }
    }

    /////////////////////////// notifyEmailList /////////////////////////////

    $scope.saveData = function() {
        var alertInfo = {
            type : "",
            msg : ""
        }
        $scope.alert = alertInfo;
        var url = "/explain/add";
        httpService.doPostWithJSON(url, $scope.innerItem).then(function(data) {
            if(data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }

            $scope.innerItem = {
                id : 0,
                repo : "",
                scanPathList : [""],
                notifyEmailList : [""]
            };
        }, function(err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function() {
        $uibModalInstance.dismiss('cancel');
    }
});