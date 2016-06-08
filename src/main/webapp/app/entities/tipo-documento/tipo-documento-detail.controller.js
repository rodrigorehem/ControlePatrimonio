(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('TipoDocumentoDetailController', TipoDocumentoDetailController);

    TipoDocumentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TipoDocumento'];

    function TipoDocumentoDetailController($scope, $rootScope, $stateParams, entity, TipoDocumento) {
        var vm = this;
        vm.tipoDocumento = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:tipoDocumentoUpdate', function(event, result) {
            vm.tipoDocumento = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
