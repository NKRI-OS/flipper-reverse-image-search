'use strict';

angular.module('flipperApp')
    .controller('AdminPictureFoundDetailController', function ($scope, $rootScope, $stateParams, entity, AdminPictureFound) {
        $scope.pictureFound = entity;

        $scope.load = function (pictureSearch_id, picture_id) {
            AdminPictureFound.get({pictureSearch_id : $stateParams.pictureSearch_id, picture_id : $stateParams.picture_id}, function(result) {
                $scope.pictureFound = result;
            });
        };
        $rootScope.$on('flipperApp:adminPictureFoundUpdate', function(event, result) {
            $scope.pictureFound = result;
        });
    });
