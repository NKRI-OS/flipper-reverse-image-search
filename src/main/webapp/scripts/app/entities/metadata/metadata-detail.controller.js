'use strict';

angular.module('flipperApp')
    .controller('MetadataDetailController', function ($scope, $rootScope, $stateParams, entity, Metadata) {
        $scope.metadata = entity;
        $scope.load = function (id) {
            Metadata.get({id: id}, function(result) {
                $scope.metadata = result;
            });
        };
        $rootScope.$on('flipperApp:metadataUpdate', function(event, result) {
            $scope.metadata = result;
        });
    });
