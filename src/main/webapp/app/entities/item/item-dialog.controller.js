(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('ItemDialogController', ItemDialogController);

    ItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Item', 'TipoItem', 'Movimentacao'];

    function ItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Item, TipoItem, Movimentacao) {
        var vm = this;
        vm.item = entity;
        vm.tipoitems = TipoItem.query();
        vm.movimentacaos = Movimentacao.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:itemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.item.id !== null) {
                Item.update(vm.item, onSaveSuccess, onSaveError);
            } else {
                Item.save(vm.item, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
