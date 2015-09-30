'use strict';

angular.module('flipperApp')
    .factory('AdminPicture', function ($resource) {
        return $resource('api/admin/pictures/:id', {}, {
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
    });
