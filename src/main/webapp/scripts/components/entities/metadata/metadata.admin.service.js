'use strict';

angular.module('flipperApp')
    .factory('AdminMetadata', function ($resource) {
        return $resource('api/admin/metadatas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            }
        });
    });
