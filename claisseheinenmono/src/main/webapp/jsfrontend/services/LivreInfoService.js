'use strict';

angular.module('RFID')
    .service('ReadLivre', function ($http) {
        return {
            readCard: function () {
                $http.get('/rest/readCard')
                    .then(function (response) {
                        console.log(response);
                        return response;
                    });
            }
        };
    });