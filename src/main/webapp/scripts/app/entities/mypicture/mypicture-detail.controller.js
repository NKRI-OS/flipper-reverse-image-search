'use strict';

angular.module('flipperApp')
    .controller('MyPictureDetailController', function ($scope, $rootScope, $stateParams, entity, MyPicture) {
        $scope.picture = entity;
        $scope.load = function (id) {
            MyPicture.get({id: id}, function(result) {
                $scope.picture = result;
            });
        };
        $rootScope.$on('flipperApp:pictureUpdate', function(event, result) {
            $scope.picture = result;
        });
    });
