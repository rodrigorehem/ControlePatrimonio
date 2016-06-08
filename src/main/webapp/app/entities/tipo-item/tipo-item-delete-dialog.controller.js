(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoItemDeleteController',TipoItemDeleteController);

    TipoItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'TipoItem'];

    function TipoItemDeleteController($uibModalInstance, entity, TipoItem) {
        var vm = this;
        vm.tipoItem = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            TipoItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
