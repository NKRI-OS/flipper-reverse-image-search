'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('mypicture', {
                parent: 'entity',
                url: '/mypictures',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.global.menu.mypictures'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mypicture/mypictures.html',
                        controller: 'MyPictureController'
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
            .state('mypicture.detail', {
                parent: 'entity',
                url: '/mypicture/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.picture.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/mypicture/mypicture-detail.html',
                        controller: 'MyPictureDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('picture');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'MyPicture', function($stateParams, MyPicture) {
                        return MyPicture.get({id : $stateParams.id});
                    }]
                }
            })
            .state('mypicture.new', {
                parent: 'mypicture',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mypicture/mypicture-dialog.html',
                        controller: 'MyPictureDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {title: null, description: null, pictureFile: null, owner: null, modifiedBy: null, favourites: null, likes: null, created: null, modified: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('mypicture', null, { reload: true });
                    }, function() {
                        $state.go('mypicture');
                    })
                }]
            })
            .state('mypicture.edit', {
                parent: 'mypicture',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/mypicture/mypicture-update.html',
                        controller: 'MyPictureUpdateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MyPicture', function(MyPicture) {
                                return MyPicture.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('mypicture', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
