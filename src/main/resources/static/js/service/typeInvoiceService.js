'use strict';

angular.module('dbApp').factory('TypeInvoiceService', ['$http', '$q', function($http, $q){
    var factory = {
    	fetchAll : fetchAllType,   
    	fetchAllService: fetchAllService,
    	getService: getService,
    };

    return factory;
    

    // Service call api get all type invoice
    function fetchAllType() {
    	return $http.get(BASE_URL + "invoice/getTypeAll").then(function (response) {
                return response.data;                
        })
    } 
    
    // Service call api get all service by id type invoice
    function fetchAllService(id) {
        var deferred = $q.defer();
        $http.get(BASE_URL + "invoice/getAllService/" + id)
            .then(
            function (response) {
            	console.log("fetchAllService" + response.data);
                deferred.resolve(response.data);                
            },
            function(errResponse){
                console.error('Error while fetching fetchAllService');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }
    
    //Service call api get service by id
    function getService(name, id) {
        var deferred = $q.defer();
        $http.get(BASE_URL + "invoice/getService/name=" + name + "&id=" + id)
            .then(
            function (response) {
            	console.log("getService" + response.data);
                deferred.resolve(response.data);                
            },
            function(errResponse){
                console.error('Error while fetching getService');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }          
}]);


