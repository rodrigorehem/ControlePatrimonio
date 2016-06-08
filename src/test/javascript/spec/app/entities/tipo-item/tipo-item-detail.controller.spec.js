'use strict';

describe('Controller Tests', function() {

    describe('TipoItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTipoItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTipoItem = jasmine.createSpy('MockTipoItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TipoItem': MockTipoItem
            };
            createController = function() {
                $injector.get('$controller')("TipoItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:tipoItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
