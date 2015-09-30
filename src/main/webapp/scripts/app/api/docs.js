'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('api', {
                parent: 'site',
                url: '/api',
                data: {
                    authorities: [],
                    pageTitle: 'global.menu.API'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/api/docs.html'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', function ($translate) {
                        return $translate.refresh();
                    }]
                }
            });
    });
