'use strict';

angular.module('flipperApp')
    .controller('AdminPictureSearchDetailController', function ($scope, $rootScope, $stateParams, entity, AdminPictureSearch) {
        $scope.pictureSearch = entity;

        $scope.load = function (id) {
            AdminPictureSearch.get({id: id}, function(result) {
                $scope.pictureSearch = result;
            });
        };
        $rootScope.$on('flipperApp:adminPictureSearchUpdate', function(event, result) {
            $scope.pictureSearch = result;
        });
    });
