'use strict';

angular.module('flipperApp')
    .controller('PictureByUserController', function ($scope, $rootScope, $stateParams, entity, PictureByUser) {
        $scope.pictures = [];
        $scope.page = 0;
        $scope.user = $stateParams.owner;

        $scope.loadAll = function(owner) {
            /** Infinity Scroll */
            PictureByUser.query({owner: owner}, function(result) {
                $scope.pictures = result;
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
