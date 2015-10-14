'use strict';

angular.module('flipperApp')
    .controller('PictureSearchController', function ($scope, PictureSearch) {
        $scope.pictureSearchs = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            PictureSearch.query({}, function(result) {
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
