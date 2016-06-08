(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipo-documento', {
            parent: 'entity',
            url: '/tipo-documento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoDocumentos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-documento/tipo-documentos.html',
                    controller: 'TipoDocumentoController',
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
        .state('tipo-documento-detail', {
            parent: 'entity',
            url: '/tipo-documento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'TipoDocumento'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-documento/tipo-documento-detail.html',
                    controller: 'TipoDocumentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'TipoDocumento', function($stateParams, TipoDocumento) {
                    return TipoDocumento.get({id : $stateParams.id});
                }]
            }
        })
        .state('tipo-documento.new', {
            parent: 'tipo-documento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-documento/tipo-documento-dialog.html',
                    controller: 'TipoDocumentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                descricao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipo-documento', null, { reload: true });
                }, function() {
                    $state.go('tipo-documento');
                });
            }]
        })
        .state('tipo-documento.edit', {
            parent: 'tipo-documento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-documento/tipo-documento-dialog.html',
                    controller: 'TipoDocumentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoDocumento', function(TipoDocumento) {
                            return TipoDocumento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-documento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-documento.delete', {
            parent: 'tipo-documento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-documento/tipo-documento-delete-dialog.html',
                    controller: 'TipoDocumentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TipoDocumento', function(TipoDocumento) {
                            return TipoDocumento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-documento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
