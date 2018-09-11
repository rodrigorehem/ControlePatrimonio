(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('ItemMovPessoaEmprestimorelatorio', ItemMovPessoaEmprestimorelatorio);

    ItemMovPessoaEmprestimorelatorio.$inject = ['$resource'];

    function ItemMovPessoaEmprestimorelatorio ($resource) {
        var resourceUrl =  'api/relarorio/itens/emprestados/';

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
