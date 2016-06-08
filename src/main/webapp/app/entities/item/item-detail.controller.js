(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('ItemDetailController', ItemDetailController);

    ItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Item', 'TipoItem', 'Movimentacao'];

    function ItemDetailController($scope, $rootScope, $stateParams, entity, Item, TipoItem, Movimentacao) {
        var vm = this;
        vm.item = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:itemUpdate', function(event, result) {
            vm.item = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
