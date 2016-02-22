'use strict';

angular.module('RFID')
    .service('ReadLivre', function (localStorageService, $http, $q) {
        return {
            readCard: function () {
                var deferred = $q.defer();
                // TODO voir la route pour le register et la méthode
                $http.get('/rest/ReadCard/', {})
                    .then(function (response) {
                        console.log(response);
                        deferred.resolve(response);
                    }, function (error) {
                        console.log(error);
                        deferred.reject(error);
                    });
                return deferred.promise;
            }
        };
    });