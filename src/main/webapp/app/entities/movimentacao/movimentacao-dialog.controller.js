(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('MovimentacaoDialogController', MovimentacaoDialogController);

    MovimentacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$uibModal', 'entity', 'Movimentacao', 'TipoMovimentacao', 'Documento', 'Pessoas', 'Item'];

    function MovimentacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance,$uibModal, entity, Movimentacao, TipoMovimentacao, Documento, Pessoa, Item) {
        var vm = this;
        vm.movimentacao = entity;
        vm.tipomovimentacaos = TipoMovimentacao.query();
        vm.documentos = Documento.query();
        vm.items = Item.query(); 
        vm.itemselect = {};

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:movimentacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.movimentacao.id !== null) {
                Movimentacao.update(vm.movimentacao, onSaveSuccess, onSaveError);
            } else {
                Movimentacao.save(vm.movimentacao, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.data = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
        
        vm.dialogBuscarPessoa = function()
        {
        	$uibModal.open({
                templateUrl: 'app/entities/pessoa/pessoa-dialog-busca.html',
                controller: 'PessoaDialogBuscaController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            nome: null,
                            cadastro: null,
                            id: null
                        };
                    }
                }
            }).result.then(function(pessoa) {
               vm.movimentacao.pessoa = pessoa;
            }, function(result) 
            {
            });
        };
        
        vm.dialogBuscarItens = function(tm)
        {
        	$uibModal.open({
                templateUrl: 'app/entities/item/item-dialog-busca.html',
                controller: 'ItemDialogBuscaController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            serial: null,
                            modelo: null,
                            tipoMovimentacao: tm,
                            id: null
                        };
                    }
                }
            }).result.then(function(item) {
            	if(!vm.movimentacao.items)
            		{vm.movimentacao.items = [];}
            	var naoAchou = true;
            	vm.movimentacao.items.forEach(function (elemento)
            	{
            		if(elemento.id == item.id)
            			{
            				naoAchou = false;
            			}
            		
            	});
            	if(naoAchou)
            		{
            		vm.movimentacao.items.push(item);
            		}
            }, function(result) 
            {
            });
        }
    }
})();
