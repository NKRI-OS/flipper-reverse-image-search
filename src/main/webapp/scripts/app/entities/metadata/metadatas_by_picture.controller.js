'use strict';

angular.module('flipperApp')
    .controller('MetadataByPictureController', function ($scope, $rootScope, $stateParams, entity, MetadataByPicture) {
        $scope.metadatas = [];
        $scope.loadAll = function(picture_id) {
            MetadataByPicture.query({picture_id: picture_id}, function(result) {
                $scope.metadatas = result;
            });
        };


        $scope.loadAll($stateParams.picture_id);

        $scope.refresh = function () {
            $scope.loadAll($stateParams.picture_id);
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.metadata = {directoryName: null, tagType: null, tagName: null, description: null, picture_id: null, title: null, pictureFile: null, id: null};
        };
    });
