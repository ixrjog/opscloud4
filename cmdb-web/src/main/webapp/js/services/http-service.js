app.factory('httpService', function($http, $q, $localStorage, $state, $websocket, $location) {
    var service = {
        //domain : "http://localhost:8080/cmdb",
        //wsdomain : "ws://localhost:8080/cmdb/keybox/ws",
        domain : ".",
        wsdomain : ($location.protocol() == 'http' ? "ws://" : "wss://") + $location.host() + ":" + $location.port() + "/keybox/ws",

        doGet : function(url) {
            var deferred = $q.defer();
            $http.get(service.domain + url, {
                headers : {
                    token : $localStorage.settings.user.token
                }
            }).success(function(data) {
                var success = data.success;
                var code = data.code;
                if(!success && (code == '0002' || code == '0003' || code == '0004')) {
                    $state.go("access.signin");
                }

                deferred.resolve(data);
            }).error(function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        },
        doPost : function(url) {
            var deferred = $q.defer();
            $http.post(service.domain + url, null, {
                headers : {
                    token : $localStorage.settings.user.token
                }
            }).success(function(data) {
                var success = data.success;
                var code = data.code;
                if(!success && (code == '0002' || code == '0003' || code == '0004')) {
                    $state.go("access.signin");
                }

                deferred.resolve(data);
            }).error(function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        },
        doDelete : function(url) {
            var deferred = $q.defer();
            $http.delete(service.domain + url, {
                headers : {
                    token : $localStorage.settings.user.token
                }
            }).success(function(data) {
                var success = data.success;
                var code = data.code;
                if(!success && (code == '0002' || code == '0003' || code == '0004')) {
                    $state.go("access.signin");
                }

                deferred.resolve(data);
            }).error(function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        },
        doPostWithJSON : function(url, body) {
            var deferred = $q.defer();
            $http.post(service.domain + url, body, {
                headers : {
                    token : $localStorage.settings.user.token
                }
            }).success(function(data) {
                var success = data.success;
                var code = data.code;
                if(!success && (code == '0002' || code == '0003' || code == '0004')) {
                    $state.go("access.signin");
                }

                deferred.resolve(data);
            }).error(function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        },
        doPostWithForm : function(url, body) {
            var deferred = $q.defer();
            $http({
                method : 'POST',
                url : service.domain + url,
                data : body,
                transformRequest: function(obj) {
                    var str = [];
                    for(var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                },
                headers : {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'token' : $localStorage.settings.user.token
                }
            }).success(function(data) {
                var success = data.success;
                var code = data.code;
                if(!success && (code == '0002' || code == '0003' || code == '0004')) {
                    $state.go("access.signin");
                }

                deferred.resolve(data);
            }).error(function(err) {
                deferred.reject(err);
            });
            return deferred.promise;
        },
        doAuthCheck : function(checkUrl, authGroup) {
            var checkResult = {
                status : false,
                code : "0000",
                body : {}
            }

            var url;
            if(authGroup == '') {
                url = service.domain + "/check/authV2?checkUrl=" + checkUrl;
            } else {
                url = service.domain + "/check/authV2?checkUrl=" + checkUrl + "&authGroup=" + authGroup;
            }

            var request;
            if (window.XMLHttpRequest) {
                request = new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                request = new ActiveXObject("Microsoft.XMLHTTP");
            } else {
                throw new Error("Your browser don't support XMLHttpRequest");
            }

            request.withCredentials = true;
            try {
                request.open('GET', url, false);
                request.setRequestHeader("token", $localStorage.settings.user.token);
                request.send(null);

                if (request.status === 200) {
                    var data = JSON.parse(request.responseText);
                    if(data.success) {
                        checkResult.status = true;
                        checkResult.body = data.body;
                        checkResult.code = data.code;
                    } else {
                        checkResult.status = false;
                        checkResult.code = data.code;
                    }
                } else {
                    checkResult.status = false;
                    checkResult.code = "9999";

                }
            } catch (err) {
                checkResult.status = false;
                checkResult.code = "9999";
                console.log(err);
            }
            return checkResult;
        },
        wsInstance : function() {
            return $websocket(service.wsdomain + "?token=" + $localStorage.settings.user.token);
        }
    }

    return service;
});