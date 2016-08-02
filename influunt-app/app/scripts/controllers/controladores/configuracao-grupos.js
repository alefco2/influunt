'use strict';

/**
 * @ngdoc function
 * @name influuntApp.controller:ControladoresConfiguracaoGrupoCtrl
 * @description
 * # ControladoresConfiguracaoGrupoCtrl
 * Controller of the influuntApp
 */
angular.module('influuntApp')
  .controller('ControladoresConfiguracaoGruposCtrl', ['$scope', '$controller', '$state', '$filter', 'assertControlador', 'influuntAlert',
    function ($scope, $controller, $state, $filter, assertControlador, influuntAlert) {
      $controller('ControladoresCtrl', {$scope: $scope});

      var atualizaPosicaoGrupos;

      /**
       * Pré-condições para acesso à tela.
       *
       * @return     {boolean}  { description_of_the_return_value }
       */
      $scope.assert = function() {
        var valid = assertControlador.hasAneis($scope.objeto) && assertControlador.hasEstagios($scope.objeto);
        if (!valid) {
          $state.go('app.wizard_controladores.aneis', {id: $scope.objeto.id});
        }

        return valid;
      };

      $scope.inicializaConfiguracaoGrupos = function() {
        return $scope.inicializaWizard().then(function() {
          if ($scope.assert()) {
            $scope.objeto.aneis = _.orderBy($scope.objeto.aneis, ['posicao'], ['asc']);
            $scope.aneis = _.filter($scope.objeto.aneis, {ativo: true});

            $scope.aneis.forEach(function(anel) {
              anel.gruposSemaforicos = anel.gruposSemaforicos || [];
              anel.gruposSemaforicos = _.orderBy(anel.gruposSemaforicos, ['posicao']);
            });

            $scope.selecionaAnel(0);
          }
        });
      };

      $scope.adicionaGrupoSemaforico = function() {
        var obj = {anel: {id: $scope.currentAnel.id}};
        $scope.currentAnel.gruposSemaforicos.push(obj);
        return atualizaPosicaoGrupos();
      };

      $scope.removeGrupo = function(index) {
        influuntAlert.delete().then(function(confirmado) {
          if (confirmado) {
            $scope.currentAnel.gruposSemaforicos.splice(index, 1);
            $scope.currentAnel.gruposSemaforicos.forEach(function(grupo, index) {
              grupo.posicao = index + 1;
            });

            atualizaPosicaoGrupos();
          }
        });
      };

      /**
       * atualiza as posições dos grupos.
       *
       * @return     {<type>}  { description_of_the_return_value }
       */
      atualizaPosicaoGrupos = function() {
        var posicao = 0;
        return _.chain($scope.aneis)
          .map('gruposSemaforicos')
          .flatten()
          .each(function(g) {
            g.posicao = ++posicao;
          })
          .value();
      };

    }]);
