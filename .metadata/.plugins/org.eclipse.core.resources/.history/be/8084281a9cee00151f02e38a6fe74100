'use strict';

angular
    .module('RFID', ['ui.router'])
    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('read', {
                url: '/readCard',
                templateUrl: 'jsfrontend/views/readCard.html',
                controller: 'livreInfoController',
                controllerAs :'livreInfo'
            }).
            state('menu', {
            	 url: '/menu',
                 templateUrl: 'jsfrontend/views/menuPrincipal.html',
                 controller: 'menuPrincipalController',
                 controllerAs: 'menuController'
            });
        
        $urlRouterProvider.otherwise('/');
    });