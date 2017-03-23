'use strict';
angular.module('myApp').factory('RegisterService', ['$http', '$q', function($http, $q){
    var factory = {        
    	register: register        
    };

    return factory;

   // Service call api register user
    function register(res) {
        var deferred = $q.defer();
        $http.post(BASE_URL + "user/register", res)
            .then(
            function (response) {
                deferred.resolve(response.data);
            },
            function(errResponse){
                console.error('Error while creating User');
                deferred.reject(errResponse);
            }
        );
        return deferred.promise;
    }

}]);
