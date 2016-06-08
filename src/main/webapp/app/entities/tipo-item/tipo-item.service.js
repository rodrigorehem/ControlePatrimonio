(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('TipoItem', TipoItem);

    TipoItem.$inject = ['$resource'];

    function TipoItem ($resource) {
        var resourceUrl =  'api/tipo-items/:id';

        return $resource(resourceUrl, {}, {
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
    }
})();
