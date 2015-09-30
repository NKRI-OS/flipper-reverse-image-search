'use strict';

angular.module('flipperApp')
    .controller('PictureSearchController', function ($scope, PictureSearch, ParseLinks) {
        $scope.pictureSearchs = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            PictureSearch.query({page: $scope.page, size: 20}, function(result, headers) {
               $scope.links = ParseLinks.parse(headers('link'));
               $scope.pictureSearchs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PictureSearch.get({id: id}, function(result) {
                $scope.pictureSearch = result;
                $('#deletePictureSearchConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PictureSearch.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePictureSearchConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pictureSearch = {pictureFile: null, created: null, pictureIdList: null, userLogin: null, features: null, id: null, pictureDTOList: null};
        };
    });
