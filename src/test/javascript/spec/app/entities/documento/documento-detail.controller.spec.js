'use strict';

describe('Controller Tests', function() {

    describe('Documento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDocumento, MockTipoDocumento, MockMovimentacao;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDocumento = jasmine.createSpy('MockDocumento');
            MockTipoDocumento = jasmine.createSpy('MockTipoDocumento');
            MockMovimentacao = jasmine.createSpy('MockMovimentacao');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Documento': MockDocumento,
                'TipoDocumento': MockTipoDocumento,
                'Movimentacao': MockMovimentacao
            };
            createController = function() {
                $injector.get('$controller')("DocumentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:documentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
