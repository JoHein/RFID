'use strict';

angular.module('RFID')
    .controller('menuPrincipalController', 
    		['$rootScope', '$scope', '$http', '$stateParams', '$timeout','$location', '$log',
            function ($rootScope, $scope, $http, $stateParams, $timeout,$location, $log) {
    				var vm = this;
    			
    			 $http.get('/rest/allCat')
		          .then(function (data) {
		              $log.debug(data);
		              $log.debug("la reponse");
		              vm.oeuvre= data.data;
		              $log.debug(vm.oeuvre);
		          });
   			 
    			 $scope.scanBook = function() {
    				 $log.debug("function scan");
    				 $http.get('/rest/readCard')
    		          .then(function (response) {
    		              $log.debug(response);
    		              $log.debug("la reponse");
    		              return response;
    		          });
    			 };
    			 
    }]);
