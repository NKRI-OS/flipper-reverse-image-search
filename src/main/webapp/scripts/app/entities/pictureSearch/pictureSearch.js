'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('pictureSearch', {
                parent: 'entity',
                url: '/pictureSearchs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.pictureSearch.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/pictureSearch/pictureSearchs.html',
                        controller: 'PictureSearchController'
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
            .state('picture.search', {
                parent: 'home',
                url: '/search',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/pictureSearch/pictureSearch-dialog.html',
                        controller: 'PictureSearchDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {pictureFile: null};
                            }
                        }
                    }).result.then(function(result) {
                            /* after onSaveFinished from pictureSearch-dialog.controller.js */
                            $state.go('pictureFound', {pictureSearch_id: result.id}, { reload: true });
                        })
                }],
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('pictureSearch');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
