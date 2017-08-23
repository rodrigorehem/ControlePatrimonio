(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('UnidadeJudiciariaDialogController', UnidadeJudiciariaDialogController);

    UnidadeJudiciariaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UnidadeJudiciaria'];

    function UnidadeJudiciariaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UnidadeJudiciaria) {
        var vm = this;
        vm.unidadeJudiciaria = entity;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('controlePatrimonialApp:unidadeJudiciariaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.unidadeJudiciaria.id !== null) {
                UnidadeJudiciaria.update(vm.unidadeJudiciaria, onSaveSuccess, onSaveError);
            } else {
                UnidadeJudiciaria.save(vm.unidadeJudiciaria, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
