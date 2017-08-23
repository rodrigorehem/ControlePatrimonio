'use strict';

describe('Controller Tests', function() {

    describe('UnidadeJudiciaria Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUnidadeJudiciaria;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUnidadeJudiciaria = jasmine.createSpy('MockUnidadeJudiciaria');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'UnidadeJudiciaria': MockUnidadeJudiciaria
            };
            createController = function() {
                $injector.get('$controller')("UnidadeJudiciariaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'controlePatrimonialApp:unidadeJudiciariaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
