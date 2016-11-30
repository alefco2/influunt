# language: pt
@relatorios @relatorio_controlador_status
Funcionalidade: Realizar o relatório de status dos controladores

  Cenário: Acessar a tela de relatório por de status do controlador
    Dado que o sistema possua controladores cadastrados e configurados
    E o usuário acessar o relatório "controladores_status"
    Então o sistema deverá apresentar na listagem controlador "Em falha"

  Cenário: Fazer o download do arquivo csv do status do controladores
    Dado o usuário clicar em "CSV"
    Então o sistema deverá apresentar na listagem controlador "Em falha"

  Cenário: Acessar a tela de relatório dos controladores que possuem falhas
    Dado o usuário acessar o relatório "controladores_falhas"
    Então o sistema deverá apresentar na listagem controlador "Falha no Controlador"

  Cenário: Fazer o download do arquivo csv dos controladores que possuem falhas
    Dado o usuário clicar em "CSV"
    Então o sistema deverá apresentar na listagem controlador "Falha no Controlador"
