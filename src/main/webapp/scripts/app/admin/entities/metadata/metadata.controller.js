'use strict';

angular.module('flipperApp')
    .controller('AdminMetadataController', function ($scope, AdminMetadata, MetadataSearchQuery) {
        $scope.metadatas = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            AdminMetadata.query({}, function(result) {
               $scope.metadatas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            AdminMetadata.get({id: id}, function(result) {
                $scope.metadata = result;
                $('#deleteMetadataConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdminMetadata.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMetadataConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            if($scope.searchQuery != ""){
                MetadataSearchQuery.query({query: $scope.searchQuery}, function(result) {
                    $scope.metadatas = result;
                }, function(response) {
                    if(response.status === 404) {
                        $scope.loadAll();
                    }
                });
            }
        };


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.metadata = {directoryName: null, tagType: null, tagName: null, description: null, picture_id: null, title: null, pictureFile: null, id: null};
        };
    });
