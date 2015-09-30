'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adminMetadata', {
                parent: 'entity',
                url: '/admin/metadatas',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.metadata.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/metadata/metadatas.html',
                        controller: 'AdminMetadataController'
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
            .state('adminMetadata.detail', {
                parent: 'entity',
                url: '/admin/metadata/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.metadata.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/metadata/metadata-detail.html',
                        controller: 'AdminMetadataDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('metadata');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminMetadata', function($stateParams, AdminMetadata) {
                        return AdminMetadata.get({id : $stateParams.id});
                    }]
                }
            });
    });
