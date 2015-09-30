'use strict';

angular.module('flipperApp').controller('AdminPictureDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'AdminPicture',
        function($scope, $stateParams, $modalInstance, entity, AdminPicture) {

        $scope.picture = entity;
        $scope.load = function(id) {
            AdminPicture.get({id : id}, function(result) {
                $scope.picture = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('flipperApp:adminPictureUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.picture.id != null) {
                AdminPicture.update($scope.picture, onSaveFinished);
            } else {
                AdminPicture.save($scope.picture, onSaveFinished);
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
