(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoDocumentoDeleteController',TipoDocumentoDeleteController);

    TipoDocumentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'TipoDocumento'];

    function TipoDocumentoDeleteController($uibModalInstance, entity, TipoDocumento) {
        var vm = this;
        vm.tipoDocumento = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TipoDocumento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
