'use strict';

angular.module('flipperApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


