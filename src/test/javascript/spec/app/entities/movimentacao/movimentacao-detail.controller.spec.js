'use strict';

describe('Controller Tests', function() {

    describe('Movimentacao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMovimentacao, MockTipoMovimentacao, MockDocumento, MockPessoa, MockItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMovimentacao = jasmine.createSpy('MockMovimentacao');
            MockTipoMovimentacao = jasmine.createSpy('MockTipoMovimentacao');
            MockDocumento = jasmine.createSpy('MockDocumento');
            MockPessoa = jasmine.createSpy('MockPessoa');
            MockItem = jasmine.createSpy('MockItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Movimentacao': MockMovimentacao,
                'TipoMovimentacao': MockTipoMovimentacao,
                'Documento': MockDocumento,
                'Pessoa': MockPessoa,
                'Item': MockItem
            };
            createController = function() {
                $injector.get('$controller')("MovimentacaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:movimentacaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
