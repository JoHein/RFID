'use strict';

angular.module('RFID')
    .controller('menuPrincipalController', 
    		['$rootScope', '$scope', '$http', '$stateParams', '$timeout','$location', '$log',
            function ($rootScope, $scope, $http, $stateParams, $timeout,$location, $log) {
    				var vm = this;
    			
    			 $http.get('/rest/allCat')
		          .then(function (data) {
		              $log.debug(data);
		              vm.oeuvre= data.data.Livres;
		              $log.debug(data.data.Livres);

		              
		              $log.debug("la reponse");

		              $log.debug(vm.oeuvre[0].nomCatalogue);
		          });
   			 
    			 	 function ScanCard() {
    				 $log.debug("function scan");
    				 $http.get('/rest/readCard')
    		          .then(function (response) {
    		              $log.debug("la reponse readcard");
    		              $log.debug(response);
    		              return response;
    		          });
    			 };
    			 
    			 $scope.deleteEnt = function() {
    				 /*
    				  * function lecture de carte 
    				  */
    				vm.uidCard= ScanCard();
    				 
    				/*
    				 * function delete
    				 */
    			
    				 $log.debug("function delete");
    				 $http.post('/deleteEnt/'+vm.uidCard)
    		          .then(function (response) {
    		              $log.debug("la reponse deleteEnt");
    		              $log.debug(response);
    		              vm.deleteEnt = response;
    		              return vm.deleteEnt;
    		          });
    			 };
    			 
    }]);
