(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('Pessoa', Pessoa);
    
    angular
    	.module('controlePatrimonialApp')
    	.factory('Pessoas', Pessoas);

    Pessoa.$inject = ['$resource'];
    Pessoas.$inject = ['$resource'];

    function Pessoa ($resource) {
        var resourceUrl =  'api/pessoas/:id';

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
    
    function Pessoas ($resource) {
        var resourceUrl =  'api/pessoas/all';

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
