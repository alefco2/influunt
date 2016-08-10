'use strict';

var worldObj = require('../world');
var world = new worldObj.World();

var AreasPage = function () {
  var INDEX_PATH = '/app/areas';
  var NEW_PATH = '/app/areas/new';

  var inputDescArea = '[name="area"]';
  var novaAreaButton = 'a[href*="/areas/new"]';
  var formAreas = 'form[name="formAreas"]';

  var totalAreasIndex = 0;

  this.world = world;

  this.isIndex = function() {
    return world.getElements('tbody tr[data-ng-repeat="area in lista"]');
  };

  this.isShow = function() {
    return world.getElement('h5 small').getText().then(function(text) {
      return text.match(/ - #/);
    });
  };

  this.indexPage = function() {
    world.visit(INDEX_PATH);
    world.getElements('tbody tr[data-ng-repeat="area in lista"]').then(function(elements) {
      totalAreasIndex = elements.length;
    });
    return world.waitFor('tbody tr[data-ng-repeat="area in lista"]');
  };

  this.existeAoMenosUmaArea = function() {
    return world.execSqlScript('features/support/scripts/areas/create_area.sql');
  };

  this.newPage = function() {
    return world.visit(NEW_PATH).then(function(){
      return world.waitFor(formAreas);
    });
  };

  this.getItensTabela = function() {
    return world.getElements('tbody tr td');
  };

  this.clicarBotaoNovaArea = function() {
    return world.clickButton(novaAreaButton);
  };

  this.formAreas = function() {
    return world.getElement(formAreas);
  };

  this.textoExisteNaTabela = function(text) {
    return world.waitForByXpath('//td[contains(text(), "'+text+'")]').then(function() {
      return world.getElementByXpath('//td[contains(text(), "'+text+'")]');
    });
  };

  this.clicarLinkComTexto = function(texto) {
    return world.waitForOverlayDisappear().then(function (){
      return world.findLinkByText(texto).click();
    });
  };

  this.textoFieldDescricaoArea = function() {
    return world.waitFor(inputDescArea).then(function() {
      return world.getElement(inputDescArea);
    }).then(function(element) {
     return element.getAttribute('value');
    });
  };

  this.textoConfirmacaoApagarRegistro = function() {
    return world.getElement('div[class*="sweet-alert"] p').getText();
  };

  this.nenhumaAreaDeveSerExcluida = function() {
    return world.getElements('tbody tr[data-ng-repeat="area in lista"]').then(function(elements) {
      // console.log('antes: '+totalAreasIndex+' / agora: '+elements.length);
      return elements.length === totalAreasIndex;
    });
  };


  this.getPageTitleH2 = function() {
    return world.getElement('h2').then(function(element) {
      return element.getText();
    });
  };

  this.toastMessage = function() {
    return world.waitFor('#toast-container div.toast-message').then(function() {
      return world.sleep(500);
    }).then(function() {
      return world.getElement('#toast-container div.toast-message').getText();
    });
  };

  this.cidadeIdH5 = function() {
    return world.getElement('h5 small').then(function(element) {
      return element.getText();
    });
  };

  this.clicarSimConfirmacaoApagarRegistro = function() {
    return world.waitFor('div[class^="sweet-alert"][class$="visible"]').then(function() {
      return world.clickButton('div[class^="sweet-alert"] button.confirm');
    });
  };

  this.clicarNaoConfirmacaoApagarRegistro = function() {
    return world.waitFor('div[class^="sweet-alert"][class$="visible"]').then(function() {
      return world.clickButton('div[class^="sweet-alert"] button.cancel');
    });
  };

  this.cidadeDeveSerExcluida = function() {
    return world.getElements('tbody tr[data-ng-repeat="cidade in lista"]').then(function(elements) {
      return elements.length === totalAreasIndex - 1;
    });
  };

  this.getErrorMessageFor = function(campo) {
    return world.waitFor('[name="'+campo+'"] + p[class*="error-msg"]').then(function() {
      return world.getElement('[name="'+campo+'"] + p[class*="error-msg"]').getText();
    });
  };

  this.selecionarCidade = function(cidade) {
    var index = cidade === 'São Paulo' ? 2 : 1;
    return world.getElement('select[name="cidade"] option:nth-child('+index+')').click();
  };

};

module.exports = AreasPage;
