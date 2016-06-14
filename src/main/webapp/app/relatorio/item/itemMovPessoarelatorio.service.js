(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('ItemMovPessoarelatorio', ItemMovPessoarelatorio);

    ItemMovPessoarelatorio.$inject = ['$resource'];

    function ItemMovPessoarelatorio ($resource) {
        var resourceUrl =  'api/relarorio/itens/entregue/';

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
