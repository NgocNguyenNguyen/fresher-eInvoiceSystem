angular.module('dbApp').factory('ReportService', ['$http', '$q', function($http, $q){   
    var factory = {
    	fetchAllReport : fetchAllReport,
    	fetchExpensesReport : fetchExpensesReport,
    };

    return factory;
    
    //Report for chart
    function fetchAllReport(start, end) {
        return $http.get(BASE_URL + "user/getReport/start="+ start + "&end=" + end)
            .then(function (response) {
                return response.data;              
            }
        );
    }
    
    //Report for expenses report
    function fetchExpensesReport(start, end, idType) {
        return $http.get(BASE_URL + "user/getExpensesReport/start="+ start + "&end=" + end + "&type=" + idType)
            .then(function (response) {
                return response.data;              
            }
        );
    }
       
}]);
    