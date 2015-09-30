'use strict';

angular.module('flipperApp')
    .controller('PictureFoundDetailController', function ($scope, $rootScope, $stateParams, entity, PictureFound) {
        $scope.pictureFound = entity;

        $scope.load = function (pictureSearch_id, picture_id) {
            PictureFound.get({pictureSearch_id : $stateParams.pictureSearch_id, picture_id : $stateParams.picture_id}, function(result) {
                $scope.pictureFound = result;
            });
        };
        $rootScope.$on('flipperApp:pictureFoundUpdate', function(event, result) {
            $scope.pictureFound = result;
        });
    });
