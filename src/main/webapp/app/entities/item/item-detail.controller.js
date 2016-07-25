(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('ItemDetailController', ItemDetailController);

    ItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', '$http', 'entity', 'Item', 'TipoItem', 'Movimentacao'];

    function ItemDetailController($scope, $rootScope, $stateParams, $http, entity, Item, TipoItem, Movimentacao) {
        var vm = this;
        vm.item = entity;
        vm.movimentacaoHistorico = {};
        
        var unsubscribe = $rootScope.$on('controlePatrimonialApp:itemUpdate', function(event, result) {
            vm.item = result;
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
	   	$http.get('api/itens/movimentacoes', config). 
	       success(function(data, status) 
	    	{
	    	   vm.movimentacaoHistorico = data;
	       		
	    	}).
	       error(function(data, status) 
	        {
	    	   console.log('ERRO');
	        });
	   	
	   	
        $scope.$on('$destroy', unsubscribe);

    }
})();
