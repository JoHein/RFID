'use strict';

angular.module('RFID')
    .controller('livreInfoController',['$rootScope', '$scope', '$http', '$stateParams', '$timeout', 'ReadLivre', 
            function ($rootScope, $scope, $http, $stateParams, $timeout, ReadLivre) {

        ReadLivre.readCard()
            .then(function (response) {
                $scope.infoLivre;
            }, function (error) {
                console.error('Une erreur est survenue : ' + error.toString());
            });

      }]);
