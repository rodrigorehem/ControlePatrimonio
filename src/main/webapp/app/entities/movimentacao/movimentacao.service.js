(function() {
    'use strict';
    angular
        .module('controlePatrimonialApp')
        .factory('Movimentacao', Movimentacao);

    Movimentacao.$inject = ['$resource', 'DateUtils'];

    function Movimentacao ($resource, DateUtils) {
        var resourceUrl =  'api/movimentacaos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.data = DateUtils.convertDateTimeFromServer(data.data);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
