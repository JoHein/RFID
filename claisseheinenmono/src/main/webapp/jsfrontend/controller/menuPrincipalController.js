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
    			         
    			         if(value.idStock!=0){

    			         $http({method:'delete',url:'/rest/deleteEnt/'+value.uidProduit})
    			         
    			         .then(function (response) {

    			                 $log.debug("la reponse deleteEnt");

    			                 $log.debug(response);

    			                 vm.deleteEnt = response.data[0].retour;
    			                 
    			                 vm.affichage= vm.deleteEnt+"\n Nom livre: "+value.nomCatalogue;
    			         
    			                 return vm.affichage;

    			             })
    			         }else{
    			        	 vm.affichage= "Pas de donnée pour cette carte";
    			        	 return  vm.affichage;
    			         }
    			     });
    			 
    			 };



    			 
    }]);
