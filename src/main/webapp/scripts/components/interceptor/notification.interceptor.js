 'use strict';

angular.module('flipperApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-flipperApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-flipperApp-params')});
                }
                return response;
            }
        };
    });
