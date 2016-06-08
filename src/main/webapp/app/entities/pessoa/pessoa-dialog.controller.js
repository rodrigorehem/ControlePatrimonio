(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('PessoaDialogController', PessoaDialogController);

    PessoaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pessoa'];

    function PessoaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pessoa) {
        var vm = this;
        vm.pessoa = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:pessoaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.pessoa.id !== null) {
                Pessoa.update(vm.pessoa, onSaveSuccess, onSaveError);
            } else {
                Pessoa.save(vm.pessoa, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
