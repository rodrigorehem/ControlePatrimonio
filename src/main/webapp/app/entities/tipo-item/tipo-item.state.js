(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipo-item', {
            parent: 'entity',
            url: '/tipo-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-item/tipo-items.html',
                    controller: 'TipoItemController',
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
        .state('tipo-item-detail', {
            parent: 'entity',
            url: '/tipo-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-item/tipo-item-detail.html',
                    controller: 'TipoItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TipoItem', function($stateParams, TipoItem) {
                    return TipoItem.get({id : $stateParams.id});
                }]
            }
        })
        .state('tipo-item.new', {
            parent: 'tipo-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-item/tipo-item-dialog.html',
                    controller: 'TipoItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                sigla: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipo-item', null, { reload: true });
                }, function() {
                    $state.go('tipo-item');
                });
            }]
        })
        .state('tipo-item.edit', {
            parent: 'tipo-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-item/tipo-item-dialog.html',
                    controller: 'TipoItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoItem', function(TipoItem) {
                            return TipoItem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-item.delete', {
            parent: 'tipo-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-item/tipo-item-delete-dialog.html',
                    controller: 'TipoItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TipoItem', function(TipoItem) {
                            return TipoItem.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-item', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
