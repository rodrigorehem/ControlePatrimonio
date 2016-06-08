'use strict';

describe('Controller Tests', function() {

    describe('Item Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockItem, MockTipoItem, MockMovimentacao;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockItem = jasmine.createSpy('MockItem');
            MockTipoItem = jasmine.createSpy('MockTipoItem');
            MockMovimentacao = jasmine.createSpy('MockMovimentacao');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Item': MockItem,
                'TipoItem': MockTipoItem,
                'Movimentacao': MockMovimentacao
            };
            createController = function() {
                $injector.get('$controller')("ItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:itemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
