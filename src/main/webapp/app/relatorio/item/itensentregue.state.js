(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('relatorio.itementregue', {
            parent: 'relatorio',
            url: '/relarorio/itens/entregue?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Items Entregue'
            },
            views: {
                'content@': {
                    templateUrl: 'app/relatorio/item/itensentregue.html',
                    controller: 'RelatorioItemEntregueController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'p.nome,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        });
    }

})();
