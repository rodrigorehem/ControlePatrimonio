(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('RefinarBuscaItemController', RefinarBuscaItemController);

    RefinarBuscaItemController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$uibModal','$http', 'entity', 'Item', 'TipoItem'];

    function RefinarBuscaItemController ($timeout, $scope, $stateParams, $uibModalInstance,$uibModal,$http, entity, Item,TipoItem) {
    	var vm = this;
    	vm.tipoitems = TipoItem.query();
    	
        vm.item = entity;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        var onSaveError = function () {
            vm.isSaving = false;
        };
        
        vm.ok = function (item) {
            $uibModalInstance.close(item);        	
            vm.isSaving = false;
        };
        
        vm.selecionar = function(item)
        {
        	$uibModalInstance.close(item);
        };
        
        vm.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.clear = function() {
            $uibModalInstance.dismiss('limpar');
        };
        
        vm.dialogBuscarUnidadeJudiciaria = function()
        {
        	$uibModal.open({
                templateUrl: 'app/entities/unidade-judiciaria/unidade-judiciaria-dialog-busca.html',
                controller: 'UnidadeJudiciariaDialogBuscaController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                        	 id: null,
                        	  coj: null,
                        	  comarca: null,
                        	  unidade: null
                        };
                    }
                }
            }).result.then(function(unidadeJud) {
            	vm.item.unidadeJudiciaria = unidadeJud;
            }, function(result) 
            {
            });
        };
    }
})();
