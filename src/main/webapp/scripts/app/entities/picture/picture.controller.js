'use strict';

angular.module('flipperApp')
    .controller('PictureController', function ($scope, Picture, PictureSearchQuery) {
        $scope.pictures = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            /** Infinity Scroll */

            Picture.query({}, function(result) {
                $scope.pictures = result;
            });

            /*
            Picture.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pictures = result;
            });
            */
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            if($scope.searchQuery != ""){
                PictureSearchQuery.query({query: $scope.searchQuery}, function(result) {
                    $scope.pictures = result;
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
            $scope.picture = {title: null, description: null, pictureFile: null, owner: null, modifiedBy: null, favourites: null, likes: null, created: null, modified: null, id: null};
        };
    });
