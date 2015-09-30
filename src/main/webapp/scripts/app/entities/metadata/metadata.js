'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('metadata', {
                parent: 'entity',
                url: '/metadatas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.metadata.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metadata/metadatas.html',
                        controller: 'MetadataController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metadata');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('metadata.byPicture', {
                parent: 'entity',
                url: '/metadatas/{picture_id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.metadata.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metadata/metadatas_by_picture.html',
                        controller: 'MetadataByPictureController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metadata');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MetadataByPicture', function($stateParams, MetadataByPicture) {
                        return MetadataByPicture.query({picture_id : $stateParams.picture_id});
                    }]
                }
            })
            .state('metadata.detail', {
                parent: 'entity',
                url: '/metadata/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.metadata.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/metadata/metadata-detail.html',
                        controller: 'MetadataDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metadata');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Metadata', function($stateParams, Metadata) {
                        return Metadata.get({id : $stateParams.id});
                    }]
                }
            });
    });
