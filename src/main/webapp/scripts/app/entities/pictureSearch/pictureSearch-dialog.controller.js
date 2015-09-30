'use strict';

angular.module('flipperApp').controller('PictureSearchDialogController',
    ['$scope', '$modalInstance', 'entity', 'PictureSearchByPicture',
        function($scope, $modalInstance, entity, PictureSearchByPicture) {

        $scope.pictureSearch = entity;

        var onSaveFinished = function (result) {
            //$scope.$emit('flipperApp:pictureSearchUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            PictureSearchByPicture.save($scope.pictureSearch, onSaveFinished);
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }
            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }
            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };

        $scope.setPictureSearchFile = function ($files, pictureSearch) {

            if ($files[0]) {
                var file = $files[0];
                var fileReader = new FileReader();
                fileReader.readAsDataURL(file);
                fileReader.onload = function (e) {
                    var data = e.target.result;
                    var base64Data = data.substr(data.indexOf('base64,') + 'base64,'.length);
                    $scope.$apply(function() {
                        pictureSearch.pictureFile = base64Data;
                    });
                };
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
