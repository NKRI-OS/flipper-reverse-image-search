'use strict';

angular.module('flipperApp')
    .factory('Metadata', function ($resource, DateUtils) {
        return $resource('api/metadatas/:id', {}, {
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
    .factory('MetadataByPicture', function ($resource) {
        return $resource('api/metadatas/byPicture/:picture_id', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
