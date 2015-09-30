'use strict';

angular.module('flipperApp')
    .controller('AdminMetadataDetailController', function ($scope, $rootScope, $stateParams, entity, AdminMetadata) {
        $scope.metadata = entity;
        $scope.load = function (id) {
            AdminMetadata.get({id: id}, function(result) {
                $scope.metadata = result;
            });
        };
        $rootScope.$on('flipperApp:adminMetadataUpdate', function(event, result) {
            $scope.metadata = result;
        });
    });
