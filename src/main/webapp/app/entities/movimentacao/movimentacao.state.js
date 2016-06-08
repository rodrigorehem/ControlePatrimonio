(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('movimentacao', {
            parent: 'entity',
            url: '/movimentacao?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Movimentacaos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movimentacao/movimentacaos.html',
                    controller: 'MovimentacaoController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'data,asc',
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
        .state('movimentacao-detail', {
            parent: 'entity',
            url: '/movimentacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Movimentacao'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/movimentacao/movimentacao-detail.html',
                    controller: 'MovimentacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Movimentacao', function($stateParams, Movimentacao) {
                    return Movimentacao.get({id : $stateParams.id});
                }]
            }
        })
        .state('movimentacao.new', {
            parent: 'movimentacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movimentacao/movimentacao-dialog.html',
                    controller: 'MovimentacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: 'Segue anexo os itens listado à baixo em comodato com a empresa Telefônica S/A Vivo, juntamente com manual de instalação e carregador de parede.',
                                data: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('movimentacao', null, { reload: true });
                }, function() {
                    $state.go('movimentacao');
                });
            }]
        })
        .state('movimentacao.edit', {
            parent: 'movimentacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movimentacao/movimentacao-dialog.html',
                    controller: 'MovimentacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Movimentacao', function(Movimentacao) {
                            return Movimentacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('movimentacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('movimentacao.delete', {
            parent: 'movimentacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/movimentacao/movimentacao-delete-dialog.html',
                    controller: 'MovimentacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Movimentacao', function(Movimentacao) {
                            return Movimentacao.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('movimentacao', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
