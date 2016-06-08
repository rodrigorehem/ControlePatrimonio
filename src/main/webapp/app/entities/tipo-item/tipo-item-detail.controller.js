(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoItemDetailController', TipoItemDetailController);

    TipoItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TipoItem'];

    function TipoItemDetailController($scope, $rootScope, $stateParams, entity, TipoItem) {
        var vm = this;
        vm.tipoItem = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:tipoItemUpdate', function(event, result) {
            vm.tipoItem = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
