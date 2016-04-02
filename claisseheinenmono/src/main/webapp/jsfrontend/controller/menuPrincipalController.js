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
    			             $log.debug(response);

    			             if(response){
    			            	 
    			             }
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

    			 
    			 $scope.addEntity = function(data){
    				    				 
    				 ScanCard(function(value){
        				 $scope.data= {};

    			         $log.debug("Dans ScanENTADD");
    			         $log.debug(data);

    			         $log.debug(value.uidProduit);
    			         
    			         $log.debug((value.uidProduit).length);
    			         $log.debug((value.uidProduit).length>10);
    			         $log.debug((value.uidProduit).length<10);


//    			         if((value.uidProduit).length>10){
//    			        	 user.uid =value.uidProduit;
//
//    			         $http({method:'post',url:'/rest/addEnt/user/'+user})
//    			         
//    			         .then(function (response) {
//
//    			                 $log.debug("la reponse addEntUser");
//
//    			                 $log.debug(response);
//
//    			                 vm.deleteEnt = response.data[0].retour;
//    			                 
//    			                 vm.affichage= user.nom+"\n"+user.prenom+" a été ajouté(e)";
//    			         
//    			                 return vm.affichage;
//
//    			             })
//    			         }else if(value.uidProduit<10){
    			         	$log.debug("Else if");

    			        	 data.uid =value.uidProduit;
    			        	 data.dispo =1;
    			        	 
    	    				 $log.debug("data apres ajout");

    	    				 $log.debug(data);
    	    				var json= JSON.stringify(data);
   	    				 $log.debug(json);

        			         $http({method:'post',url:'/rest/addEnt/product/'+data.uid+'/'+data.idCatalogue+'/1', headers: {'Content-Type': 'application/json'}})
        			         
        			         .then(function (response) {

        			                 $log.debug("la reponse addentBook");

        			                 $log.debug(response);

        			                 $log.debug(response.data[0].retour);
;
        			                 
        			                 vm.affichage= response.data[0].titre+"\n a été ajouté";
        			         
        			                 return vm.affichage;

        			             })
//    			         }else{
//    			        	 vm.affichage = "Erreur de carte";
//    			        	 return vm.affichage;
//    			         }
    			     });
    				 
    				 /*
    				  * faire le scan de carte
    				  * $window
    				  * pour valider la creation avant le rentrage en BDD
    				  * 
    				  * Message de confirmation dans vm.affichage;
    				  */
    				 
    			 };
    			 

    			 
    }]);
