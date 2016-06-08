(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoDocumentoDialogController', TipoDocumentoDialogController);

    TipoDocumentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoDocumento'];

    function TipoDocumentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TipoDocumento) {
        var vm = this;
        vm.tipoDocumento = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:tipoDocumentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.tipoDocumento.id !== null) {
                TipoDocumento.update(vm.tipoDocumento, onSaveSuccess, onSaveError);
            } else {
                TipoDocumento.save(vm.tipoDocumento, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
