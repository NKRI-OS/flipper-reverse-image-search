'use strict';

angular.module('flipperApp')
    .controller('AdminPictureSearchController', function ($scope, AdminPictureSearch, ParseLinks) {
        $scope.pictureSearchs = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            AdminPictureSearch.query({page: $scope.page, size: 20}, function(result, headers) {
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
            AdminPictureSearch.get({id: id}, function(result) {
                $scope.pictureSearch = result;
                $('#deletePictureSearchConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdminPictureSearch.delete({id: id},
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
