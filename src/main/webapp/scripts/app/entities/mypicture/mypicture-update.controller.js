'use strict';

angular.module('flipperApp').controller('MyPictureUpdateDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'MyPicture',
        function($scope, $stateParams, $modalInstance, entity, MyPicture) {

        $scope.picture = entity;
        $scope.load = function(id) {
            MyPicture.get({id : id}, function(result) {
                $scope.picture = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('flipperApp:pictureUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.picture.id != null) {
                MyPicture.update($scope.picture, onSaveFinished);
            } else {
                MyPicture.save($scope.picture, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

}]);
