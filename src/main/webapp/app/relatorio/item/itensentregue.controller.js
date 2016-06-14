(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('RelatorioItemEntregueController', RelatorioItemEntregueController);

    RelatorioItemEntregueController.$inject = ['$scope', '$state', 'ItemMovPessoarelatorio','Item', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','$uibModal'];

    function RelatorioItemEntregueController ($scope, $state, ItemMovPessoarelatorio, Item, ParseLinks, AlertService, pagingParams, paginationConstants, $uibModal) {
        var vm = this;
        vm.loadAll = loadAll;
        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.refinarBuscaca =  refinarBuscaca;
        
        vm.item = new Item();
        vm.loadAll();

        function loadAll (item) {
        	ItemMovPessoarelatorio.query(
            		{
            			filtro: search(),
		                page: pagingParams.page - 1,
		                size: paginationConstants.itemsPerPage,
		                sort: sort()
            		}, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function search()
            {
            	var retorno = [];
            	for (var key in vm.item.toJSON()) 
            	{ 
            		if(vm.item[key] != null && vm.item[key] != '')
            			{
            				retorno.push(key+':'+vm.item[key])
            			}
            	}
            	
            	return retorno;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.items = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }
        
        //Refina a busca aplicando Filtros
        function refinarBuscaca(item)
        {
        	$uibModal.open({
                templateUrl: 'app/components/filtrobusca/item/refinar-busca-item.html',
                controller: 'RefinarBuscaItemController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return item;
                    }
                }
            }).result.then(function(item) {
               vm.item = item;
               vm.loadAll(item);
            }, function(result) 
            {
            });
        }
        
        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

    }
})();
