'use strict';

angular.module('flipperApp')
    .factory('PictureSearch', function ($resource, DateUtils) {
        return $resource('api/pictureSearchs/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    })
    .factory('PictureSearchByPicture', function ($resource, DateUtils) {
        return $resource('api/_search/pictureSearchs', {}, {
            'save': { method: 'POST'}
        });
    });
