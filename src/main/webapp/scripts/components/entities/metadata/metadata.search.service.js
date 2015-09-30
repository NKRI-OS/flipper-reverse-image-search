'use strict';

angular.module('flipperApp')
    .factory('MetadataSearchQuery', function ($resource) {
        return $resource('api/_search/metadatas/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
