(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('MovimentacaoDetailController', MovimentacaoDetailController);

    MovimentacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$state', '$uibModal', 'DataUtils', 'entity', 'Movimentacao', 'TipoMovimentacao', 'Documento', 'Pessoa', 'Item','Principal'];

    function MovimentacaoDetailController($scope, $rootScope, $stateParams, $state, $uibModal, DataUtils, entity, Movimentacao, TipoMovimentacao, Documento, Pessoa, Item,Principal) {
        var vm = this;
        vm.movimentacao = entity;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.account = null;
        vm.addDocumento = addDocumento;
        
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function addDocumento(movimentacao)
        {
        	
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
                            movimentacao: movimentacao,
                            id: null
                        };
                    }
                }
            }).result.then(function() {
                $state.go('movimentacao-detail', null, { reload: true });
            }, function() {
                $state.go('movimentacao-detail');
            });
        	
        }
/*        vm.grafico = {
                labels: [ 'Fevereiro', 'March', 'April'],
                series: ['ZEQYBQAG2000ME', 'ZEQYBQAG2000BN', 'ZEQYBQAG2000B2'],
                data: [
                    ['62.14', '55.64','48.20'],
                    ['70.36', '109.62','80.34'],
                    ['32.14', '59.64','68.20']
                ]
            };*/
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:movimentacaoUpdate', function(event, result) {
            vm.movimentacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
