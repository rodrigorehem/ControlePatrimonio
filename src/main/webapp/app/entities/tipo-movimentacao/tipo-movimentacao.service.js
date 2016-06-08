(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('TipoMovimentacao', TipoMovimentacao);

    TipoMovimentacao.$inject = ['$resource'];

    function TipoMovimentacao ($resource) {
        var resourceUrl =  'api/tipo-movimentacaos/:id';

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
