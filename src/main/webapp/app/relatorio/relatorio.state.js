(function() {
    'use strict';

    angular
        .module('controlePatrimonialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('relatorio', {
            abstract: true,
            parent: 'app'
        });
    }
})();
