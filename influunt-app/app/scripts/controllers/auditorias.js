'use strict';

/**
 * @ngdoc function
 * @name influuntApp.controller:AuditoriasCtrl
 * @description
 * # AuditoriasCtrl
 * Controller of the influuntApp
 */
angular.module('influuntApp')
  .controller('AuditoriasCtrl', ['$scope', '$controller',
    function ($scope, $controller) {
      // Herda todo o comportamento do crud basico.
      $controller('CrudCtrl', {$scope: $scope});
      $scope.inicializaNovoCrud('auditorias/');
    }]);
