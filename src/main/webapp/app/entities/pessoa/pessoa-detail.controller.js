(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('PessoaDetailController', PessoaDetailController);

    PessoaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Pessoa'];

    function PessoaDetailController($scope, $rootScope, $stateParams, entity, Pessoa) {
        var vm = this;
        vm.pessoa = entity;
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:pessoaUpdate', function(event, result) {
            vm.pessoa = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
