'use strict';

describe('Controller Tests', function() {

    describe('TipoDocumento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTipoDocumento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTipoDocumento = jasmine.createSpy('MockTipoDocumento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TipoDocumento': MockTipoDocumento
            };
            createController = function() {
                $injector.get('$controller')("TipoDocumentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:tipoDocumentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
