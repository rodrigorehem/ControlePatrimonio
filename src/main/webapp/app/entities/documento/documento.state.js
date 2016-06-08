(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('documento', {
            parent: 'entity',
            url: '/documento?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Documentos'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documento/documentos.html',
                    controller: 'DocumentoController',
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
        .state('documento-detail', {
            parent: 'entity',
            url: '/documento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Documento'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/documento/documento-detail.html',
                    controller: 'DocumentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Documento', function($stateParams, Documento) {
                    return Documento.get({id : $stateParams.id});
                }]
            }
        })
        .state('documento.new', {
            parent: 'documento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documento/documento-dialog.html',
                    controller: 'DocumentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                anexo: null,
                                anexoContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('documento', null, { reload: true });
                }, function() {
                    $state.go('documento');
                });
            }]
        })
        .state('documento.edit', {
            parent: 'documento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documento/documento-dialog.html',
                    controller: 'DocumentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Documento', function(Documento) {
                            return Documento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('documento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('documento.delete', {
            parent: 'documento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/documento/documento-delete-dialog.html',
                    controller: 'DocumentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Documento', function(Documento) {
                            return Documento.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('documento', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
