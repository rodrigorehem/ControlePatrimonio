(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoMovimentacaoDeleteController',TipoMovimentacaoDeleteController);

    TipoMovimentacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'TipoMovimentacao'];

    function TipoMovimentacaoDeleteController($uibModalInstance, entity, TipoMovimentacao) {
        var vm = this;
        vm.tipoMovimentacao = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TipoMovimentacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
