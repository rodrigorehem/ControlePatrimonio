(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('PessoaDialogBuscaController', PessoaDialogBuscaController);

    PessoaDialogBuscaController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$http', 'entity', 'Pessoa'];

    function PessoaDialogBuscaController ($timeout, $scope, $stateParams, $uibModalInstance,$http, entity, Pessoa) {
        var vm = this;
        vm.pessoaBusca = entity;
        vm.pessoas = {};

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveError = function () {
            vm.isSaving = false;
        };

        
        vm.ok = function (pessoa) {
            //$uibModalInstance.close(result);
        	var data = {
        			 nome:pessoa.nome
        			};

        			var config = {
        			 params: data,
        			 headers : {'Accept' : 'application/json'}
        			};
        	$http.get('/api/pessoas/all', config). 
            success(function(data, status) {
            	vm.pessoas = data;
            })
            .
            error(function(data, status) {
               console.log('ERRO')
            });
        	
            vm.isSaving = false;
        };
        
        vm.selecionar = function(pessoa)
        {
        	$uibModalInstance.close(pessoa);
        };
        
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
