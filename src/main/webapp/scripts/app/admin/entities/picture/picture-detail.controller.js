'use strict';

angular.module('flipperApp')
    .controller('AdminPictureDetailController', function ($scope, $rootScope, $stateParams, entity, AdminPicture) {
        $scope.picture = entity;
        $scope.load = function (id) {
            AdminPicture.get({id: id}, function(result) {
                $scope.picture = result;
            });
        };
        $rootScope.$on('flipperApp:adminPictureUpdate', function(event, result) {
            $scope.picture = result;
        });
    });
