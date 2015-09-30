'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('adminPicture', {
                parent: 'entity',
                url: '/admin/pictures',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.picture.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/picture/pictures.html',
                        controller: 'AdminPictureController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('picture');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('adminPicture.detail', {
                parent: 'entity',
                url: '/admin/picture/{id}',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'flipperApp.picture.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/entities/picture/picture-detail.html',
                        controller: 'AdminPictureDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('picture');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'AdminPicture', function($stateParams, AdminPicture) {
                        return AdminPicture.get({id : $stateParams.id});
                    }]
                }
            })
            .state('adminPicture.edit', {
                parent: 'adminPicture',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/admin/entities/picture/picture-update.html',
                        controller: 'AdminPictureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['AdminPicture', function(AdminPicture) {
                                return AdminPicture.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('adminPicture', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
