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
        vm.gerarDevolucao = gerarDevolucao;
        vm.exibirGerarDevolucao = exibirGerarDevolucao;
        vm.downloadPDF = downloadPDF;
        
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
        
        function downloadPDF(doc)
        {
        	var dlnk = document.getElementById('dwnldLnk');
            dlnk.href = 'data:' + doc.anexoContentType + ';base64,' + doc.anexo;
            dlnk.download = doc.id+'_'+doc.descricao+'.pdf';

            dlnk.click();
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
        function exibirGerarDevolucao(mov)
        {
        	try{
        		return mov.tipoMovimentacao.id == 1;
        	}catch(e)
        	{
        		return false;
        	}
        }
        
        function gerarDevolucao(movimentacao)
        {
        	$uibModal.open({
                templateUrl: 'app/entities/movimentacao/movimentacao-dialog.html',
                controller: 'MovimentacaoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            descricao: movimentacao.descricao,
                            pessoa: movimentacao.pessoa,
                            tipoMovimentacao:{id:2},
                            items:movimentacao.items,
                            data: new Date(),
                            gerarDevolucao:true,
                            id: null
                        };
                    }
                }
            }).result.then(function(movimentacao) {
                //$state.go('movimentacao', null, { reload: true });
            	 $state.go('movimentacao-detail', {id:movimentacao.id}, { reload: true });
            }, function() {
                //$state.go('movimentacao');
            	$state.go('movimentacao-detail', {id:movimentacao.id}, { reload: true });
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
