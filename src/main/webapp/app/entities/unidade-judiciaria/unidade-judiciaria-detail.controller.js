(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('UnidadeJudiciariaDetailController', UnidadeJudiciariaDetailController);

    UnidadeJudiciariaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'UnidadeJudiciaria'];

    function UnidadeJudiciariaDetailController($scope, $rootScope, $stateParams, entity, UnidadeJudiciaria) {
        var vm = this;
        vm.unidadeJudiciaria = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:unidadeJudiciariaUpdate', function(event, result) {
            vm.unidadeJudiciaria = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
