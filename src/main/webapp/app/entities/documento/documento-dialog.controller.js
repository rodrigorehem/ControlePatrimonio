(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('DocumentoDialogController', DocumentoDialogController);

    DocumentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Documento', 'TipoDocumento', 'Movimentacao'];

    function DocumentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Documento, TipoDocumento, Movimentacao) {
        var vm = this;
        vm.documento = entity;
        vm.tipodocumentos = TipoDocumento.query();
        vm.movimentacaos = Movimentacao.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:documentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.documento.id !== null) {
                Documento.update(vm.documento, onSaveSuccess, onSaveError);
            } else {
                Documento.save(vm.documento, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setAnexo = function ($file, documento) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        documento.anexo = base64Data;
                        documento.anexoContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
