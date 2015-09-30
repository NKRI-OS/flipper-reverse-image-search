'use strict';

angular.module('flipperApp')
    .controller('PictureByUserController', function ($scope, $rootScope, $stateParams, entity, PictureByUser, ParseLinks) {
        $scope.pictures = [];
        $scope.page = 0;
        $scope.user = $stateParams.owner;

        $scope.loadAll = function(owner) {
            /** Infinity Scroll */
            PictureByUser.query({owner: owner, page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.pictures.push(result[i]);
                }
            });
        };

        $rootScope.$on('flipperApp:pictureUpdate', function(event, result) {
            $scope.pictures = result;
        });

        $scope.loadAll($stateParams.owner);

        $scope.reset = function() {
            $scope.page = 0;
            $scope.pictures = [];
            $scope.loadAll($stateParams.owner);
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll($stateParams.owner);
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.picture = {title: null, description: null, pictureFile: null, owner: null, modifiedBy: null, favourites: null, likes: null, created: null, modified: null, id: null};
        };
    });
