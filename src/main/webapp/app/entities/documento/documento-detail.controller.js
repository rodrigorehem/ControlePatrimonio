(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('DocumentoDetailController', DocumentoDetailController);

    DocumentoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Documento', 'TipoDocumento', 'Movimentacao'];

    function DocumentoDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Documento, TipoDocumento, Movimentacao) {
        var vm = this;
        vm.documento = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:documentoUpdate', function(event, result) {
            vm.documento = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
