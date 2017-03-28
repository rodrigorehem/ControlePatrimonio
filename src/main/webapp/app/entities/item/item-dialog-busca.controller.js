(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('ItemDialogBuscaController', ItemDialogBuscaController);

    ItemDialogBuscaController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$http', 'entity', 'Item'];

    function ItemDialogBuscaController ($timeout, $scope, $stateParams, $uibModalInstance,$http, entity, Item) {
        var vm = this;
        vm.itemBusca = entity;
        vm.items = {};

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        var onSaveError = function () {
            vm.isSaving = false;
        };
        
        vm.ok = function (item) {
            //$uibModalInstance.close(result);
        	var data = {
        			 tipoMovimentacao:vm.itemBusca.tipoMovimentacao
        			};
        	if(vm.itemBusca.serial != null)
        		{
        			data.serial = vm.itemBusca.serial
        		}else
        			{
        				data.serial = '';
        			}

        	if(vm.itemBusca.tombo != null)
        		{
        			data.tombo = vm.itemBusca.tombo;
        		}else
        			{
        				data.tombo = '';
        			}
        			var config = {
        			 params: data,
        			 headers : {'Accept' : 'application/json'}
        			};
        	$http.get('api/itens/all', config). 
            success(function(data, status) {
            	vm.items = data;
            })
            .
            error(function(data, status) {
               console.log('ERRO')
            });
        	
            vm.isSaving = false;
        };
        
        vm.selecionar = function(item)
        {
        	$uibModalInstance.close(item);
        };
        
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
