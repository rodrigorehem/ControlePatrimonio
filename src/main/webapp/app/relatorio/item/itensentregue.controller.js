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
        vm.preencheItem = preencheItem;
        vm.search = search;
        vm.isPagination = pagingParams.pagination == 'true' ? true : false;
        
        vm.exportData = function () {
            var blob = new Blob([document.getElementById('table-responsive').innerHTML], {
                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8"
            });
            
            saveAs(blob, "ItensEntregues.xls");
        };
                
        vm.preencheItem(pagingParams.filtro);
        vm.loadAll();

        function loadAll (item) {
        	ItemMovPessoarelatorio.query(
            		{
            			filtro: vm.search(),
		                page: pagingParams.page - 1,
		                size: vm.isPagination ? paginationConstants.itemsPerPage : 0,
		                sort: sort()
            		}, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                var dataTratado = new Array();
                var dataTratadoFinal = new Array();
                //var count = 0;
                for(var i = 0; i < data.length; i++)
                	{
                		var itemAux;
                		if(dataTratado[data[i].pessoa.id])
                			{
                				itemAux = dataTratado[data[i].pessoa.id];
                			}else { 
                				itemAux = {};
                				itemAux.id = data[i].pessoa.id;
                        		itemAux.nome = data[i].pessoa.nome
                        		itemAux.itens = new Array();
                        		dataTratado[data[i].pessoa.id] = itemAux
                        		dataTratadoFinal.push(itemAux);
                			}
                		itemAux.itens.push(data[i]);
                		
                	}
                vm.items = dataTratadoFinal;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
        
        function search()
        {
        	var retorno = [];
        	if(vm.item.item)
        	{
            	/*for (var key in vm.item.item.toJSON()) 
            	{ 
            		if(vm.item.item[key] != null && vm.item.item[key] != '')
            			{*/
        		if(vm.item.item.serial)
        		retorno.push('i.serial:'+vm.item.item.serial);
        		
        		if(vm.item.item.id)
        		retorno.push('i.id:'+vm.item.item.id);
        		
        		if(vm.item.item.modelo)
        		retorno.push('i.modelo:'+vm.item.item.modelo);
        		
        		if(vm.item.item.estado && vm.item.item.estado.toString().length > 0)
        		retorno.push('i.estado:'+vm.item.item.estado.join('#'));
        		
        		if(vm.item.item.numero)
        		retorno.push('i.numero:'+vm.item.item.numero);
        		
        		if(vm.item.item.tipoItem)
        		retorno.push('i.tipoItem:'+vm.item.item.tipoItem);
        		
        		if(vm.item.item.tombo)
            		retorno.push('i.tombo:'+vm.item.item.tombo);
        		
            }
        	if(vm.item.pessoa)
        	{
        		if(vm.item.pessoa.nome)
        			retorno.push('p.nome:'+vm.item.pessoa.nome);
        		
        		if(vm.item.pessoa.categoriaFuncional && vm.item.pessoa.categoriaFuncional.toString().length > 0)
            		retorno.push('p.categoria_funcional:'+vm.item.pessoa.categoriaFuncional.join('#'));

        	}
        	if(vm.item.unidadeJudiciaria)
        	{
        		retorno.push('u.coj:'+vm.item.unidadeJudiciaria.coj+'#'+vm.item.unidadeJudiciaria.unidade);
        	}
        	vm.filtro = retorno.toString();
        	return retorno;
        }
        
        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }
        
        function preencheItem (filtro) 
        {
        	vm.item = new Item();
        	vm.item.isPagination = vm.isPagination ? 'true' : 'false';
        	if(filtro)
        	{
        		var arrayFiltro = filtro.split(',');
        		for(var i = 0; i < arrayFiltro.length; i++)
        		{
        			var aux = arrayFiltro[i].split(':');
        			switch(aux[0])
        			{
	        			case 'i.serial':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.serial = aux[1];
	        				break;
	        			case 'i.tombo':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.tombo = aux[1];
	        				break;
	        			case 'i.id':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.id = aux[1];
	        				break;
	        			case 'i.modelo':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.modelo = aux[1];
	        				break;
	        			case 'i.estado':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.estado = aux[1].split('#');
	        				break;
	        			case 'i.numero':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.numero = aux[1];
	        				break;
	        			case 'i.tipoItem':
	        				if(!vm.item.item)
	        				{
	        					vm.item.item = {};
	        				}
	        				vm.item.item.tipoItem = aux[1];
	        				break;
	        			case 'p.nome':
	        				if(!vm.item.pessoa)
	        				{
	        					vm.item.pessoa = {};
	        				}
	        				vm.item.pessoa.nome = aux[1];
	        				break;
	        			case 'p.categoria_funcional':
	        				if(!vm.item.pessoa)
	        				{
	        					vm.item.pessoa = {};
	        				}
	        				if(!vm.item.pessoa.categoriaFuncional)
	        				{
	        					vm.item.pessoa.categoriaFuncional = {};
	        				}
	        				vm.item.pessoa.categoriaFuncional = aux[1].split('#');
	        				break;
	        			case 'u.coj':
	        				if(!vm.item.unidadeJudiciaria)
	        				{
	        					vm.item.unidadeJudiciaria = {};
	        				}
	        				vm.item.unidadeJudiciaria.unidade = aux[1].split('#')[1];
	        				vm.item.unidadeJudiciaria.coj = aux[1].split('#')[0];
	        				break;
        			}
        		}
/*        		'i.serial:'
            	'i.id:'
            	'i.modelo:'
            	'i.estado:'
            	'i.numero:'
            	'i.tipoItem:'
            	'p.nome:'*/
        	}
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
               vm.isPagination = item.isPagination == 'true' ? true : false;
               vm.search();
               vm.transition();
               //vm.loadAll(item);
            }, function(result) 
            {
            	if(result == 'limpar')
            	{
            		vm.item = {};
            		//vm.item.item = null;
            		//vm.item.unidadeJudiciaria = null;
                    vm.isPagination = item.isPagination == 'true' ? true : false;
                    vm.search();
                    vm.transition();
            	}
            });
        }
        
        function transition () {
            $state.transitionTo($state.$current, {
            	filtro: vm.filtro,
            	pagination: vm.isPagination ? 'true' : 'false',
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }

    }
})();
