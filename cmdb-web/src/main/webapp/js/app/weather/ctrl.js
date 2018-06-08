app.controller("WeatherCtrl", ['$scope', 'yahooApi', 'geoApi', function($scope, yahooApi, geoApi) {
    $scope.userSearchText = '';
    $scope.search = {};
    $scope.forcast = {};
    $scope.place = {};
    $scope.data = {};

    // get place
    geoApi.then(function(res) {
      $scope.userSearchText = res.data.city+", "+res.data.country_code;
      $scope.getLocations();
    });
    
    // get locations
    $scope.getLocations = function () {
      var query = 'select * from geo.places where text="' + $scope.userSearchText + '"';
      yahooApi.query({'q':query,'format':'json'}, {}, function (response) {
        $scope.search = response;
        if(response.query.count === 1 && !response.query.results.channel){
          $scope.getWeather( response.query.results.place.woeid, response.query.results.place.name, response.query.results.place.country.content);
        }
      });
    };

    // get weather
    $scope.getWeather = function(woeid, city, country){
      $scope.place.city = city;
      $scope.place.country = country;
      var query = 'select item from weather.forecast where woeid=' + woeid;
      yahooApi.query({'q':query,'format':'json'}, {}, function (response) {
        response.query.results.channel.item.forecast.forEach(function(i, v) {
          i.icon = $scope.getCustomIcon(i.code);
        });
        response.query.results.channel.item.condition.icon = $scope.getCustomIcon(response.query.results.channel.item.condition.code);
        $scope.data = response;
      });
    };

    $scope.getCustomIcon = function (condition) {
      switch (condition) {
        case "0":
        case "1":
        case "2":
        case "24":
        case "25":
            return "wind";
        case "5":
        case "6":
        case "7":        
        case "18":        
            return "sleet";
        case "3":
        case "4":
        case "8":        
        case "9":
        case "10":
        case "11":
        case "12":
        case "35":
        case "37":
        case "38":
        case "39":        
        case "40":
        case "45":
        case "47":
            return "rain";
        case "13":
        case "14":
        case "15":
        case "16":
        case "17":
        case "41":
        case "42":
        case "43":
        case "46":
            return "snow";
        case "19":
        case "20":
        case "21":
        case "22":
        case "23":
            return "fog";        
        case "26":
        case "27":
        case "28":
        case "44":
            return "cloudy";
        case "29":
            return "partly-cloudy-night";
        case "30":
            return "partly-cloudy-day";
        case "31":
        case "33":
            return "clear-night";
        case "32":
        case "34":
        case "36":
            return "clear-day";
        default:
            return "";
      }
    }
  }
]);

app.factory('yahooApi', ['$resource', function($resource) {
  return $resource('http://query.yahooapis.com/v1/public/yql', {}, 
    {'query': 
      {
        method: 'GET', 
        isArray: false
      }
    }
  );
}]);

app.factory('geoApi', ['$http', function($http) {
    return $http.jsonp("http://muslimsalat.com/daily.json?callback=JSON_CALLBACK");
  }
]);

function JSON_CALLBACK(){
  // Nothing
}