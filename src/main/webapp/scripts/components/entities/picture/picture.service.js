'use strict';

angular.module('flipperApp')
    .factory('Picture', function ($resource) {
        return $resource('api/pictures/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('PictureByUser', function ($resource) {
        return $resource('api/pictures/byUser/:owner', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
