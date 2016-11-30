(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'olData'];

    function HomeController ($scope, Principal, LoginService, $state, olData) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.selecionamunicipio = "";
        vm.feature_mousemove = null;
        vm.mouseMoveCountry = null;
        vm.map = null;
         
        vm.olcenter = 
        {
            lat: -13.475105944334956,
            lon: -41.7919921875,
            zoom: 6,
            bounds: [],
            projection: "EPSG:4326"
        };
        
        //#################
        
        
        //#################
        
        ///////////////////////////////////////////////////////
        
        vm.simulateQuery = false;
        vm.isDisabled    = false;

        vm.querySearch   = querySearch;
        vm.selectedItemChange = selectedItemChange;
        vm.searchTextChange   = searchTextChange;

        vm.newState = newState;

        function newState(state) {
          alert("Sorry! You'll need to create a Constitution for " + state + " first!");
        }

        // ******************************
        // Internal methods
        // ******************************

        /**
         * Search for states... use $timeout to simulate
         * remote dataservice call.
         */
        function querySearch (query) {
          var results = query ? vm.selectmunicipio.filter( createFilterFor(query) ) : vm.selectmunicipio,
              deferred;
          if (vm.simulateQuery) {
            deferred = $q.defer();
            $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
            return deferred.promise;
          } else {
            return results;
          }
        }

        function searchTextChange(text) {
        	console.log('Text changed to ' + text);
        }

        function selectedItemChange(item) {
        	var layers = vm.map.getLayers();
            
            layers = vm.map.getLayers().getArray();
            
            for (var i = 0; i < layers.length; i++) {
            	var layer = layers[i];
            	if(layer.get('name') === 'vetormunicipio')
            	{
            		var features = layer.getSource().getFeatures();
            		for (var j = 0; j < features.length; j++) 
            		{
            			var feature = features[j];
            			
            			if(item.id === feature.getProperties().id)
        				{
            				var extent = feature.getGeometry().getExtent();
            				vm.map.getView().fit(extent, vm.map.getSize());
            				vm.municipioba.source.params.viewparams= 'muid:'+item.id;
        				}
            		}
            	}
            }
        }

        /**
         * Create filter function for a query string
         */
        function createFilterFor(query) {
          var lowercaseQuery = angular.lowercase(query);

          return function filterFn(state) {
            return (state.nome.toLowerCase().indexOf(lowercaseQuery) === 0);
          };

        }
        
        /////////////////////////////////////////////////////////
        
        
        
		vm.setZoomMunicipio = function(m)
		{
			if(this.selecionamunicipio)
			{
				//this.center.lat = this.selecionamunicipio.lat;
				//this.center.lon = this.selecionamunicipio.lon;
				//this.center.zoom = 9;
				// olData.getMap().then(function(map) {
		                var layers = this.map.getLayers();
		                
		                var layers = this.map.getLayers().getArray();
		                var vmA = {ma: m};
		                for (var i = 0; i < layers.length; i++) {
		                	var layer = layers[i];
		                	if(layer.get('name') === 'vetormunicipio')
		                	{
		                		var features = layer.getSource().getFeatures();
		                		for (var j = 0; j < features.length; j++) 
		                		{
		                			var feature = features[j];
		                			
		                			if(m.id === feature.getProperties().id)
	                				{
		                				var extent = feature.getGeometry().getExtent();
										this.map.getView().fit(extent, this.map.getSize());
	                				}
		                		}
		                	}
		                }
		                
		                /*layers.forEach(function(layer) {
		                    if (layer.get('name') === 'vetormunicipio') 
		                    {
		                    	var souces = layer.getSource();
		                    	souces.forEachFeature(function(feature) 
		                    	{
		                    		//if(feature.getProperties().id === )
									var extent = feature.getGeometry().getExtent();
									console.log(extent);
									//this.map.getView().fit(extent, this.map.getSize());
		                    	});
		                    }
		                });*/
		          //  });

				this.municipioba.source.params.viewparams= 'muid:'+this.selecionamunicipio.id;
			}
		}
        vm.selectmunicipio = 
        	[
        		{id:1826,nome:"Abaíra", lat:-13.2868987430714, lon:-41.7312149352015},
        		{id:1827,nome:"Abaré", lat:-8.83246875887995, lon:-39.3317507343523},
        		{id:1828,nome:"Acajutiba", lat:-11.6747446202371, lon:-37.9955847425154},
        		{id:1829,nome:"Adustina", lat:-10.5622896382662, lon:-38.036668967719},
        		{id:1830,nome:"Água Fria", lat:-11.8306100403768, lon:-38.7242379039885},
        		{id:1832,nome:"Aiquara", lat:-14.0946387369204, lon:-39.8912288296819},
        		{id:1833,nome:"Alagoinhas", lat:-12.110487046237, lon:-38.4032729879922},
        		{id:1834,nome:"Alcobaça", lat:-17.4737356008718, lon:-39.4051671892604},
        		{id:1835,nome:"Almadina", lat:-14.7033934072848, lon:-39.6630838094092},
        		{id:1836,nome:"Amargosa", lat:-13.0377746345394, lon:-39.6345144282098},
        		{id:1837,nome:"Amélia Rodrigues", lat:-12.4205146867332, lon:-38.7112829113905},
        		{id:1838,nome:"América Dourada", lat:-11.4119508892696, lon:-41.5277284573601},
        		{id:1839,nome:"Anagé", lat:-14.5891416534487, lon:-40.9247202001849},
        		{id:1840,nome:"Andaraí", lat:-12.7594959886519, lon:-41.1950904396404},
        		{id:1841,nome:"Andorinha", lat:-10.2903233129621, lon:-39.8918788783551},
        		{id:1842,nome:"Angical", lat:-11.9983821576385, lon:-44.6351843451138},
        		{id:1843,nome:"Anguera", lat:-12.1812371125296, lon:-39.1956111428594},
        		{id:1844,nome:"Antas", lat:-10.4074424450252, lon:-38.2944017451744},
        		{id:1845,nome:"Antônio Cardoso", lat:-12.3804508315853, lon:-39.1468980756189},
        		{id:1846,nome:"Antônio Gonçalves", lat:-10.5905087825922, lon:-40.3598283653938},
        		{id:1847,nome:"Aporá", lat:-11.7238259666391, lon:-38.1985501623889},
        		{id:1848,nome:"Apuarema", lat:-13.8249897415324, lon:-39.7356153232397},
        		{id:1850,nome:"Araças", lat:-12.1775081795935, lon:-38.2141781328263},
        		{id:1849,nome:"Aracatu", lat:-14.3647333382605, lon:-41.3752273437035},
        		{id:1851,nome:"Araci", lat:-11.2140530377636, lon:-39.0757712023075},
        		{id:1852,nome:"Aramari", lat:-12.0482015466658, lon:-38.5248281414344},
        		{id:1853,nome:"Arataca", lat:-15.2357349314213, lon:-39.4182850616447},
        		{id:1854,nome:"Aratuípe", lat:-13.0852250380229, lon:-39.0721853212695},
        		{id:1855,nome:"Aurelino Leal", lat:-14.3634618220401, lon:-39.4797179169052},
        		{id:1856,nome:"Baianópolis", lat:-12.6632637685328, lon:-44.4455344243547},
        		{id:1857,nome:"Baixa Grande", lat:-11.964332782862, lon:-40.1543266674828},
        		{id:1858,nome:"Banzaê", lat:-10.6194636279924, lon:-38.6279039763938},
        		{id:1859,nome:"Barra", lat:-11.0075663564265, lon:-43.3683872724308},
        		{id:1860,nome:"Barra da Estiva", lat:-13.6442520136256, lon:-41.144310945485},
        		{id:1861,nome:"Barra do Choça", lat:-14.9121318203403, lon:-40.5861490108583},
        		{id:1862,nome:"Barra do Mendes", lat:-11.9768283923668, lon:-42.1128480694045},
        		{id:1863,nome:"Barra do Rocha", lat:-14.0861142427767, lon:-39.6047678734296},
        		{id:1864,nome:"Barreiras", lat:-12.0184432930667, lon:-45.5223612529395},
        		{id:1865,nome:"Barro Alto", lat:-11.8488379527387, lon:-41.8361821665688},
        		{id:1867,nome:"Barro Preto", lat:-14.7612074984687, lon:-39.4210306334601},
        		{id:1866,nome:"Barrocas", lat:-11.5362517769309, lon:-39.0972377733682},
        		{id:1868,nome:"Belmonte", lat:-15.9365196257478, lon:-39.1858873732278},
        		{id:1869,nome:"Belo Campo", lat:-14.9794337243358, lon:-41.2123655127923},
        		{id:1870,nome:"Biritinga", lat:-11.5605987067738, lon:-38.7943135811249},
        		{id:1871,nome:"Boa Nova", lat:-14.3593476537705, lon:-40.2375337833453},
        		{id:1872,nome:"Boa Vista do Tupim", lat:-12.7831619847615, lon:-40.6505887919672},
        		{id:1873,nome:"Bom Jesus da Lapa", lat:-13.2800665977319, lon:-43.284213168493},
        		{id:1874,nome:"Bom Jesus da Serra", lat:-14.3860711984499, lon:-40.5760117209271},
        		{id:1875,nome:"Boninal", lat:-12.7907721829028, lon:-41.7989628728567},
        		{id:1876,nome:"Bonito", lat:-11.9785641176903, lon:-41.3235993042127},
        		{id:1877,nome:"Boquira", lat:-12.7446547698409, lon:-42.7204321212869},
        		{id:1878,nome:"Botuporã", lat:-13.3237803287303, lon:-42.5366799505202},
        		{id:1879,nome:"Brejões", lat:-13.0762630635094, lon:-39.8575044804641},
        		{id:1880,nome:"Brejolândia", lat:-12.5071027785471, lon:-43.7894243068777},
        		{id:1881,nome:"Brotas de Macaúbas", lat:-12.0891611896084, lon:-42.4488903359542},
        		{id:1882,nome:"Brumado", lat:-14.171327064415, lon:-41.6823551957477},
        		{id:1883,nome:"Buerarema", lat:-14.9960610686142, lon:-39.284469409324},
        		{id:1884,nome:"Buritirama", lat:-10.5732635998184, lon:-43.6852909318565},
        		{id:1885,nome:"Caatiba", lat:-15.0037794598928, lon:-40.3362475950102},
        		{id:1886,nome:"Cabaceiras do Paraguaçu", lat:-12.5648051191359, lon:-39.1937029606872},
        		{id:1887,nome:"Cachoeira", lat:-12.640393579104, lon:-38.8850880735924},
        		{id:1888,nome:"Caculé", lat:-14.4764891064791, lon:-42.2917045354888},
        		{id:1889,nome:"Caém", lat:-11.1480688666832, lon:-40.2904022854183},
        		{id:1890,nome:"Caetanos", lat:-14.3154412758757, lon:-40.9132498976832},
        		{id:1891,nome:"Caetité", lat:-13.9764520494331, lon:-42.4959694767377},
        		{id:1892,nome:"Cafarnaum", lat:-11.7377369611117, lon:-41.4829075809483},
        		{id:1893,nome:"Cairu", lat:-13.508279260164, lon:-38.9819452307025},
        		{id:1894,nome:"Caldeirão Grande", lat:-11.0427719441522, lon:-40.2208675854661},
        		{id:1895,nome:"Camacan", lat:-15.4448698515479, lon:-39.5193596760877},
        		{id:1896,nome:"Camaçari", lat:-12.6651949596358, lon:-38.2082622346502},
        		{id:1897,nome:"Camamu", lat:-14.0147678800352, lon:-39.1790250023934},
        		{id:1898,nome:"Campo Alegre de Lourdes", lat:-9.55020884850667, lon:-43.1726929263794},
        		{id:1899,nome:"Campo Formoso", lat:-10.2515519171292, lon:-40.7211786918589},
        		{id:1900,nome:"Canápolis", lat:-13.0910738379045, lon:-44.2111660025349},
        		{id:1901,nome:"Canarana", lat:-11.7568491446962, lon:-41.6975525476268},
        		{id:1902,nome:"Canavieiras", lat:-15.6541808214416, lon:-39.120247065937},
        		{id:1903,nome:"Candeal", lat:-11.8695607315651, lon:-39.190364150329},
        		{id:1904,nome:"Candeias", lat:-12.6759808738829, lon:-38.4845754080427},
        		{id:1905,nome:"Candiba", lat:-14.4051065399591, lon:-42.8308207612294},
        		{id:1906,nome:"Cândido Sales", lat:-15.3240713444619, lon:-41.3282522052687},
        		{id:1907,nome:"Cansanção", lat:-10.7307055503146, lon:-39.4722276073345},
        		{id:1908,nome:"Canudos", lat:-10.0714475319546, lon:-38.9846206904007},
        		{id:1909,nome:"Capela do Alto Alegre", lat:-11.6943443849578, lon:-39.8120767530647},
        		{id:1910,nome:"Capim Grosso", lat:-11.3231871031292, lon:-39.9835631477865},
        		{id:1911,nome:"Caraíbas", lat:-14.6256045577008, lon:-41.2787135400131},
        		{id:1912,nome:"Caravelas", lat:-17.6592805855837, lon:-39.6879322840752},
        		{id:1913,nome:"Cardeal da Silva", lat:-12.038061390048, lon:-37.9379787933521},
        		{id:1914,nome:"Carinhanha", lat:-13.9978922377154, lon:-43.8519679670517},
        		{id:1915,nome:"Casa Nova", lat:-9.24004077918757, lon:-41.336738695328},
        		{id:1916,nome:"Castro Alves", lat:-12.7449022867545, lon:-39.3673705690279},
        		{id:1917,nome:"Catolândia", lat:-12.2853026715548, lon:-44.7119795223107},
        		{id:1918,nome:"Catu", lat:-12.3327321999728, lon:-38.4169375636219},
        		{id:1919,nome:"Caturama", lat:-13.2311750832531, lon:-42.3138700787931},
        		{id:1920,nome:"Central", lat:-11.1547076384814, lon:-42.1039038613933},
        		{id:1921,nome:"Chorrochó", lat:-9.24445988304104, lon:-39.1646682314571},
        		{id:1922,nome:"Cícero Dantas", lat:-10.5465705325025, lon:-38.421762341943},
        		{id:1923,nome:"Cipó", lat:-11.1144887564944, lon:-38.5090645039157},
        		{id:1924,nome:"Coaraci", lat:-14.6430744861181, lon:-39.5900741635597},
        		{id:1925,nome:"Cocos", lat:-14.5005203377554, lon:-45.2248251283052},
        		{id:1926,nome:"Conceição da Feira", lat:-12.5071159419011, lon:-38.9993677557411},
        		{id:1927,nome:"Conceição do Almeida", lat:-12.8563351641756, lon:-39.241170270596},
        		{id:1928,nome:"Conceição do Coité", lat:-11.5226391656519, lon:-39.2658520615534},
        		{id:1929,nome:"Conceição do Jacuípe", lat:-12.3375431700331, lon:-38.7405212667004},
        		{id:1930,nome:"Conde", lat:-11.828344923561, lon:-37.6803172993689},
        		{id:1931,nome:"Condeúba", lat:-14.9277873730336, lon:-42.0496599290638},
        		{id:1932,nome:"Contendas do Sincorá", lat:-13.8166046818073, lon:-41.0951498025735},
        		{id:1933,nome:"Coração de Maria", lat:-12.2190640733017, lon:-38.7824056420798},
        		{id:1934,nome:"Cordeiros", lat:-15.0383857309112, lon:-41.8957780197676},
        		{id:1935,nome:"Coribe", lat:-13.7822534307032, lon:-44.391526928959},
        		{id:1936,nome:"Coronel João Sá", lat:-10.2688612126676, lon:-37.9517224010846},
        		{id:1937,nome:"Correntina", lat:-13.4777546306109, lon:-45.4071665610629},
        		{id:1938,nome:"Cotegipe", lat:-11.7080903836281, lon:-44.3288696689261},
        		{id:1939,nome:"Cravolândia", lat:-13.4269131171278, lon:-39.8015768806901},
        		{id:1940,nome:"Crisópolis", lat:-11.5072845942239, lon:-38.1682883115843},
        		{id:1941,nome:"Cristópolis", lat:-12.1943212929877, lon:-44.2614295940985},
        		{id:1942,nome:"Cruz das Almas", lat:-12.6860795168888, lon:-39.1198047557517},
        		{id:1943,nome:"Curaçá", lat:-9.26323066949592, lon:-39.640200875052},
        		{id:1944,nome:"Dário Meira", lat:-14.4040335214755, lon:-39.9834876159533},
        		{id:1945,nome:"Dias d'Ávila", lat:-12.6004690772172, lon:-38.2850240885333},
        		{id:1946,nome:"Dom Basílio", lat:-13.8565888158139, lon:-41.7083452514078},
        		{id:1947,nome:"Dom Macedo Costa", lat:-12.9144959051663, lon:-39.1713981489617},
        		{id:1948,nome:"Elísio Medrado", lat:-12.9319544926939, lon:-39.5351344414986},
        		{id:1949,nome:"Encruzilhada", lat:-15.5846476373815, lon:-40.9829421412331},
        		{id:1950,nome:"Entre Rios", lat:-12.074878367783, lon:-38.0410411160486},
        		{id:1831,nome:"Érico Cardoso", lat:-13.4131076034469, lon:-42.0627043824759},
        		{id:1951,nome:"Esplanada", lat:-11.9372468224725, lon:-37.8873499783576},
        		{id:1952,nome:"Euclides da Cunha", lat:-10.4573145711385, lon:-38.8740746473387},
        		{id:1953,nome:"Eunápolis", lat:-16.2806895239446, lon:-39.604529380851},
        		{id:1954,nome:"Fátima", lat:-10.5934430631625, lon:-38.2005813932621},
        		{id:1955,nome:"Feira da Mata", lat:-14.0519468135258, lon:-44.1982362401713},
        		{id:1956,nome:"Feira de Santana", lat:-12.1891900868199, lon:-39.0360423219127},
        		{id:1957,nome:"Filadélfia", lat:-10.720549166433, lon:-40.1457601165474},
        		{id:1958,nome:"Firmino Alves", lat:-14.9236373891835, lon:-39.9115937428301},
        		{id:1959,nome:"Floresta Azul", lat:-14.8286616676092, lon:-39.7322383558717},
        		{id:1960,nome:"Formosa do Rio Preto", lat:-11.0205194272864, lon:-45.7610972295937},
        		{id:1961,nome:"Gandu", lat:-13.7854803929321, lon:-39.4756257519768},
        		{id:1962,nome:"Gavião", lat:-11.5062999133769, lon:-39.7452630934824},
        		{id:1963,nome:"Gentio do Ouro", lat:-11.4082101298017, lon:-42.5987580526514},
        		{id:1964,nome:"Glória", lat:-9.19321171390386, lon:-38.433943985325},
        		{id:1965,nome:"Gongogi", lat:-14.2811927609997, lon:-39.57733246529},
        		{id:1966,nome:"Governador Mangabeira", lat:-12.5827460130477, lon:-39.0715942906972},
        		{id:1967,nome:"Guajeru", lat:-14.5798521837011, lon:-42.0233547604735},
        		{id:1968,nome:"Guanambi", lat:-14.205331017077, lon:-42.8215097142069},
        		{id:1969,nome:"Guaratinga", lat:-16.542669471031, lon:-39.9260976713633},
        		{id:1970,nome:"Heliópolis", lat:-10.7491083760483, lon:-38.3088674708285},
        		{id:1971,nome:"Iaçu", lat:-12.8166182625651, lon:-40.1073953359516},
        		{id:1972,nome:"Ibiassucê", lat:-14.2795225751191, lon:-42.2994317484147},
        		{id:1973,nome:"Ibicaraí", lat:-14.8523362434642, lon:-39.5680203644615},
        		{id:1974,nome:"Ibicoara", lat:-13.3855752285582, lon:-41.3427723681913},
        		{id:1975,nome:"Ibicuí", lat:-14.6410198001749, lon:-39.8663730975638},
        		{id:1976,nome:"Ibipeba", lat:-11.5736413580954, lon:-42.1805215255073},
        		{id:1977,nome:"Ibipitanga", lat:-12.8589080621975, lon:-42.3615544072182},
        		{id:1978,nome:"Ibiquera", lat:-12.6092675893027, lon:-40.8411951437473},
        		{id:1979,nome:"Ibirapitanga", lat:-14.0613917794536, lon:-39.4303588158089},
        		{id:1980,nome:"Ibirapuã", lat:-17.7417170409404, lon:-40.0172167745052},
        		{id:1981,nome:"Ibirataia", lat:-13.9701038601682, lon:-39.6439536794529},
        		{id:1982,nome:"Ibitiara", lat:-12.5761081025985, lon:-42.2839133322629},
        		{id:1983,nome:"Ibititá", lat:-11.5764021241409, lon:-41.9167944334393},
        		{id:1984,nome:"Ibotirama", lat:-12.037313530073, lon:-43.2019330632524},
        		{id:1985,nome:"Ichu", lat:-11.7272603023217, lon:-39.153460000382},
        		{id:1986,nome:"Igaporã", lat:-13.8732424640641, lon:-42.7018305153677},
        		{id:1987,nome:"Igrapiúna", lat:-13.8596009368697, lon:-39.1700310023216},
        		{id:1988,nome:"Iguaí", lat:-14.628542883739, lon:-40.0682728506024},
        		{id:1989,nome:"Ilhéus", lat:-14.7452688413467, lon:-39.1967720544791},
        		{id:1990,nome:"Inhambupe", lat:-11.790349163253, lon:-38.4174414283017},
        		{id:1991,nome:"Ipecaetá", lat:-12.3022331251706, lon:-39.3305518091921},
        		{id:1992,nome:"Ipiaú", lat:-14.0605472017171, lon:-39.7235044191017},
        		{id:1993,nome:"Ipirá", lat:-12.2001060412005, lon:-39.7825757311364},
        		{id:1994,nome:"Ipupiara", lat:-11.8425786776628, lon:-42.4784636019312},
        		{id:1995,nome:"Irajuba", lat:-13.2173502347, lon:-40.0429645515475},
        		{id:1996,nome:"Iramaia", lat:-13.4965782761886, lon:-40.9106424898763},
        		{id:1997,nome:"Iraquara", lat:-12.2430847772391, lon:-41.5800416867485},
        		{id:1998,nome:"Irará", lat:-12.050096253996, lon:-38.7565153517219},
        		{id:1999,nome:"Irecê", lat:-11.3131314130798, lon:-41.8410716764004},
        		{id:2000,nome:"Itabela", lat:-16.6727620128529, lon:-39.5568472338544},
        		{id:2001,nome:"Itaberaba", lat:-12.4976781452549, lon:-40.2603812249886},
        		{id:2002,nome:"Itabuna", lat:-14.8491920280515, lon:-39.3288157399292},
        		{id:2003,nome:"Itacaré", lat:-14.3609789927088, lon:-39.1590850993411},
        		{id:2004,nome:"Itaeté", lat:-13.0841726315759, lon:-41.0232480258341},
        		{id:2005,nome:"Itagi", lat:-14.1517629891816, lon:-40.0293320729102},
        		{id:2006,nome:"Itagibá", lat:-14.2522018354685, lon:-39.8284818954667},
        		{id:2007,nome:"Itagimirim", lat:-16.0986607168516, lon:-39.7482828981884},
        		{id:2008,nome:"Itaguaçu da Bahia", lat:-10.7772673771296, lon:-42.228181498511},
        		{id:2009,nome:"Itaju do Colônia", lat:-15.1492563034191, lon:-39.7329583024426},
        		{id:2010,nome:"Itajuípe", lat:-14.6833239110131, lon:-39.4521401586022},
        		{id:2011,nome:"Itamaraju", lat:-17.0042796113205, lon:-39.7634840117683},
        		{id:2012,nome:"Itamari", lat:-13.7685841182963, lon:-39.6596364444796},
        		{id:2013,nome:"Itambé", lat:-15.1616799235569, lon:-40.4880733773982},
        		{id:2014,nome:"Itanagra", lat:-12.3115126661911, lon:-38.053369939551},
        		{id:2015,nome:"Itanhém", lat:-17.1217896627765, lon:-40.3769270351114},
        		{id:2016,nome:"Itaparica", lat:-12.8907518929662, lon:-38.6592106452352},
        		{id:2017,nome:"Itapé", lat:-14.9526482564462, lon:-39.5022282246568},
        		{id:2018,nome:"Itapebi", lat:-15.9059678241509, lon:-39.6672675219105},
        		{id:2019,nome:"Itapetinga", lat:-15.3042810928949, lon:-40.0539453695965},
        		{id:2020,nome:"Itapicuru", lat:-11.1901186201417, lon:-38.194462157298},
        		{id:2021,nome:"Itapitanga", lat:-14.4740976979575, lon:-39.6216913704173},
        		{id:2022,nome:"Itaquara", lat:-13.4534831237186, lon:-39.8949086004938},
        		{id:2023,nome:"Itarantim", lat:-15.6856654968282, lon:-40.0386818183618},
        		{id:2024,nome:"Itatim", lat:-12.7246405650805, lon:-39.7414666466225},
        		{id:2025,nome:"Itiruçu", lat:-13.4856687070708, lon:-40.1510459770608},
        		{id:2026,nome:"Itiúba", lat:-10.7301813226559, lon:-39.8458008463805},
        		{id:2027,nome:"Itororó", lat:-15.0462225181889, lon:-40.0056896928969},
        		{id:2028,nome:"Ituaçu", lat:-13.837317341955, lon:-41.385952501781},
        		{id:2029,nome:"Ituberá", lat:-13.7496146906418, lon:-39.1489501731156},
        		{id:2030,nome:"Iuiú", lat:-14.5086502206231, lon:-43.5614994270495},
        		{id:2031,nome:"Jaborandi", lat:-14.0466934019299, lon:-45.4404910875389},
        		{id:2032,nome:"Jacaraci", lat:-14.7751627586807, lon:-42.3089704452113},
        		{id:2033,nome:"Jacobina", lat:-11.1373814158423, lon:-40.5601669634567},
        		{id:2034,nome:"Jaguaquara", lat:-13.5457162514461, lon:-39.9626107508333},
        		{id:2035,nome:"Jaguarari", lat:-10.0405876577442, lon:-40.0907079063753},
        		{id:2036,nome:"Jaguaripe", lat:-13.1155732412188, lon:-38.9823134451918},
        		{id:2037,nome:"Jandaíra", lat:-11.5977948826282, lon:-37.5959902309858},
        		{id:2038,nome:"Jequié", lat:-13.8726895501314, lon:-40.1597558469836},
        		{id:2039,nome:"Jeremoabo", lat:-9.93886938332743, lon:-38.5497551556344},
        		{id:2040,nome:"Jiquiriçá", lat:-13.3304837769783, lon:-39.5866743820118},
        		{id:2041,nome:"Jitaúna", lat:-13.9416620334445, lon:-39.8858115328564},
        		{id:2042,nome:"João Dourado", lat:-11.2257862112548, lon:-41.5687118513015},
        		{id:2043,nome:"Juazeiro", lat:-9.54420828083018, lon:-40.2732894828643},
        		{id:2044,nome:"Jucuruçu", lat:-16.8466488949496, lon:-40.1051672861054},
        		{id:2045,nome:"Jussara", lat:-10.9636915287666, lon:-41.8611042714798},
        		{id:2046,nome:"Jussari", lat:-15.132333367613, lon:-39.4977196956178},
        		{id:2047,nome:"Jussiape", lat:-13.5187057275416, lon:-41.5989032236332},
        		{id:2048,nome:"Lafaiete Coutinho", lat:-13.627231864382, lon:-40.2748002191811},
        		{id:2049,nome:"Lagoa Real", lat:-14.0393722327639, lon:-42.2337062952686},
        		{id:2050,nome:"Laje", lat:-13.1650807238103, lon:-39.3456601512811},
        		{id:2051,nome:"Lajedão", lat:-17.5547390190628, lon:-40.3119666960913},
        		{id:2052,nome:"Lajedinho", lat:-12.3832029743366, lon:-40.9719709353937},
        		{id:2053,nome:"Lajedo do Tabocal", lat:-13.4645779927719, lon:-40.2782031056305},
        		{id:2054,nome:"Lamarão", lat:-11.8017783599561, lon:-38.9558774699816},
        		{id:2055,nome:"Lapão", lat:-11.4990207384886, lon:-41.7856139208236},
        		{id:2056,nome:"Lauro de Freitas", lat:-12.8583757358458, lon:-38.3310549416366},
        		{id:2057,nome:"Lençóis", lat:-12.4206739531099, lon:-41.3351334602673},
        		{id:2058,nome:"Licínio de Almeida", lat:-14.5914765430449, lon:-42.4665086562959},
        		{id:2059,nome:"Livramento de Nossa Senhora", lat:-13.8091800946869, lon:-42.0092627818951},
        		{id:2060,nome:"Luís Eduardo Magalhães", lat:-12.2009141833033, lon:-46.0148716970764},
        		{id:2061,nome:"Macajuba", lat:-12.1299323291621, lon:-40.2645448875614},
        		{id:2062,nome:"Macarani", lat:-15.5509120429417, lon:-40.3829815476461},
        		{id:2063,nome:"Macaúbas", lat:-13.1751283731764, lon:-42.7103108670583},
        		{id:2064,nome:"Macururé", lat:-9.2765080334682, lon:-38.9127820269128},
        		{id:2065,nome:"Madre de Deus", lat:-12.7381896377183, lon:-38.6273807422882},
        		{id:2066,nome:"Maetinga", lat:-14.6598461348809, lon:-41.517579979209},
        		{id:2067,nome:"Maiquinique", lat:-15.6979609179328, lon:-40.2672842074956},
        		{id:2068,nome:"Mairi", lat:-11.7304169336215, lon:-40.1768670918018},
        		{id:2069,nome:"Malhada", lat:-14.2114472163629, lon:-43.6510181448896},
        		{id:2070,nome:"Malhada de Pedras", lat:-14.3485151164701, lon:-41.836270868482},
        		{id:2071,nome:"Manoel Vitorino", lat:-14.0490202402408, lon:-40.5654051794236},
        		{id:2072,nome:"Mansidão", lat:-11.1264311460723, lon:-44.1083870996279},
        		{id:2073,nome:"Maracás", lat:-13.4974315180589, lon:-40.5532811494912},
        		{id:2074,nome:"Maragogipe", lat:-12.8214458186687, lon:-38.9382484561795},
        		{id:2075,nome:"Maraú", lat:-14.1552858042834, lon:-39.1510862358518},
        		{id:2076,nome:"Marcionílio Souza", lat:-13.1416849423393, lon:-40.6653828100904},
        		{id:2077,nome:"Mascote", lat:-15.658283880012, lon:-39.4235899143734},
        		{id:2078,nome:"Mata de São João", lat:-12.4762016117096, lon:-38.1457598412893},
        		{id:2079,nome:"Matina", lat:-13.8494601167013, lon:-42.9244390764061},
        		{id:2080,nome:"Medeiros Neto", lat:-17.364802844083, lon:-40.2847759074045},
        		{id:2081,nome:"Miguel Calmon", lat:-11.4208493686875, lon:-40.6112596103021},
        		{id:2082,nome:"Milagres", lat:-12.9256408769455, lon:-39.7740196376967},
        		{id:2083,nome:"Mirangaba", lat:-10.7814116242726, lon:-40.7500855789188},
        		{id:2084,nome:"Mirante", lat:-14.1790715312747, lon:-40.7501521667943},
        		{id:2085,nome:"Monte Santo", lat:-10.3640428355543, lon:-39.4758848856887},
        		{id:2086,nome:"Morpará", lat:-11.7746482982297, lon:-43.0258716032309},
        		{id:2087,nome:"Morro do Chapéu", lat:-11.4731237952549, lon:-41.1514727207644},
        		{id:2088,nome:"Mortugaba", lat:-14.9630383355783, lon:-42.3361825673623},
        		{id:2089,nome:"Mucugê", lat:-13.0625580428782, lon:-41.473133008507},
        		{id:2090,nome:"Mucuri", lat:-18.0595772145052, lon:-39.8677290156722},
        		{id:2091,nome:"Mulungu do Morro", lat:-11.9837986014212, lon:-41.5192789502797},
        		{id:2092,nome:"Mundo Novo", lat:-11.9498905724136, lon:-40.5561393034251},
        		{id:2093,nome:"Muniz Ferreira", lat:-12.9977578567797, lon:-39.0889187593475},
        		{id:2094,nome:"Muquém de São Francisco", lat:-12.1593953137105, lon:-43.5138061052895},
        		{id:2095,nome:"Muritiba", lat:-12.6201026241527, lon:-39.0907986366756},
        		{id:2096,nome:"Mutuípe", lat:-13.311170279159, lon:-39.5069658654774},
        		{id:2097,nome:"Nazaré", lat:-12.9478266198089, lon:-38.9924036862256},
        		{id:2098,nome:"Nilo Peçanha", lat:-13.6591178823166, lon:-39.1817058209759},
        		{id:2099,nome:"Nordestina", lat:-10.8763807831368, lon:-39.4528395426944},
        		{id:2100,nome:"Nova Canaã", lat:-14.8424106812748, lon:-40.1861704435338},
        		{id:2101,nome:"Nova Fátima", lat:-11.6216503527585, lon:-39.6371550679257},
        		{id:2102,nome:"Nova Ibiá", lat:-13.8175444845973, lon:-39.5855472918506},
        		{id:2103,nome:"Nova Itarana", lat:-13.0512981119837, lon:-40.0299712525503},
        		{id:2104,nome:"Nova Redenção", lat:-12.8379403646595, lon:-41.108847370302},
        		{id:2105,nome:"Nova Soure", lat:-11.3064954771006, lon:-38.5567408775292},
        		{id:2106,nome:"Nova Viçosa", lat:-17.9003192563312, lon:-39.7157290887185},
        		{id:2107,nome:"Novo Horizonte", lat:-12.8465088992154, lon:-42.1327984916095},
        		{id:2108,nome:"Novo Triunfo", lat:-10.3835290868653, lon:-38.4309987052241},
        		{id:2109,nome:"Olindina", lat:-11.419711748411, lon:-38.3539614616578},
        		{id:2110,nome:"Oliveira dos Brejinhos", lat:-12.2611083449267, lon:-42.7444847306941},
        		{id:2111,nome:"Ouriçangas", lat:-12.0038867912977, lon:-38.6592486598456},
        		{id:2112,nome:"Ourolândia", lat:-10.8628818687643, lon:-41.1333819618974},
        		{id:2113,nome:"Palmas de Monte Alto", lat:-14.1631695007305, lon:-43.2455709860068},
        		{id:2114,nome:"Palmeiras", lat:-12.5466102523066, lon:-41.5584257947445},
        		{id:2115,nome:"Paramirim", lat:-13.5134972690211, lon:-42.2578974029785},
        		{id:2116,nome:"Paratinga", lat:-12.6845487457992, lon:-43.0674896199934},
        		{id:2117,nome:"Paripiranga", lat:-10.6176164224932, lon:-37.9014900700489},
        		{id:2118,nome:"Pau Brasil", lat:-15.4578520140141, lon:-39.6964107894551},
        		{id:2119,nome:"Paulo Afonso", lat:-9.53814676949774, lon:-38.2957645267059},
        		{id:2120,nome:"Pé de Serra", lat:-11.8617028925676, lon:-39.6085475242255},
        		{id:2121,nome:"Pedrão", lat:-12.1247594790099, lon:-38.6406870645084},
        		{id:2122,nome:"Pedro Alexandre", lat:-10.0566764027152, lon:-37.9574953502808},
        		{id:2123,nome:"Piatã", lat:-13.0376374672442, lon:-41.8792111092754},
        		{id:2124,nome:"Pilão Arcado", lat:-10.0093093408774, lon:-43.1106968818358},
        		{id:2125,nome:"Pindaí", lat:-14.4739034095208, lon:-42.6731684051272},
        		{id:2126,nome:"Pindobaçu", lat:-10.7174852330408, lon:-40.3445264571556},
        		{id:2127,nome:"Pintadas", lat:-11.8716135678945, lon:-39.9063865763462},
        		{id:2128,nome:"Piraí do Norte", lat:-13.8101741529274, lon:-39.3644067788788},
        		{id:2129,nome:"Piripá", lat:-14.9927511488577, lon:-41.7158794472057},
        		{id:2130,nome:"Piritiba", lat:-11.693487878965, lon:-40.6145683029339},
        		{id:2131,nome:"Planaltino", lat:-13.1967266294336, lon:-40.2843866455377},
        		{id:2132,nome:"Planalto", lat:-14.7102875569423, lon:-40.4682718955034},
        		{id:2133,nome:"Poções", lat:-14.5363516238362, lon:-40.3616739371611},
        		{id:2134,nome:"Pojuca", lat:-12.3549662568586, lon:-38.2501336586874},
        		{id:2135,nome:"Ponto Novo", lat:-10.9551544579782, lon:-40.1220223060179},
        		{id:2136,nome:"Porto Seguro", lat:-16.6198694531203, lon:-39.2902520494692},
        		{id:2137,nome:"Potiraguá", lat:-15.705363701733, lon:-39.7654650534575},
        		{id:2138,nome:"Prado", lat:-17.1321458621999, lon:-39.3520676047438},
        		{id:2139,nome:"Presidente Dutra", lat:-11.292716825312, lon:-41.9953907721405},
        		{id:2140,nome:"Presidente Jânio Quadros", lat:-14.6885472511891, lon:-41.7466264949848},
        		{id:2141,nome:"Presidente Tancredo Neves", lat:-13.445550204557, lon:-39.4240121895781},
        		{id:2142,nome:"Queimadas", lat:-11.0560731513473, lon:-39.7660826640034},
        		{id:2143,nome:"Quijingue", lat:-10.7711566909489, lon:-39.0545216603756},
        		{id:2144,nome:"Quixabeira", lat:-11.3961425835155, lon:-40.1186008268065},
        		{id:2145,nome:"Rafael Jambeiro", lat:-12.4698937831679, lon:-39.5855603033978},
        		{id:2146,nome:"Remanso", lat:-9.60069750064877, lon:-42.2554515858817},
        		{id:2147,nome:"Retirolândia", lat:-11.4840157027904, lon:-39.4031401145391},
        		{id:2148,nome:"Riachão das Neves", lat:-11.6254464606439, lon:-45.182745384705},
        		{id:2149,nome:"Riachão do Jacuípe", lat:-11.8128900287315, lon:-39.4246488219151},
        		{id:2150,nome:"Riacho de Santana", lat:-13.6791829306261, lon:-43.1065346013655},
        		{id:2151,nome:"Ribeira do Amparo", lat:-10.9721492024242, lon:-38.3845428247083},
        		{id:2152,nome:"Ribeira do Pombal", lat:-10.7665071183964, lon:-38.4952943176048},
        		{id:2153,nome:"Ribeirão do Largo", lat:-15.4249713883707, lon:-40.6370203123944},
        		{id:2154,nome:"Rio de Contas", lat:-13.5867965701019, lon:-41.7413770422495},
        		{id:2155,nome:"Rio do Antônio", lat:-14.3303515369452, lon:-42.0652837447921},
        		{id:2156,nome:"Rio do Pires", lat:-13.151453193541, lon:-42.1179700786475},
        		{id:2157,nome:"Rio Real", lat:-11.5455682103062, lon:-37.9163680959749},
        		{id:2158,nome:"Rodelas", lat:-9.22016985923702, lon:-38.6890773121361},
        		{id:2159,nome:"Ruy Barbosa", lat:-12.2412540324539, lon:-40.6620085540613},
        		{id:2160,nome:"Salinas da Margarida", lat:-12.8830246277771, lon:-38.7592059010347},
        		{id:2161,nome:"Salvador", lat:-12.8668316527723, lon:-38.5194871949867},
        		{id:2162,nome:"Santa Bárbara", lat:-11.9368291358513, lon:-38.9827080427837},
        		{id:2163,nome:"Santa Brígida", lat:-9.75843145256872, lon:-38.143047514687},
        		{id:2164,nome:"Santa Cruz Cabrália", lat:-16.2311038542764, lon:-39.2146334547699},
        		{id:2165,nome:"Santa Cruz da Vitória", lat:-14.9159089185223, lon:-39.8113323956206},
        		{id:2166,nome:"Santa Inês", lat:-13.2888033696793, lon:-39.8567987665432},
        		{id:2168,nome:"Santa Luzia", lat:-15.4433068529456, lon:-39.243271325983},
        		{id:2169,nome:"Santa Maria da Vitória", lat:-13.2243673270709, lon:-44.3847042585533},
        		{id:2172,nome:"Santa Rita de Cássia", lat:-11.0089905434475, lon:-44.5585884647615},
        		{id:2173,nome:"Santa Teresinha", lat:-12.7299054937135, lon:-39.5703876919741},
        		{id:2167,nome:"Santaluz", lat:-11.2239168536613, lon:-39.4993273015386},
        		{id:2170,nome:"Santana", lat:-13.0642007748168, lon:-43.9515155970113},
        		{id:2171,nome:"Santanópolis", lat:-11.9840895224472, lon:-38.8622503818158},
        		{id:2174,nome:"Santo Amaro", lat:-12.5614607540841, lon:-38.7631200682138},
        		{id:2175,nome:"Santo Antônio de Jesus", lat:-13.0082266518031, lon:-39.2297258848994},
        		{id:2176,nome:"Santo Estêvão", lat:-12.4678537316361, lon:-39.2702301956484},
        		{id:2177,nome:"São Desidério", lat:-12.7993229082233, lon:-45.4839516606349},
        		{id:2178,nome:"São Domingos", lat:-11.4591768438501, lon:-39.57873325428},
        		{id:2181,nome:"São Felipe", lat:-12.8377443728179, lon:-39.0950249761375},
        		{id:2179,nome:"São Félix", lat:-12.6737785833153, lon:-38.9989209332653},
        		{id:2180,nome:"São Félix do Coribe", lat:-13.4669849121879, lon:-44.0376138754971},
        		{id:2182,nome:"São Francisco do Conde", lat:-12.6500119823389, lon:-38.635591024845},
        		{id:2183,nome:"São Gabriel", lat:-11.0620430478664, lon:-41.6928703704047},
        		{id:2184,nome:"São Gonçalo dos Campos", lat:-12.4178828409165, lon:-38.9427321713382},
        		{id:2185,nome:"São José da Vitória", lat:-15.0599544905685, lon:-39.3661533878218},
        		{id:2186,nome:"São José do Jacuípe", lat:-11.4377362765397, lon:-39.9082200870809},
        		{id:2187,nome:"São Miguel das Matas", lat:-13.0491192563988, lon:-39.4308464147414},
        		{id:2188,nome:"São Sebastião do Passé", lat:-12.5162948942459, lon:-38.498331466543},
        		{id:2189,nome:"Sapeaçu", lat:-12.7279685047376, lon:-39.21694939249},
        		{id:2190,nome:"Sátiro Dias", lat:-11.5697260117466, lon:-38.5934372344249},
        		{id:2191,nome:"Saubara", lat:-12.7754540574536, lon:-38.7726643477328},
        		{id:2192,nome:"Saúde", lat:-10.9094233127132, lon:-40.3860729059368},
        		{id:2193,nome:"Seabra", lat:-12.4208769382474, lon:-41.8815216157793},
        		{id:2194,nome:"Sebastião Laranjeiras", lat:-14.5712992325467, lon:-43.1282117046974},
        		{id:2195,nome:"Senhor do Bonfim", lat:-10.4757415213426, lon:-40.1127018734794},
        		{id:2197,nome:"Sento Sé", lat:-10.136658384727, lon:-41.6499494651997},
        		{id:2196,nome:"Serra do Ramalho", lat:-13.4867150246941, lon:-43.7019377518379},
        		{id:2198,nome:"Serra Dourada", lat:-12.8139262556731, lon:-43.7994592745084},
        		{id:2199,nome:"Serra Preta", lat:-12.097593897516, lon:-39.3745841076058},
        		{id:2200,nome:"Serrinha", lat:-11.660939507936, lon:-38.970222551386},
        		{id:2201,nome:"Serrolândia", lat:-11.4357208707887, lon:-40.2548631631565},
        		{id:2202,nome:"Simões Filho", lat:-12.7668004069653, lon:-38.4031364461054},
        		{id:2203,nome:"Sítio do Mato", lat:-12.8463663329677, lon:-43.4710659240084},
        		{id:2204,nome:"Sítio do Quinto", lat:-10.328183444814, lon:-38.1563501407157},
        		{id:2205,nome:"Sobradinho", lat:-9.68783343610588, lon:-40.8865944593156},
        		{id:2206,nome:"Souto Soares", lat:-12.0198391513289, lon:-41.7982572301282},
        		{id:2207,nome:"Tabocas do Brejo Velho", lat:-12.4816746036645, lon:-44.1554458774928},
        		{id:2208,nome:"Tanhaçu", lat:-14.1340587829656, lon:-41.1803130279508},
        		{id:2209,nome:"Tanque Novo", lat:-13.556006839542, lon:-42.5473988941889},
        		{id:2210,nome:"Tanquinho", lat:-11.9516714120494, lon:-39.0887986804265},
        		{id:2211,nome:"Taperoá", lat:-13.5791392363666, lon:-39.2216466384012},
        		{id:2212,nome:"Tapiramutá", lat:-11.8854830392688, lon:-40.7846947423217},
        		{id:2213,nome:"Teixeira de Freitas", lat:-17.4243992247125, lon:-39.7891764381983},
        		{id:2214,nome:"Teodoro Sampaio", lat:-12.2646126859603, lon:-38.621837398582},
        		{id:2215,nome:"Teofilândia", lat:-11.4762914994476, lon:-38.9555241180596},
        		{id:2216,nome:"Teolândia", lat:-13.5551354813726, lon:-39.4980776328411},
        		{id:2217,nome:"Terra Nova", lat:-12.3895215496808, lon:-38.5986309140348},
        		{id:2218,nome:"Tremedal", lat:-15.0068220894901, lon:-41.4572357603217},
        		{id:2219,nome:"Tucano", lat:-10.9876547140896, lon:-38.7887139347031},
        		{id:2220,nome:"Uauá", lat:-9.89626039549827, lon:-39.4441456787203},
        		{id:2221,nome:"Ubaíra", lat:-13.2772168140962, lon:-39.685529856034},
        		{id:2222,nome:"Ubaitaba", lat:-14.2679401061667, lon:-39.4215443584219},
        		{id:2223,nome:"Ubatã", lat:-14.051108502107, lon:-39.5300587747811},
        		{id:2224,nome:"Uibaí", lat:-11.3738766884666, lon:-42.1028065287091},
        		{id:2225,nome:"Umburanas", lat:-10.5702254828906, lon:-41.2370449315569},
        		{id:2226,nome:"Una", lat:-15.2229858901478, lon:-39.1660853881105},
        		{id:2227,nome:"Urandi", lat:-14.7373678342862, lon:-42.6832481238328},
        		{id:2228,nome:"Uruçuca", lat:-14.5156287489536, lon:-39.221149956927},
        		{id:2229,nome:"Utinga", lat:-12.038664081636, lon:-41.0673414005013},
        		{id:2230,nome:"Valença", lat:-13.3600626560702, lon:-39.1990226080545},
        		{id:2231,nome:"Valente", lat:-11.3806969419656, lon:-39.4514192076669},
        		{id:2232,nome:"Várzea da Roça", lat:-11.5972190715884, lon:-40.0466831464991},
        		{id:2233,nome:"Várzea do Poço", lat:-11.5400165245966, lon:-40.3165215832596},
        		{id:2234,nome:"Várzea Nova", lat:-11.1212451208827, lon:-41.0305140442862},
        		{id:2235,nome:"Varzedo", lat:-12.976199062643, lon:-39.3805607167977},
        		{id:2236,nome:"Vera Cruz", lat:-13.0114533288681, lon:-38.7155046457275},
        		{id:2237,nome:"Vereda", lat:-17.1897159221273, lon:-39.9452803094353},
        		{id:2238,nome:"Vitória da Conquista", lat:-15.0179407084555, lon:-40.92552956229},
        		{id:2239,nome:"Wagner", lat:-12.2353957776639, lon:-41.1654146533411},
        		{id:2240,nome:"Wanderley", lat:-11.8067992463822, lon:-43.9148357976695},
        		{id:2241,nome:"Wenceslau Guimarães", lat:-13.6308566454686, lon:-39.6271267074134},
        		{id:2242,nome:"Xique-Xique", lat:-11.0341983289606, lon:-42.7935553348202}
        	];
        
        vm.style = function(feature, event)
        {
        		//var center = ol.extent.getCenter(feature.getGeometry().getExtent());
        		//vm.selectmunicipio = [{id:2242,nome:"Xique-Xique", lat:center[1], lon:center[0]}];
        		return new ol.style.Style({
                    stroke: new ol.style.Stroke({
                color: 'black',
                width: 1
              })
            });
        }
        
        vm.municipioba = {
        		name: 'Municipios',
                source: {
                    type: 'ImageWMS',
                    url: '/geoserver/coate/wms',
                    projection: 'EPSG:4326',
                    params: { 
                    		  LAYERS: 'coate:municipio_ba,coate:municipio_ba_select',
                    		  viewparams:'muid:0'
                    		}
                }
            };
        
        vm.municipioba2 = {
        		name: 'vetormunicipio',
                source: {
                    type: 'GeoJSON',
                    url: '/geoserver/coate/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=coate:municipio_ba&outputFormat=application%2Fjson',
                    projection: 'EPSG:4326'
                },
                "style": vm.style
            };
        
        vm.defaults = 
        {
            events: {
                layers: [ 'mousemove', 'click' ]
            },
            view: {
            	projection: 'EPSG:4326'
            },
            interactions: {
                mouseWheelZoom: true
              }
        };
        
        
        olData.getMap().then(function(map) 
        {
        	vm.map = map;
        	
	        $scope.$on('openlayers.layers.vetormunicipio.mousemove', function(event, feature) {
	            $scope.$apply(function(scope) {
	            	
	            	vm.mouseMoveCountry = feature;
	            	if(scope.vm.feature_mousemove)
	        		{
	            		if(scope.vm.feature_mousemove != feature)
	            		{
	            			scope.vm.feature_mousemove.setStyle( new ol.style.Style({
	                            stroke: new ol.style.Stroke({
	                                color: 'black',
	                                width: 1
	                              })
	                            }));
	            			scope.vm.feature_mousemove = feature
	            			feature.setStyle( new ol.style.Style({
	                            stroke: new ol.style.Stroke({
	                              color: '#3cdbff',
	                              width: 3
	                            })
	                          }));
	            		}
	            		
	        		}else{
	        			scope.vm.feature_mousemove = feature;
	        			feature.setStyle( new ol.style.Style({
	                        stroke: new ol.style.Stroke({
	                          color: '#3cdbff',
	                          width: 3
	                        })
	                      }));
	        		}
	            });
	        });
	        $scope.$on('openlayers.layers.vetormunicipio.click', function(event, feature) {
	            $scope.$apply(function(scope) {
	                if(feature) {
	                	scope.vm.municipioba.source.params.viewparams= 'muid:'+feature.getProperties().id;
	                	for (var i = 0; i < scope.vm.selectmunicipio.length; i++) 
	                	{
		                	if(scope.vm.selectmunicipio[i].id === feature.getProperties().id)
	        				{
		                		scope.vm.selecionamunicipio = scope.vm.selectmunicipio[i];
	        				}
	                	}
	                }
	            });
	        });
        });
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
