(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('MovimentacaoDetailController', MovimentacaoDetailController);

    MovimentacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Movimentacao', 'TipoMovimentacao', 'Documento', 'Pessoa', 'Item'];

    function MovimentacaoDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Movimentacao, TipoMovimentacao, Documento, Pessoa, Item) {
        var vm = this;
        vm.movimentacao = entity;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:movimentacaoUpdate', function(event, result) {
            vm.movimentacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
