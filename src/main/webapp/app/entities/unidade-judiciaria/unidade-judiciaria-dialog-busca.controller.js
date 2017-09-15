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
            angular.element('.form-group:eq(0)>select').focus();
        });

        var onSaveError = function () {
            vm.isSaving = false;
        };

        
        vm.ok = function (unidadeJud) {
            //$uibModalInstance.close(result);
        	if(unidadeJud.unidade == null)
    		{
        		unidadeJud.unidade = "";
    		}
        	if(unidadeJud.comarca == null)
    		{
        		unidadeJud.comarca = "";
    		}
        	var data = {
        			 unidade:unidadeJud.unidade,
        			 comarca:unidadeJud.comarca
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
