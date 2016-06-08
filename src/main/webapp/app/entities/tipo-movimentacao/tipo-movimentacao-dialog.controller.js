(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoMovimentacaoDialogController', TipoMovimentacaoDialogController);

    TipoMovimentacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoMovimentacao'];

    function TipoMovimentacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TipoMovimentacao) {
        var vm = this;
        vm.tipoMovimentacao = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:tipoMovimentacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tipoMovimentacao.id !== null) {
                TipoMovimentacao.update(vm.tipoMovimentacao, onSaveSuccess, onSaveError);
            } else {
                TipoMovimentacao.save(vm.tipoMovimentacao, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
