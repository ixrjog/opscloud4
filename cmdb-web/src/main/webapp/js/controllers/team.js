'use strict';

app.controller('teamCtrl', function ($scope, $state, $uibModal, toaster, httpService) {

    $scope.pageData = [];
    $scope.totalItems = 0;
    $scope.currentPage = 0;
    $scope.pageLength = 10;
    /////////////////////////////////////////////////
    $scope.queryTeamName = "";
    $scope.queryTeamleaderUsername = "";


    $scope.pageChanged = function () {
        $scope.doQuery();
    };

    /////////////////////////////////////////////////

    $scope.doQuery = function () {
        var url = "/team/page?"
            + "teamName=" + $scope.queryTeamName
            + "&teamleaderUsername=" + $scope.queryTeamleaderUsername
            + "&teamType=-1"
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

    $scope.doQuery();


    /////////////////////////////////////////////////////////////

    $scope.addTeam = function () {
        var team = {
            id: 0,
            teamName: "",
            teamType: 0,
            content: "",
            teamleaderUserId: 0,
            teamleaderUsername: ""
        }
        doSaveTeam(team);
    }

    $scope.editTeam = function (team) {
        doSaveTeam(team);
    }

    var doSaveTeam = function (team) {
        var modalInstance = $uibModal.open({
            templateUrl: 'teamInfo',
            controller: 'teamInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                team: function () {
                    return team;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }

    /////////////////////////////////////////////////////////////
    $scope.editTeamuser = function (team) {
        var modalInstance = $uibModal.open({
            templateUrl: 'teamuserInfo',
            controller: 'teamuserInfoInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                team: function () {
                    return team;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }
    /////////////////////////////////////////////////////////////
    $scope.userList = function (id) {
        var modalInstance = $uibModal.open({
            templateUrl: 'keyBoxUserModal',
            controller: 'keyBoxUserInstanceCtrl',
            size: 'lg',
            resolve: {
                httpService: function () {
                    return httpService;
                },
                serverGroupId: function () {
                    return id;
                }
            }
        });

        modalInstance.result.then(function () {
            $scope.doQuery();
        }, function () {
            $scope.doQuery();
        });
    }
});

/**
 * 团队详情
 */
app.controller('teamInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, team) {

    $scope.team = team;

    $scope.nowTeamleader = {};
    $scope.userList = [];

    var init = function () {
        if ($scope.team.id == 0) return;
        $scope.nowTeamleader.selected = $scope.team.leader;
    }

    init();


    $scope.queryUser = function (queryParam) {
        var url = "/users?username=" + queryParam + "&page=0&length=10";
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                var body = data.body;
                $scope.userList = body.data;
            } else {
                toaster.pop("warning", data.msg);
            }
        }, function (err) {
            toaster.pop("error", err);
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

    $scope.reset = function () {
        $scope.team = {
            id: 0,
            teamName: "",
            teamType: 0,
            content: "",
            teamleaderUserId: 0,
            teamleaderUsername: ""
        }
        $scope.nowTeamleader = {};
    }


    $scope.saveTeam = function () {
        var url = "/team/save";

        if ($scope.team.teamName == null || $scope.team.teamName == '') {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必需指定团队名称";
            return;
        }

        if ($scope.nowTeamleader.selected == null) {
            $scope.alert.type = 'warning';
            $scope.alert.msg = "必需指定团队负责人";
            return;
        } else {
            $scope.team.teamleaderUserId = $scope.nowTeamleader.selected.id;
            $scope.team.teamleaderUsername = $scope.nowTeamleader.selected.username;
        }

        httpService.doPostWithJSON(url, $scope.team).then(function (data) {
            if (data.success) {
                $scope.alert.type = 'success';
                $scope.alert.msg = "保存成功！";
                $scope.reset();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'danger';
            $scope.alert.msg = err;
        });
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});

/**
 * 团队成员管理
 */
app.controller('teamuserInfoInstanceCtrl', function ($scope, $uibModalInstance, httpService, team) {

    $scope.team = team;

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

    $scope.getTeam = function () {
        var url = "/team/get?id=" + $scope.team.id;
        httpService.doGet(url).then(function (data) {
            if (data.success) {
                $scope.team = data.body;
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.addTeamuser = function () {
        if ($scope.nowUser.selected == null) return;
        var teamuser = {
            teamId: $scope.team.id,
            userId: $scope.nowUser.selected.id,
            username: $scope.nowUser.selected.username,
            userRole: 0
        }

        var url = "/team/teamuser/save";
        httpService.doPostWithJSON(url, teamuser).then(function (data) {
            if (data.success) {
                $scope.getTeam();
            } else {
                $scope.alert.type = 'warning';
                $scope.alert.msg = data.msg;
            }
        }, function (err) {
            $scope.alert.type = 'error';
            $scope.alert.msg = err;
        });
    }

    $scope.delTeamuser = function (id) {
        var url = "/team/teamuser/del?id=" + id;
        httpService.doDelete(url).then(function (data) {
            if (data.success) {
                $scope.getTeam();
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

    $scope.reset = function () {
        $scope.team = {
            id: 0,
            teamName: "",
            teamType: 0,
            content: "",
            teamleaderUserId: 0,
            teamleaderUsername: ""
        }
        $scope.nowTeamleader = {};
    }

    $scope.closeModal = function () {
        $uibModalInstance.dismiss('cancel');
    }
});