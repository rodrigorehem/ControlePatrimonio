(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('relatorio-itemdisponivel', {
            parent: 'relatorio',
            url: '/relarorio/itens/disponivel?page&sort&search&filtro&pagination',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Items Disponíveis'
            },
            views: {
                'content@': {
                    templateUrl: 'app/relatorio/item/itensdisponiveis.html',
                    controller: 'RelatorioItemController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null,
                filtro: null,
                pagination: {
                    value: 'true',
                    squash: true
                }
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search,
                        filtro: $stateParams.filtro,
                        pagination: $stateParams.pagination
                    };
                }],
            }
        });
    }

})();
