(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoItemDialogController', TipoItemDialogController);

    TipoItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoItem'];

    function TipoItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TipoItem) {
        var vm = this;
        vm.tipoItem = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:tipoItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tipoItem.id !== null) {
                TipoItem.update(vm.tipoItem, onSaveSuccess, onSaveError);
            } else {
                TipoItem.save(vm.tipoItem, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
