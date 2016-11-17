# language: pt
@filter_mapas
Funcionalidade: Realizar filtros no mapa

  Cenário: Exibir o menu para realizar os filtros
    Dado o usuário logue no sistema com usuário "mobilab" e perfil administrador
    Dado que o sistema possua controladores cadastrados e configurados
    E que o usuário acesse a página de listagem de controladores
    E o usuário clicar no botão "Ver no mapa"
    Então o sistema deve redirecionar para o mapa
    Quando o usuário clicar no menu filtros
    Então o menu filtros deverá aparecer

  Cenário: Exibir todos os controladores e aneis configurados
    Dado usuário estiver na tela de mapa
    Então o sistema deverá mostrar no mapa "3" controladores
    E o sistema deverá mostrar no mapa "0" aneis
    E o sistema deverá mostrar "2" controladores agrupados

  Cenário: Vizualizar aneis agrupados no mapa
    Dado usuário estiver na tela de mapa
    Quando o usuário clicar no grupo de aneis "2"
    Então o sistema deverá mostrar no mapa "3" controladores
    E o sistema deverá mostrar no mapa "2" aneis

  Cenário: Quero visualizar apenas os controladores
    Dado usuário estiver na tela de mapa
    E o usuário clicar na opção "Anéis" para filtrar
    Então o sistema deverá mostrar no mapa "3" controladores
    E o sistema deverá mostrar no mapa "0" aneis

  Cenário: Quero filtar no mapa pelo o número do controlador 2.000.0001
    Dado usuário estiver na tela de mapa
    E o usuário clicar na opção "Anéis" para filtrar
    E o usuário selecionar o valor "2.000.0001" para o campo "controladores"
    Então o sistema deverá mostrar no mapa "1" controladores
    E o sistema deverá mostrar no mapa "1" aneis

  Cenário: Quero filtar no mapa pelo o número do controlador 1.000.0001
    Dado usuário estiver na tela de mapa
    E o usuário selecionar o valor "1.000.0001" para o campo "controladores"
    Então o sistema deverá mostrar no mapa "2" controladores
    E o sistema deverá mostrar no mapa "1" aneis

  Cenário: Quero filtar no mapa pelo o número do controlador 1.000.0002
    Dado usuário estiver na tela de mapa
    E o usuário selecionar o valor "1.000.0002" para o campo "controladores"
    Então o sistema deverá mostrar no mapa "2" controladores
    E o sistema deverá mostrar no mapa "2" aneis

  Cenário: Quero filtar no mapa pelo o número do controlador 1.000.0003
    Dado usuário estiver na tela de mapa
    E o usuário selecionar o valor "1.000.0003" para o campo "controladores"
    Então o sistema deverá mostrar no mapa "3" controladores
    E o sistema deverá mostrar no mapa "2" aneis

  Cenário: Quero remover do meu filtro os controladores selecionados
    Dado usuário estiver na tela de mapa
    Quando o usuário remover o "2.000.0001" selecionado do campo "controladores"
    Então o sistema deverá mostrar no mapa "2" controladores
    E o sistema deverá mostrar no mapa "3" aneis
    Quando o usuário remover o "1.000.0001" selecionado do campo "controladores"
    Então o sistema deverá mostrar no mapa "2" controladores
    E o sistema deverá mostrar no mapa "2" aneis
    Quando o usuário remover o "1.000.0002" selecionado do campo "controladores"
    Então o sistema deverá mostrar no mapa "1" controladores
    E o sistema deverá mostrar no mapa "2" aneis
    Quando o usuário remover o "1.000.0003" selecionado do campo "controladores"
    Então o sistema deverá mostrar no mapa "3" controladores
    E o sistema deverá mostrar no mapa "2" aneis

  Cenário: Quero filtar no mapa pela área
    Dado usuário estiver na tela de mapa
    E o usuário selecionar o valor "2" para o campo "areas"
    Então o sistema deverá mostrar no mapa "1" controladores
    E o sistema deverá mostrar no mapa "1" aneis

  Cenário: Quero filtar no mapa pelaq subárea
    Dado usuário estiver na tela de mapa
    Quando o usuário remover o "2" selecionado do campo "areas"
    E o usuário selecionar o valor "AREA SUL PAULISTA" para o campo "subarea"
    Então o sistema deverá marcar o agrupamento no mapa
    E o sistema deverá mostrar no mapa "1" controladores
    E o sistema deverá mostrar no mapa "1" aneis

  Cenário: Quero filtar no mapa pelo agrupamento
    Dado usuário estiver na tela de mapa
    Quando o usuário remover o "AREA SUL PAULISTA" selecionado do campo "subarea"
    E o usuário selecionar o valor "Corredor da Paulista" para o campo "agrupamentos"
    Então o sistema deverá marcar o agrupamento no mapa
    E o sistema deverá mostrar no mapa "2" controladores
    E o sistema deverá mostrar no mapa "2" aneis

  Cenário: Sair da tela de Mapas
    Dado usuário estiver na tela de mapa
    Quando o usuário clicar em "Voltar"
    Então o sistema deverá redirecionar o usuário para a página de listagem de controladores
