'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adminPictureFound', {
                parent: 'entity',
                url: '/admin/pictureFound/{pictureSearch_id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.pictureFound.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/pictureFound/pictureFound.html',
                        controller: 'AdminPictureFoundByPictureSearchController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureFound');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminPictureFoundByPictureSearch', function($stateParams, AdminPictureFoundByPictureSearch) {
                        return AdminPictureFoundByPictureSearch.query({pictureSearch_id : $stateParams.pictureSearch_id});
                    }]
                }
            })
            .state('adminPictureFound.detail', {
                parent: 'entity',
                url: '/admin/pictureFound/{pictureSearch_id}/{picture_id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.pictureFound.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/pictureFound/pictureFound-detail.html',
                        controller: 'AdminPictureFoundDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureSearch');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminPictureFound', function($stateParams, AdminPictureFound) {
                        return AdminPictureFound.get({pictureSearch_id : $stateParams.pictureSearch_id, picture_id : $stateParams.picture_id});
                    }]
                }
            });
    });
