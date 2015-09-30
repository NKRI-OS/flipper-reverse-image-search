'use strict';

angular.module('flipperApp')
    .controller('MetadataController', function ($scope, Metadata, MetadataSearchQuery, ParseLinks) {
        $scope.metadatas = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            Metadata.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.metadatas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
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
