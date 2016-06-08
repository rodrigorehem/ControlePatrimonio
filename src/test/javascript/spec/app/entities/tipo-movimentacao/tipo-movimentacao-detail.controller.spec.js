'use strict';

describe('Controller Tests', function() {

    describe('TipoMovimentacao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTipoMovimentacao;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTipoMovimentacao = jasmine.createSpy('MockTipoMovimentacao');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TipoMovimentacao': MockTipoMovimentacao
            };
            createController = function() {
                $injector.get('$controller')("TipoMovimentacaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:tipoMovimentacaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
