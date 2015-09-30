'use strict';

angular.module('flipperApp')
    .factory('AdminPictureSearch', function ($resource) {
        return $resource('api/admin/pictureSearchs/:id', {}, {
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
    .factory('AdminPictureSearchByPicture', function ($resource) {
        return $resource('api/admin/_search/pictureSearchs', {}, {
            'save': { method: 'POST'}
        });
    });
