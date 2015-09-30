'use strict';

angular.module('flipperApp')
    .controller('AdminPictureController', function ($scope, AdminPicture, PictureSearchQuery, ParseLinks) {
        $scope.pictures = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            AdminPicture.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.pictures = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            AdminPicture.get({id: id}, function(result) {
                $scope.picture = result;
                $('#deletePictureConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdminPicture.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePictureConfirmation').modal('hide');
                    $scope.clear();
                });
        };

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
