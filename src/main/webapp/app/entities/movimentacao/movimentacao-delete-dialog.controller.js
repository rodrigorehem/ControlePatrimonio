(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('MovimentacaoDeleteController',MovimentacaoDeleteController);

    MovimentacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Movimentacao'];

    function MovimentacaoDeleteController($uibModalInstance, entity, Movimentacao) {
        var vm = this;
        vm.movimentacao = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Movimentacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
