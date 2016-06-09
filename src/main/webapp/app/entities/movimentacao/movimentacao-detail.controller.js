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
        vm.grafico = {
                labels: ["January", "February", "March", "April", "May", "June", "July"],
                series: ['Series A', 'Series B'],
                data: [
                    [65, 59, 80, 81, 56, 55, 40],
                    [28, 48, 40, 19, 86, 27, 90]
                ]
            };
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:movimentacaoUpdate', function(event, result) {
            vm.movimentacao = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
