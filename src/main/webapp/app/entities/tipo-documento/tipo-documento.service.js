(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('TipoDocumento', TipoDocumento);

    TipoDocumento.$inject = ['$resource'];

    function TipoDocumento ($resource) {
        var resourceUrl =  'api/tipo-documentos/:id';

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
