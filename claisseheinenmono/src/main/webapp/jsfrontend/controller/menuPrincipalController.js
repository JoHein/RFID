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
   			 
//    			 function ScanCard() {
//    				 $log.debug("function scan");
//    				 $http.get('/rest/readCard')
//    		          .then(function (response) {
//    		              $log.debug("la reponse readcard");
//    		              $log.debug(response.data[0].uidProduit);
//    		              vm.uidProduit= response.data[0].uidProduit;
////    		              return response;
//    		          })
//    			 };
//    			 
//    			 $scope.deleteEnt = function(){
//    				 ScanCard();
//    			 }, function (value){   					 
//
//    				 			$log.debug("Dans rest");
//    		
//    		    				 $log.debug(value);
//    		    				 $log.debug("function delete");
//    		    				 $http.post('rest/deleteEnt/'+value)
//    		    		          .then(function (response) {
//    		    		              $log.debug("la reponse deleteEnt");
//    		    		              $log.debug(response);
//    		    		              vm.deleteEnt = response;
//    		    		              return vm.deleteEnt;
//    		    		          });
//    		    			
//    			 };
//    			


    			 function ScanCard(callback) {

    			     $log.debug("function scan");

    			     $http.get('/rest/readCard')

    			         .then(function (response) {

    			             $log.debug("la reponse readcard");

    			             $log.debug(response.data[0]);

    			             vm.Produit= response.data[0];

    			             callback(vm.Produit);

    			         });

    			 }

    			 $scope.deleteEnt = function(){

    			     ScanCard(function(value){

    			         $log.debug("Dans rest");

    			         $log.debug(value.uidProduit);

    			         $log.debug("function delete");
    			         

    			         $http({method:'delete',url:'/rest/deleteEnt/'+value.uidProduit})
    			         
    			         .then(function (response) {

    			                 $log.debug("la reponse deleteEnt");

    			                 $log.debug(response);

    			                 vm.deleteEnt = response;

    			                 return vm.deleteEnt;

    			             })

    			     });

    			 };



    			 
    }]);
