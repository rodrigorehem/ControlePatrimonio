(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('UnidadeJudiciariaDialogBuscaController', UnidadeJudiciariaDialogBuscaController);

    UnidadeJudiciariaDialogBuscaController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','$http', 'entity', 'UnidadeJudiciaria'];

    function UnidadeJudiciariaDialogBuscaController ($timeout, $scope, $stateParams, $uibModalInstance,$http, entity, UnidadeJudiciaria) {
        var vm = this;
        vm.unidadeJudiciaria = entity;
        vm.unidadesJudiciarias = {};

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveError = function () {
            vm.isSaving = false;
        };

        
        vm.ok = function (unidadeJud) {
            //$uibModalInstance.close(result);
        	var data = {
        			 unidade:unidadeJud.unidade
        			};

        			var config = {
        			 params: data,
        			 headers : {'Accept' : 'application/json'}
        			};
        	$http.get('api/unidade-judiciarias/all', config). 
            success(function(data, status) {
            	vm.unidadesJudiciarias = data;
            })
            .
            error(function(data, status) {
               console.log('ERRO')
            });
        	
            vm.isSaving = false;
        };
        
        vm.selecionar = function(unidadeJud)
        {
        	$uibModalInstance.close(unidadeJud);
        };
        
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
