(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoMovimentacaoDetailController', TipoMovimentacaoDetailController);

    TipoMovimentacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TipoMovimentacao'];

    function TipoMovimentacaoDetailController($scope, $rootScope, $stateParams, entity, TipoMovimentacao) {
        var vm = this;
        vm.tipoMovimentacao = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:tipoMovimentacaoUpdate', function(event, result) {
            vm.tipoMovimentacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
