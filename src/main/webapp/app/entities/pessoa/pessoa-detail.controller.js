(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('PessoaDetailController', PessoaDetailController);

    PessoaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$http', 'entity', 'Pessoa'];

    function PessoaDetailController($scope, $rootScope, $stateParams, $http, entity, Pessoa) {
        var vm = this;
        vm.pessoa = entity;
        vm.itens = {};
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:pessoaUpdate', function(event, result) {
            vm.pessoa = result;
        });
        
        var data = 
        {
   			 id:$stateParams.id
   		};

   		var config = 
   		{
   			 params: data,
   			 headers : {'Accept' : 'application/json'}
   		};
	   	$http.get('/api/pessoas/itens', config). 
	       success(function(data, status) 
	    	{
	       		vm.itens = data;
	    	}).
	       error(function(data, status) 
	        {
	    	   console.log('ERRO');
	        });
   	
        $scope.$on('$destroy', unsubscribe);

    }
})();
