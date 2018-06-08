app.directive('highStock', function (uiLoad) {
    return {
        restrict: 'E',
        template: '<div></div>',
        replace : true,
        scope: {
            options: '='
        },
        link: function (scope, element) {
            uiLoad.load(['vendor/libs/highstock/highstock.js']).then(function() {
                Highcharts.StockChart(element[0], scope.options);
            });
        }
    };
})