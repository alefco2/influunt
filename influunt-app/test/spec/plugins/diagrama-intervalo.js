(function () {
  'use strict';

  describe('Configuração de planos', function() {
    var plano, valoresMinimos, resposta;
    beforeEach(function() {
      plano = {
        posicao: 1,
        tempoCiclo: 60,
        posicaoTabelaEntreVerde: 1,
        defasagem: 0,
        quantidadeGruposSemaforicos: 5,
        modoOperacao: 'TEMPO_FIXO_ISOLADO',
        estagiosPlanos: [
          {
            id: 'EP1',
            idJson: 'EP1-JSON',
            tempoVerde: 20,
            tempoVerdeMinimo: null,
            tempoVerdeMaximo: null,
            tempoVerdeIntermediario: null,
            extensaoVerde: null,
            estagio: {
              id: 'E1',
              idJson: 'E1-JSON',
              gruposSemaforicos: [
                {
                  id: 'G1',
                  idJson: 'G1-JSON',
                  tipo: 'VEICULAR',
                  posicao: 1,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-1',
                      idJson: 'tabela-entre-verdes-1-JSON',
                      posicao: 1
                    },
                    {
                      id: 'tabela-entre-verdes-1-2',
                      idJson: 'tabela-entre-verdes-1-2-JSON',
                      posicao: 2
                    },
                  ],
                  transicoes: [
                    {
                      id: 'transicao-1',
                      idJson: 'transicao-1-JSON',
                      origem: {
                        id: 'E1', 
                        idJson: 'E1-JSON'
                      },
                      destino: {
                        id: 'E2', 
                        idJson: 'E2-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-1',
                            idJson: 'tabela-entre-verdes-1-JSON'
                          },
                          tempoAmarelo: 3,
                          tempoVermelhoLimpeza: 1
                        }
                      ]
                    }
                  ]
                },
                {
                  id: 'G5',
                  idJson: 'G5-JSON',
                  tipo: 'PEDESTRE',
                  posicao: 5,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-2',
                      idJson: 'tabela-entre-verdes-2-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-3',
                      idJson: 'transicao-3-JSON',
                      origem: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      destino: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-2',
                            idJson: 'tabela-entre-verdes-2-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                    {
                      id: 'transicao-13',
                      idJson: 'transicao-13-JSON',
                      origem: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-2',
                            idJson: 'tabela-entre-verdes-2-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    }
                  ]
                },
              ]
            }
          },
          {
            id: 'EP2',
            idJson: 'EP2-JSON',
            tempoVerde: 10,
            tempoVerdeMinimo: null,
            tempoVerdeMaximo: null,
            tempoVerdeIntermediario: null,
            extensaoVerde: null,
            estagio: {
              id: 'E2',
              idJson: 'E2-JSON',
              gruposSemaforicos: [
                {
                  id: 'G3',
                  idJson: 'G3-JSON',
                  tipo: 'PEDESTRE',
                  posicao: 3,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-3',
                      idJson: 'tabela-entre-verdes-3-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-4',
                      idJson: 'transicao-4-JSON',
                      origem: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-3',
                            idJson: 'tabela-entre-verdes-3-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                    {
                      id: 'transicao-5',
                      idJson: UUID.generate(),
                      origem: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJSON: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-3',
                            idJson: 'tabela-entre-verdes-3-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                  ]
                },
                {
                  id: 'G5',
                  idJson: 'G5-JSON',
                  tipo: 'PEDESTRE',
                  posicao: 5,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-2',
                      idJson: 'tabela-entre-verdes-2-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-3',
                      idJson: 'transicao-3-JSON',
                      origem: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      destino: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-2',
                            idJson: 'tabela-entre-verdes-2-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                    {
                      id: 'transicao-13',
                      idJson: 'transicao-13-JSON',
                      origem: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-2',
                            idJson: 'tabela-entre-verdes-2-JSON'
                          },
                          tempoVermelhoIntermitente: 3,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    }
                  ]
                },
                {
                  id: 'G4',
                  idJson: 'G4-JSON',
                  tipo: 'PEDESTRE',
                  posicao: 4,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-5',
                      idJson: 'tabela-entre-verdes-5-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-6',
                      idJson: 'transicao-6-JSON',
                      origem: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-5',
                            idJson: 'tabela-entre-verdes-5-JSON'
                          },
                          tempoVermelhoIntermitente: 5,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                    {
                      id: 'transicao-7',
                      idJson: 'transicao-7-JSON',
                      origem: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      destino: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-5',
                            idJson: 'tabela-entre-verdes-5-JSON'
                          },
                          tempoVermelhoIntermitente: 5,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                  ]
                }
              ]
            }
          },
          {
            id: 'EP3',
            idJson: 'EP3-JSON',
            tempoVerde: 12,
            tempoVerdeMinimo: null,
            tempoVerdeMaximo: null,
            tempoVerdeIntermediario: null,
            extensaoVerde: null,
            estagio: {
              id: 'E3',
              idJson: 'E3-JSON',
              gruposSemaforicos: [
                {
                  id: 'G2',
                  idJson: 'G2-JSON',
                  tipo: 'VEICULAR',
                  posicao: 2,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-6',
                      idJson: 'tabela-entre-verdes-6-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-8',
                      idJson: 'transicao-8-JSON',
                      origem: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      destino: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-6',
                            idJson: 'tabela-entre-verdes-6-JSON'
                          },
                          tempoAmarelo: 3,
                          tempoVermelhoLimpeza: 3,
                          tempoAtrasoGrupo: 2
                        }
                      ]
                    }
                  ]
                },
                {
                  id: 'G4',
                  idJson: 'G4-JSON',
                  tipo: 'PEDESTRE',
                  posicao: 4,
                  tabelasEntreVerdes: [
                    {
                      id: 'tabela-entre-verdes-5',
                      idJson: 'tabela-entre-verdes-5-JSON',
                      posicao: 1
                    }
                  ],
                  transicoes: [
                    {
                      id: 'transicao-6',
                      idJson: 'transicao-6-JSON',
                      origem: {
                        id: 'E2',
                        idJson: 'E2-JSON'
                      },
                      destino: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-5',
                            idJson: 'tabela-entre-verdes-5-JSON'
                          },
                          tempoVermelhoIntermitente: 5,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                    {
                      id: 'transicao-7',
                      idJson: 'transicao-7-JSON',
                      origem: {
                        id: 'E3',
                        idJson: 'E3-JSON'
                      },
                      destino: {
                        id: 'E1',
                        idJson: 'E1-JSON'
                      },
                      tabelaEntreVerdesTransicoes: [
                        {
                          tabelaEntreVerdes: {
                            id: 'tabela-entre-verdes-5',
                            idJson: 'tabela-entre-verdes-5-JSON'
                          },
                          tempoVermelhoIntermitente: 5,
                          tempoVermelhoLimpeza: 3
                        }
                      ]
                    },
                  ]

                },
              ]
            }
          },
        ],
        posicaoGruposSemaforicos: {
          G1: 0,
          G2: 1,
          G3: 2,
          G4: 3,
          G5: 4
        }
      };
      valoresMinimos = {
        verdeMin: 1,
        verdeMinimoMin: 1
      };
    });
    describe('Descricao do plano', function() {
      it('Deve existir um objeto de plano', function() {
        expect(plano).toBeDefined();
      });

      it('O plano deve conter uma sequencia de estagios', function() {
        expect(plano.estagiosPlanos).toBeDefined();
      });

      it('A sequencia de estágios deve conter 3 estagios', function() {
        expect(plano.estagiosPlanos.length).toBe(3);
      });

      it('O E1 deve possuir 2 grupos semafóricos', function() {
        expect(plano.estagiosPlanos[0].estagio.gruposSemaforicos.length).toBe(2);
      });

      it('O E2 deve possuir 3 grupos semafóricos', function() {
        expect(plano.estagiosPlanos[1].estagio.gruposSemaforicos.length).toBe(3);
      });

      it('O E3 deve possuir 2 grupos semafóricos', function() {
        expect(plano.estagiosPlanos[2].estagio.gruposSemaforicos.length).toBe(2);
      });

      it('O G1 deve ter 1 transicao', function() {
        expect(plano.estagiosPlanos[0].estagio.gruposSemaforicos[0].transicoes.length).toBe(1);
      });

      it('O G2 deve ter 1 transicao', function() {
        expect(plano.estagiosPlanos[2].estagio.gruposSemaforicos[0].transicoes.length).toBe(1);
      });

      it('O G3 deve ter 2 transicoes', function() {
        expect(plano.estagiosPlanos[1].estagio.gruposSemaforicos[0].transicoes.length).toBe(2);
      });

      it('O G4 deve ter 2 transicoes', function() {
        expect(plano.estagiosPlanos[1].estagio.gruposSemaforicos[2].transicoes.length).toBe(2);
        expect(plano.estagiosPlanos[2].estagio.gruposSemaforicos[1].transicoes.length).toBe(2);
      });

      it('O G5 deve ter 2 transicoes', function() {
        expect(plano.estagiosPlanos[0].estagio.gruposSemaforicos[1].transicoes.length).toBe(2);
        expect(plano.estagiosPlanos[1].estagio.gruposSemaforicos[1].transicoes.length).toBe(2);
      });
    });

    describe('descricao da resposta', function() {
      beforeEach(function() {
        var diagramaIntervaloBuilder = new influunt.components.DiagramaIntervalos(plano, valoresMinimos);
        resposta = diagramaIntervaloBuilder.calcula();
      });

      it('Deve conter uma lista de erros vazia', function() {
        expect(resposta.erros.length).toBe(0);
      });

      it('Deve possuir uma lista de gruposSemaforicos contendo 5 grupos', function() {
        expect(resposta.gruposSemaforicos.length).toBe(5);
      });

      it('O G1 deve ter 5 intervalos', function() {
        expect(resposta.gruposSemaforicos[0].intervalos.length).toBe(5);
      });

      it('O G2 deve ter 5 intervalos', function() {
        expect(resposta.gruposSemaforicos[1].intervalos.length).toBe(5);
      });

      it('O G3 deve ter 5 intervalos', function() {
        expect(resposta.gruposSemaforicos[2].intervalos.length).toBe(5);
      });

      it('O G4 deve ter 4 intervalos', function() {
        expect(resposta.gruposSemaforicos[3].intervalos.length).toBe(4);
      });

      it('O G5 deve ter 5 intervalos', function() {
        expect(resposta.gruposSemaforicos[4].intervalos.length).toBe(5);
      });

      it('Os intervalos do G1 devem durar 8(vermelho) , 20(verde), 3(amarelo), 1(vermelho limpeza) e 28(vermelho) segundos', function() {
        expect(resposta.gruposSemaforicos[0].intervalos[0].duracao).toBe(8);
        expect(resposta.gruposSemaforicos[0].intervalos[1].duracao).toBe(20);
        expect(resposta.gruposSemaforicos[0].intervalos[2].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[0].intervalos[3].duracao).toBe(1);
        expect(resposta.gruposSemaforicos[0].intervalos[4].duracao).toBe(28);

        expect(resposta.gruposSemaforicos[0].intervalos[0].status).toBe(3);
        expect(resposta.gruposSemaforicos[0].intervalos[1].status).toBe(1);
        expect(resposta.gruposSemaforicos[0].intervalos[2].status).toBe(2);
        expect(resposta.gruposSemaforicos[0].intervalos[3].status).toBe(6);
        expect(resposta.gruposSemaforicos[0].intervalos[4].status).toBe(3);
      });

      it('Os intervalos do G2 devem durar 2(verde) , 3(amarelo), 3(vermelho limpeza), 40(vermelho) e 12(verde) segundos', function() {
        expect(resposta.gruposSemaforicos[1].intervalos[0].duracao).toBe(2);
        expect(resposta.gruposSemaforicos[1].intervalos[1].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[1].intervalos[2].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[1].intervalos[3].duracao).toBe(40);
        expect(resposta.gruposSemaforicos[1].intervalos[4].duracao).toBe(12);

        expect(resposta.gruposSemaforicos[1].intervalos[0].status).toBe(1);
        expect(resposta.gruposSemaforicos[1].intervalos[1].status).toBe(2);
        expect(resposta.gruposSemaforicos[1].intervalos[2].status).toBe(6);
        expect(resposta.gruposSemaforicos[1].intervalos[3].status).toBe(3);
        expect(resposta.gruposSemaforicos[1].intervalos[4].status).toBe(1);
      });

      it('Os intervalos do G3 devem durar 32(vermelho) , 10(verde), 3(vermelhoIntermitente), 3(vermelho limpeza) e 12(vermelho) segundos', function() {
        expect(resposta.gruposSemaforicos[2].intervalos[0].duracao).toBe(32);
        expect(resposta.gruposSemaforicos[2].intervalos[1].duracao).toBe(10);
        expect(resposta.gruposSemaforicos[2].intervalos[2].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[2].intervalos[3].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[2].intervalos[4].duracao).toBe(12);

        expect(resposta.gruposSemaforicos[2].intervalos[0].status).toBe(3);
        expect(resposta.gruposSemaforicos[2].intervalos[1].status).toBe(1);
        expect(resposta.gruposSemaforicos[2].intervalos[2].status).toBe(4);
        expect(resposta.gruposSemaforicos[2].intervalos[3].status).toBe(6);
        expect(resposta.gruposSemaforicos[2].intervalos[4].status).toBe(3);
      });

      it('Os intervalos do G4 devem durar 5(vermelhoIntermitente), 3(vermelho limpeza), 24(vermelho), 28(verde) segundos', function() {
        expect(resposta.gruposSemaforicos[3].intervalos[0].duracao).toBe(5);
        expect(resposta.gruposSemaforicos[3].intervalos[1].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[3].intervalos[2].duracao).toBe(24);
        expect(resposta.gruposSemaforicos[3].intervalos[3].duracao).toBe(28);

        expect(resposta.gruposSemaforicos[3].intervalos[0].status).toBe(4);
        expect(resposta.gruposSemaforicos[3].intervalos[1].status).toBe(6);
        expect(resposta.gruposSemaforicos[3].intervalos[2].status).toBe(3);
        expect(resposta.gruposSemaforicos[3].intervalos[3].status).toBe(1);
      });

      it('Os intervalos do G5 devem durar 8(vermelho), 34(verde), 3(vermelhoIntermitente), 3(vermelho limpeza) e 12(vermelho) segundos', function() {
        expect(resposta.gruposSemaforicos[4].intervalos[0].duracao).toBe(8);
        expect(resposta.gruposSemaforicos[4].intervalos[1].duracao).toBe(34);
        expect(resposta.gruposSemaforicos[4].intervalos[2].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[4].intervalos[3].duracao).toBe(3);
        expect(resposta.gruposSemaforicos[4].intervalos[4].duracao).toBe(12);

        expect(resposta.gruposSemaforicos[4].intervalos[0].status).toBe(3);
        expect(resposta.gruposSemaforicos[4].intervalos[1].status).toBe(1);
        expect(resposta.gruposSemaforicos[4].intervalos[2].status).toBe(4);
        expect(resposta.gruposSemaforicos[4].intervalos[3].status).toBe(6);
        expect(resposta.gruposSemaforicos[4].intervalos[4].status).toBe(3);
      });

      it('A resposta deve ter uma lista de 3 estágios', function() {
        expect(resposta.estagios.length).toBe(3);
      });

      it('Os estágios devem ter as durações de E1: 28, E2: 14, E3: 18', function() {
        expect(resposta.estagios[0].duracao).toBe(28);
        expect(resposta.estagios[1].duracao).toBe(14);
        expect(resposta.estagios[2].duracao).toBe(18);
      });

    });
  });
})();