(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('UnidadeJudiciaria', UnidadeJudiciaria);

    UnidadeJudiciaria.$inject = ['$resource'];

    function UnidadeJudiciaria ($resource) {
        var resourceUrl =  'api/unidade-judiciarias/:id';

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
