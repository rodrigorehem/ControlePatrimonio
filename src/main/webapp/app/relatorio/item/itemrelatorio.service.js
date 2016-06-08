(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('ItemRelatorio', ItemRelatorio);

    ItemRelatorio.$inject = ['$resource'];

    function ItemRelatorio ($resource) {
        var resourceUrl =  'api/relarorio/itens/disponivel';

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
