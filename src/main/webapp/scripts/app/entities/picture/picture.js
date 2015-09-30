'use strict';

angular.module('flipperApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('picture', {
                parent: 'entity',
                url: '/pictures',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.picture.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/picture/pictures.html',
                        controller: 'PictureController'
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
            .state('picture.byUser', {
                parent: 'entity',
                url: '/pictures/{owner}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.picture.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/picture/pictures_by_user.html',
                        controller: 'PictureByUserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('picture');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PictureByUser', function($stateParams, PictureByUser) {
                        return PictureByUser.get({owner : $stateParams.owner});
                    }]
                }
            })
            .state('picture.detail', {
                parent: 'entity',
                url: '/picture/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'flipperApp.picture.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/picture/picture-detail.html',
                        controller: 'PictureDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('picture');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Picture', function($stateParams, Picture) {
                        return Picture.get({id : $stateParams.id});
                    }]
                }
            });
    });
