'use strict';

angular.module('flipperApp')
    .controller('PictureFoundController', function ($scope, $rootScope, $stateParams, entity, PictureFound, PictureFoundByPictureSearch) {
        $scope.pictureSearch = null;
        $scope.pictureFounds = [];
        $scope.page = 0;

        $scope.loadAll = function(pictureSearch_id) {
            PictureFoundByPictureSearch.query({pictureSearch_id: pictureSearch_id}, function(result) {
                $scope.pictureFounds = result;
            });
        };
        $rootScope.$on('flipperApp:pictureSearchUpdate', function(event, result) {
            $scope.pictureSearch = result;
            //alert("pictures found for search "+result.id);
            $scope.loadAll(result.id);
        });

        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll($stateParams.pictureSearch_id);
        };
        $scope.loadAll($stateParams.pictureSearch_id);

        $scope.refresh = function () {
            $scope.loadAll($stateParams.pictureSearch_id);
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pictureFound = {picture_id: null, title: null, owner: null, totalScore:null, littlePictureFile: null, pictureSearch_id: null, autocolorCorrelogramScore: null, ceddScore: null, colorHistogramScore: null, colorLayoutScore: null, edgeHistogramScore: null, phogScore: null, mediumPictureFile: null, bigPictureFile: null, created: null};
        };
    });
