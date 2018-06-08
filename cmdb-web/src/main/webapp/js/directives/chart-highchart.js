app.directive('highChart', function (uiLoad) {
    return {
        restrict: 'E',
        template: '<div></div>',
        replace : true,
        scope: {
            options : '='
        },
        link: function (scope, element) {
            uiLoad.load(['vendor/libs/highchart/highcharts.js']).then(function() {
                Highcharts.chart(element[0], scope.options);

                scope.$watch('options', function(newValue, oldValue) {
                    Highcharts.chart(element[0], scope.options);
                }, true);
            });
        }
    };
})