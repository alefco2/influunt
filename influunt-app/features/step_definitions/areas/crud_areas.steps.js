'use strict';

var expect = require('chai').expect;
var AreasPage = require('../../support/page-objects/areas');

module.exports = function() {
  var areasPage = new AreasPage();

  // this.Given(/^que exista o usuário "([^"]*)" com senha "([^"]*)"$/, function(arg1, arg2, callback) {
  //   // Write code here that turns the phrase above into concrete actions
  //   callback(null, 'pending');
  // });

  // this.Given(/^que o usuário "([^"]*)" entre no sistema com a senha "([^"]*)"$/, function(arg1, arg2, callback) {
  //   // Write code here that turns the phrase above into concrete actions
  //   callback(null, 'pending');
  // });

  this.Given(/^que exista ao menos uma area cadastrada no sistema$/, { timeout: 15 * 1000 }, function() {
    return areasPage.existeAoMenosUmaArea();
  });

  this.Given(/^o usuário acessar a tela de listagem de areas$/, function() {
    return areasPage.indexPage();
  });

  this.Given(/^deve ser exibida uma lista com as areas já cadastradas no sistema$/, function() {
    return areasPage.getItensTabela().then(function(itens) {
      expect(itens).to.have.length.at.least(3);
    });
  });

  this.Given(/^Clica no botão de Nova Area$/, function(callback) {
    areasPage.clicarBotaoNovaArea();
    callback();
  });

  this.Given(/^o sistema deverá redirecionar para o formulário de Cadastro de novas Areas$/, function() {
    return areasPage.formAreas().then(function(form) {
      return expect(form).to.exist;
    });
  });

  this.Given(/^o usuário acessar a tela de cadastro de novas areas$/, function() {
    return areasPage.newPage();
  });

  this.Given(/^preenche os campos da area corretamente$/, function(callback) {
    areasPage.fillAreaForm(51);
    callback();
  });

  this.Given(/^o registro da área deverá ser salvo com sucesso$/, function() {
    return areasPage.textoExisteNaTabela(51);
  });

  this.Given(/^o sistema deverá retornar à tela de listagem de areas$/, function() {
    return areasPage.isIndex();
  });

  this.Given(/^clicar no botão de visualizar área$/, function() {
    areasPage.clicarLinkComTexto('Visualizar');
  });

  this.Given(/^o sistema deverá redirecionar para a tela de visualização de areas$/, function() {
    return areasPage.isShow().then(function(resp) {
      return expect(resp).to.not.be.null;
    });
  });

  this.Given(/^clicar no botão de editar area$/, function() {
    areasPage.clicarLinkComTexto('Editar');
  });

  this.Given(/^o sistema deverá redirecionar para o formulário de edição de areas$/, function() {
    return areasPage.textoFieldDescricaoArea().then(function(descricao) {
      return expect(descricao).to.not.be.empty;
    });
  });

  this.Given(/^o usuário acessar o formulário de edição de areas$/, function() {
    areasPage.indexPage();
    return areasPage.clicarLinkComTexto('Editar');
  });

  this.Given(/^clicar no botão de excluir uma area$/, function() {
    areasPage.clicarLinkComTexto('Excluir');
  });

  this.Given(/^o sistema exibe uma caixa de confirmação se o usuário deve mesmo excluir a area$/, function() {
    return areasPage.textoConfirmacaoApagarRegistro().then(function(text) {
      expect(text).to.equal('quer mesmo apagar este registro?');
    });
  });

  // this.Given(/^o usuário responde sim$/, function() {
  //   return areasPage.clicarSimConfirmacaoApagarRegistro();
  // });

  this.Given(/^a area deverá ser excluida$/, function() {
    // return areasPage.cidadeDeveSerExcluida().then(function(res) {
    //   expect(res).to.be.true
    // });
    return areasPage.toastMessage().then(function(text) {
      expect(text).to.match(/Removido com sucesso/);
    });
  });

  // this.Given(/^o usuário responde não$/, function() {
  //   return areasPage.clicarNaoConfirmacaoApagarRegistro();
  // });

  this.Given(/^nenhuma área deve ser excluída$/, function() {
    return areasPage.nenhumaAreaDeveSerExcluida().then(function(res) {
      return expect(res).to.be.true;
    });
  });
};
