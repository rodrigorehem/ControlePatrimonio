(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('UnidadeJudiciariaDeleteController',UnidadeJudiciariaDeleteController);

    UnidadeJudiciariaDeleteController.$inject = ['$uibModalInstance', 'entity', 'UnidadeJudiciaria'];

    function UnidadeJudiciariaDeleteController($uibModalInstance, entity, UnidadeJudiciaria) {
        var vm = this;
        vm.unidadeJudiciaria = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            UnidadeJudiciaria.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
