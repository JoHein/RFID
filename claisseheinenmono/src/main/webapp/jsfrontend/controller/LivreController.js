'use strict';

angular.module('RFID')
    .controller('livreInfoController',['$rootScope', '$scope', '$http', '$stateParams', '$timeout', 'ReadLivre', 
            function ($rootScope, $scope, $http, $stateParams, $timeout, ReadLivre) {

    	  $http.get('/rest/readCard')
          .then(function (response) {
              console.log(response);
              return response;
          });

      }]);
