'use strict';

angular.module('flipperApp')
    .controller('PictureDetailController', function ($scope, $rootScope, $stateParams, entity, Picture) {
        $scope.picture = entity;
        $scope.load = function (id) {
            Picture.get({id: id}, function(result) {
                $scope.picture = result;
            });
        };
        $rootScope.$on('flipperApp:pictureUpdate', function(event, result) {
            $scope.picture = result;
        });
    });
