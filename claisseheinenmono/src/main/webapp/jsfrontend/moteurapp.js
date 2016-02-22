'use strict';

angular
    .module('RFID')
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('read', {
                abstract: true,
                url: '/rest/ReadCard',
                templateUrl: 'views/readCard.html',
                controller: 'livreInfoController',
                controllerAs :'livreInfo'
            });
//            .state('register', {
//                url: '/register',
//                templateUrl: 'views/register.html',
//                controller: 'RegisterController'
//            })
//            .state('app', {
//                url: '/app',
//                abstract: true,
//                templateUrl: 'views/app.html',
//                controller: 'AppController'
//            })
//            .state('app.home', {
//                url: '/home',
//                templateUrl: 'views/home.html',
//                controller: 'HomeController',
//                resolve: {
//                    Games: function ($http, $q) {
//                        var deferred = $q.defer();
//
//                        $http.get('/rest/game')
//                            .then(function (data) {
//                                deferred.resolve(data);
//                            }, function (error) {
//                                deferred.reject(error);
//                            });
//
//                        return deferred.promise;
//                    }
//                }
//            })
//            .state('app.start', {
//                url: '/start',
//                templateUrl: 'views/start.html',
//                controller: 'StartController'
//            })
//            .state('app.room', {
//                url: '/room/:name',
//                templateUrl: 'views/room.html',
//                controller: 'RoomController'
//            })
//            .state('app.game', {
//                url: '/game/:name',
//                templateUrl: 'views/game.html',
//                controller: 'GameController'
//            });

        $urlRouterProvider.otherwise('/');
    });