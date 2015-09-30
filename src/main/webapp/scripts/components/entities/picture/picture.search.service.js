'use strict';

angular.module('flipperApp')
    .factory('PictureSearchQuery', function ($resource) {
        return $resource('api/_search/pictures/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
