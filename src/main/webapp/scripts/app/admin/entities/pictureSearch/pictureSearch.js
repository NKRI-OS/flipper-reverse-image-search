'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adminPictureSearch', {
                parent: 'entity',
                url: '/admin/pictureSearchs',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.pictureSearch.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/pictureSearch/pictureSearchs.html',
                        controller: 'AdminPictureSearchController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureSearch');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adminPictureSearch.detail', {
                parent: 'entity',
                url: '/admin/pictureSearch/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.pictureSearch.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/pictureSearch/pictureSearch-detail.html',
                        controller: 'AdminPictureSearchDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureSearch');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminPictureSearch', function($stateParams, AdminPictureSearch) {
                        return AdminPictureSearch.get({id : $stateParams.id});
                    }]
                }
            });
    });
