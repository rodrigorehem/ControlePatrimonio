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
/*        vm.grafico = {
                labels: [ 'Fevereiro', 'March', 'April'],
                series: ['ZEQYBQAG2000ME', 'ZEQYBQAG2000BN', 'ZEQYBQAG2000B2'],
                data: [
                    ['62.14', '55.64','48.20'],
                    ['70.36', '109.62','80.34'],
                    ['32.14', '59.64','68.20']
                ]
            };*/
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:movimentacaoUpdate', function(event, result) {
            vm.movimentacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
