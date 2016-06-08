(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('RefinarBuscaItemController', RefinarBuscaItemController);

    RefinarBuscaItemController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$http', 'entity', 'Item', 'TipoItem'];

    function RefinarBuscaItemController ($timeout, $scope, $stateParams, $uibModalInstance,$http, entity, Item,TipoItem) {
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
        
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
