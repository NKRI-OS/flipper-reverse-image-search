'use strict';

angular.module('flipperApp')
    .factory('AdminPictureFound', function ($resource) {
        return $resource('api/admin/pictureFounds/:pictureSearch_id/:picture_id', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('AdminPictureFoundByPictureSearch', function ($resource) {
        return $resource('api/admin/pictureFounds/byPictureSearch/:pictureSearch_id', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
