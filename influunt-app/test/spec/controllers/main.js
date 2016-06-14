'use strict';

describe('Controller: MainCtrl', function () {

  // load the controller's module
  beforeEach(module('influuntApp'));

  var MainCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    MainCtrl = $controller('MainCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('Deve possuir as definições do controller de breadcrumbs', function() {
    expect(scope.udpateBreadcrumbs).toBeDefined();
  });

  it('Deve possuir as definições do controller de datatables', function() {
    expect(scope.dtOptions).toBeDefined();
  });
});
