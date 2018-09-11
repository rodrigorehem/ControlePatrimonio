(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipo-movimentacao', {
            parent: 'entity',
            url: '/tipo-movimentacao?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoMovimentacaos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-movimentacao/tipo-movimentacaos.html',
                    controller: 'TipoMovimentacaoController',
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
        })
        .state('tipo-movimentacao-detail', {
            parent: 'entity',
            url: '/tipo-movimentacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoMovimentacao'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-movimentacao/tipo-movimentacao-detail.html',
                    controller: 'TipoMovimentacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TipoMovimentacao', function($stateParams, TipoMovimentacao) {
                    return TipoMovimentacao.get({id : $stateParams.id});
                }]
            }
        })
        .state('tipo-movimentacao.new', {
            parent: 'tipo-movimentacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-movimentacao/tipo-movimentacao-dialog.html',
                    controller: 'TipoMovimentacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                descricao: null,
                                categoria: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipo-movimentacao', null, { reload: true });
                }, function() {
                    $state.go('tipo-movimentacao');
                });
            }]
        })
        .state('tipo-movimentacao.edit', {
            parent: 'tipo-movimentacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-movimentacao/tipo-movimentacao-dialog.html',
                    controller: 'TipoMovimentacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoMovimentacao', function(TipoMovimentacao) {
                            return TipoMovimentacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-movimentacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-movimentacao.delete', {
            parent: 'tipo-movimentacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-movimentacao/tipo-movimentacao-delete-dialog.html',
                    controller: 'TipoMovimentacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TipoMovimentacao', function(TipoMovimentacao) {
                            return TipoMovimentacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-movimentacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
