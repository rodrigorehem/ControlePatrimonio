(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('unidade-judiciaria', {
            parent: 'entity',
            url: '/unidade-judiciaria?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UnidadeJudiciarias'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciarias.html',
                    controller: 'UnidadeJudiciariaController',
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
        .state('unidade-judiciaria-detail', {
            parent: 'entity',
            url: '/unidade-judiciaria/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'UnidadeJudiciaria'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciaria-detail.html',
                    controller: 'UnidadeJudiciariaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'UnidadeJudiciaria', function($stateParams, UnidadeJudiciaria) {
                    return UnidadeJudiciaria.get({id : $stateParams.id});
                }]
            }
        })
        .state('unidade-judiciaria.new', {
            parent: 'unidade-judiciaria',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciaria-dialog.html',
                    controller: 'UnidadeJudiciariaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                coj: null,
                                comarca: null,
                                unidade: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('unidade-judiciaria', null, { reload: true });
                }, function() {
                    $state.go('unidade-judiciaria');
                });
            }]
        })
        .state('unidade-judiciaria.edit', {
            parent: 'unidade-judiciaria',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciaria-dialog.html',
                    controller: 'UnidadeJudiciariaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UnidadeJudiciaria', function(UnidadeJudiciaria) {
                            return UnidadeJudiciaria.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('unidade-judiciaria', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('unidade-judiciaria.delete', {
            parent: 'unidade-judiciaria',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciaria-delete-dialog.html',
                    controller: 'UnidadeJudiciariaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UnidadeJudiciaria', function(UnidadeJudiciaria) {
                            return UnidadeJudiciaria.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('unidade-judiciaria', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
