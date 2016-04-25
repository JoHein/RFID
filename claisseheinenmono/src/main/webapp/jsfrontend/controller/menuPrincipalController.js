'use strict';

angular.module('RFID')
    .controller('menuPrincipalController',
        ['$rootScope', '$scope', '$http', '$stateParams', '$timeout', '$location', '$log', '$window',
            function ($rootScope, $scope, $http, $stateParams, $timeout, $location, $log, $window) {
                var vm = this;

                $http.get('/rest/allBorrow')
                    .then(function (data) {
                        $log.debug(data);
                        vm.emprunts = [];
                        vm.emprunts.push(data.data.Emprunt);
                        $log.debug(data.data.Emprunt);
                        $log.debug(vm.emprunts);
                        $log.debug("la reponse");

                    });

                $http.get('/rest/allCat')
                    .then(function (data) {
                        $log.debug(data);
                        vm.oeuvre = [];
                        vm.oeuvre.push(data.data.Livres);
                        $log.debug(data.data.Livres);

                        $log.debug(vm.oeuvre);

                        $log.debug("la reponse");

                        $log.debug(vm.oeuvre[0][0].nomCatalogue);
                        $log.debug(vm.oeuvre[0][1].nomCatalogue);

                    });


                $http.get('/rest/allUser')
                    .then(function (data) {
                        $log.debug(data);
                        vm.users = [];
                        vm.users.push(data.data.Users);
                        $log.debug(data.data.Users);
                        $log.debug(vm.users);
                        $log.debug("la reponse");

                    });


                function ScanCard(callback) {

                    $log.debug("function scan");

                    $http.get('/rest/readCard')

                        .then(function (response) {

                            $log.debug("la reponse readcard");

                            $log.debug(response.data[0]);
                            $log.debug(response);


                            vm.Produit = response.data[0];

                            if (response.data == "") {
                                vm.affichage = "Opération annulée";
                            }
                            callback(vm.Produit);
                        });

                }

                $scope.deleteEntUser = function () {
                    vm.affichage = "Suppression : Présentez une carte User";
                    ScanCard(function (value) {

                        $log.debug("Dans rest");

                        $log.debug(value.uidUser);

                        $log.debug("function delete");
                        var deleteUser = $window.confirm("Are you sure you want to delete this user?");
                        if (deleteUser) {
                            if ((value.uidUser).length == 14) {

                                $http({method: 'delete', url: '/rest/deleteEnt/' + value.uidUser})

                                    .then(function (response) {

                                        $log.debug("la reponse deleteEnt");

                                        $log.debug(response);

                                        vm.deleteEnt = response.data[0].retour;

                                        vm.affichage = vm.deleteEnt + "\n User: " + value.nomUser + " " + value.prenomUser;

                                        return vm.affichage;

                                    })
                            } else {
                                vm.affichage = "Pas de donnée pour cette carte";
                                return vm.affichage;
                            }
                        } else {
                            vm.affichage = "Operation annulée";
                            return vm.affichage;
                        }
                    });

                };

                $scope.deleteEntBook = function () {
                    vm.affichage = "Suppression : Présentez une carte Book";

                    ScanCard(function (value) {

                        $log.debug("Dans rest");

                        $log.debug(value.uidProduit);

                        $log.debug("function delete");
                        var deleteUser = $window.confirm("Are you sure you want to delete this book?");
                        if (deleteUser) {
                            if ((value.uidProduit).length == 8) {

                                $http({method: 'delete', url: '/rest/deleteEnt/' + value.uidProduit})

                                    .then(function (response) {

                                        $log.debug("la reponse deleteEnt");

                                        $log.debug(response);

                                        vm.deleteEnt = response.data[0].retour;

                                        vm.affichage = vm.deleteEnt + "\n Nom livre: " + value.nomCatalogue;

                                        return vm.affichage;

                                    })

                            } else {
                                vm.affichage = "Pas de donnée pour cette carte";
                                return vm.affichage;
                            }
                        } else {
                            vm.affichage = "Operation annulée";
                            return vm.affichage;
                        }
                    });

                };


                $scope.addEntity = function (data) {
                    vm.affichage = "Ajout : Présentez une carte";

                    ScanCard(function (value) {
                        $scope.data = {};

                        $log.debug("Dans ScanENTADD");
                        $log.debug(data);
                        $log.debug(value);


                        if (value.idStock | value.idUser) {
                            vm.affichage = "Ajout : La carte existe déjà";
                            return vm.affichage;
                        } else {

                            if ((value.uidNew).length > 10) {
                                data.uid = value.uidNew;

                                $http({
                                    method: 'post',
                                    url: '/rest/addEnt/user/' + data.uid + '/' + data.nom + '/' + data.prenom
                                })

                                    .then(function (response) {

                                        $log.debug("la reponse addEntUser");

                                        $log.debug(response);


                                        vm.affichage = response.data[0].retour;

                                        return vm.affichage;

                                    })
                            } else if ((value.uidNew).length < 10) {
                                $log.debug("Else if");

                                data.uid = value.uidNew;
                                data.dispo = 1;

                                $log.debug("data apres ajout");

                                $log.debug(data);

                                $http({
                                    method: 'post',
                                    url: '/rest/addEnt/product/' + data.uid + '/' + data.idCatalogue + '/1'
                                })

                                    .then(function (response) {

                                        $log.debug("la reponse addentBook");

                                        $log.debug(response);
                                        $log.debug(response.data[0]);

                                        $log.debug(response.data[0].retour);

                                        vm.affichage = response.data[0].retour;

                                        return vm.affichage;

                                    })
                            } else {
                                vm.affichage = "Erreur de carte";
                                return vm.affichage;
                            }
                        }
                    });

                };

                $scope.emprunt = function () {
                    vm.affichage = "Emprunt : Présentez carte utilisateur";

                    $http.get('/rest/readCard')
                        .then(function (response) {
                            $log.debug("la reponse 1");
                            $log.debug(response);

                            $log.debug(response.data[0]);

                            vm.UserInfo = response.data[0];
                            var nextEpisode = $window.confirm("Emprunt pour User: " + vm.UserInfo.nomUser + " " + vm.UserInfo.prenomUser + "?");


                            if (nextEpisode) {
                                vm.affichage = "Emprunt : Présentez livre";

                                $http.get('/rest/readCard')
                                    .then(function (response2) {
                                        $log.debug("la reponse 2");
                                        $log.debug(response2);

                                        vm.cardProd = response2.data[0];

                                        var nextFinalEpisode = $window.confirm("Validez emprunt pour User: " + vm.UserInfo.nomUser + " " + vm.UserInfo.prenomUser + "\n Livre: " + vm.cardProd.nomCatalogue + "?");
                                        if (nextFinalEpisode) {
                                            $http.post('/rest/manageBorrow/borrow/' + vm.UserInfo.uidUser + '/' + vm.cardProd.uidProduit)

                                                .then(function (response) {

                                                    $log.debug("la reponse emprunt");

                                                    $log.debug(response);
                                                    vm.affichage = "Emprunt enregistré";

                                                })

                                        }
                                    })
                            }

                        })
                };

                $scope.retour = function () {
                    vm.affichage = "Retour : Présentez carte utilisateur";

                    $http.get('/rest/readCard')
                        .then(function (response) {
                            $log.debug("la reponse 1");
                            $log.debug(response);

                            $log.debug(response.data[0]);

                            vm.UserInfo = response.data[0];
                            var nextEpisode = $window.confirm("Retour pour User: " + vm.UserInfo.nomUser + " " + vm.UserInfo.prenomUser + "?");


                            if (nextEpisode) {
                                vm.affichage = "Retour : Présentez livre";

                                $http.get('/rest/readCard')
                                    .then(function (response2) {
                                        $log.debug("la reponse 2");
                                        $log.debug(response2);

                                        vm.cardProd = response2.data[0];

                                        var nextFinalEpisode = $window.confirm("Validez retour pour User: " + vm.UserInfo.nomUser + " " + vm.UserInfo.prenomUser + "\n Livre: " + vm.cardProd.nomCatalogue + "?");
                                        if (nextFinalEpisode) {
                                            $http.post('/rest/manageBorrow/return/' + vm.UserInfo.uidUser + '/' + vm.cardProd.uidProduit)

                                                .then(function (response) {

                                                    $log.debug("la reponse retour");

                                                    $log.debug(response);
                                                    vm.affichage = "Retour enregistré";

                                                })

                                        }
                                    })
                            }

                        })
                };


                $scope.getSearchBook = function (searchBook) {
                    $log.debug("SearchData : " + searchBook);

                    return $http.get('/rest/titleFind', {
                        params: {
                            search: searchBook
                        }
                    }).then(function (response) {
                        $log.debug("Response search : " + response.data.Livres[0].nomCatalogue);
                        $log.debug(response);

                        return response.data.Livres;
                    });
                };


                /*********************************************************************************************************************************************************/

                $scope.addOeuvre = function (data) {
                    $scope.data = {};

                    $http.post('/rest/manageCat/Création/0/' + data.nomCatalogue + '/' + data.auteur + '/' + data.type + '/' + data.categorie + '')
                        .then(function (response) {
                            $log.debug("la reponse addOeuvre");
                            $log.debug(response);
                            vm.affichage = "Ajout Oeuvre : " + data.nomCatalogue + "\n" + response.data[0].retour;

                            /*
                             * ajouter a vm.oeuvre l'ajout
                             */
                            vm.oeuvre[0].push(data);

                            var nbDispo = 0;
                            data.nbDispo = nbDispo;

                            $log.debug(data);

                            $log.debug(vm.oeuvre);

                        });

                }

                $scope.deleteOeuvre = function (data) {
                    var deleteOeuvre = $window.confirm("Êtes-vous sur de vouloir supprimer l'oeuvre : " + data.nomCatalogue + "?");
                    if (deleteOeuvre) {
                        $http.post('/rest/manageCat/Suppression/' + data.idCatalogue + '/' + data.nomCatalogue + '/' + data.auteur + '/' + data.type + '/' + data.categorie + '')
                            .then(function (response) {
                                $log.debug("la reponse deleteOeuvre");
                                $log.debug(response);
                                vm.affichage = "Suppresion Oeuvre : " + data.nomCatalogue + " " +
                                    "" + response.data[0].retour;

                                /*
                                 * Suppresion dans vm.oeuvre l'array affiché de l'oeuvre
                                 */

                                $log.debug(data);

                                $log.debug(vm.oeuvre);

                            });
                    } else {
                        vm.affichage = "Suppresion Oeuvre : Annulée";
                    }
                }


            }]);
