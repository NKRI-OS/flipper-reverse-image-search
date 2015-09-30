'use strict';

angular.module('flipperApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
