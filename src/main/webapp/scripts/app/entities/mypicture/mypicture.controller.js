'use strict';

angular.module('flipperApp')
    .controller('MyPictureController', function ($scope, MyPicture) {
        $scope.pictures = [];
        $scope.page = 0;

        $scope.loadAll = function() {
            MyPicture.query({}, function(result) {
                $scope.pictures = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            MyPicture.get({id: id}, function(result) {
                $scope.picture = result;
                $('#deleteMyPictureConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MyPicture.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMyPictureConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.picture = {title: null, description: null, pictureFile: null, owner: null, modifiedBy: null, favourites: null, likes: null, created: null, modified: null, id: null};
        };
    });
