'use strict';

angular.module('flipperApp')
    .factory('PictureFound', function ($resource) {
        return $resource('api/pictureFounds/:pictureSearch_id/:picture_id', {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('PictureFoundByPictureSearch', function ($resource) {
        return $resource('api/pictureFounds/byPictureSearch/:pictureSearch_id', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
