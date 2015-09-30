'use strict';

angular.module('flipperApp')
    .controller('AdminPictureFoundByPictureSearchController', function ($scope, $rootScope, $stateParams, entity, AdminPictureFound, AdminPictureFoundByPictureSearch, ParseLinks) {
        $scope.pictureFounds = [];
        $scope.page = 0;

        $scope.loadAll = function(pictureSearch_id) {
            AdminPictureFoundByPictureSearch.query({pictureSearch_id: pictureSearch_id, page: $scope.page, size: 20}, function(result, headers) {
               $scope.links = ParseLinks.parse(headers('link'));
               $scope.pictureFounds = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll($stateParams.pictureSearch_id);
        };
        $scope.loadAll($stateParams.pictureSearch_id);

        $scope.delete = function (id) {
            AdminPictureFound.get({id: id}, function(result) {
                $scope.pictureFound = result;
                $('#deletePictureFoundConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AdminPictureFound.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePictureFoundConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll($stateParams.pictureSearch_id);
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pictureFound = {picture_id: null, title: null, owner: null, littlePictureFile: null, pictureSearch_id: null, autocolorCorrelogramScore: null, ceddScore: null, colorHistogramScore: null, colorLayoutScore: null, edgeHistogramScore: null, phogScore: null, mediumPictureFile: null, bigPictureFile: null, created: null};
        };
    });
