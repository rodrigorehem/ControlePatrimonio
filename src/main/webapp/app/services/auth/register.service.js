(function () {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
