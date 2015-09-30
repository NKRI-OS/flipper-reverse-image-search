'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pictureFound', {
                parent: 'entity',
                url: '/picturesFound/{pictureSearch_id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.pictureFound.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pictureFound/pictureFound.html',
                        controller: 'PictureFoundController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureFound');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PictureFoundByPictureSearch', function($stateParams, PictureFoundByPictureSearch) {
                        return PictureFoundByPictureSearch.query({pictureSearch_id : $stateParams.pictureSearch_id});
                    }]
                }
            })
            .state('pictureFound.detail', {
                parent: 'entity',
                url: '/pictureFound/{pictureSearch_id}/{picture_id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.pictureFound.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pictureFound/pictureFound-detail.html',
                        controller: 'PictureFoundDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureSearch');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PictureFound', function($stateParams, PictureFound) {
                        return PictureFound.get({pictureSearch_id : $stateParams.pictureSearch_id, picture_id : $stateParams.picture_id});
                    }]
                }
            });
    });
