(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('PessoaDeleteController',PessoaDeleteController);

    PessoaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pessoa'];

    function PessoaDeleteController($uibModalInstance, entity, Pessoa) {
        var vm = this;
        vm.pessoa = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Pessoa.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
