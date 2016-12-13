package models;

import checks.PlanosCheck;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ChangeLog;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.deserializers.InfluuntDateTimeDeserializer;
import json.serializers.InfluuntDateTimeSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.math3.util.Pair;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lesiopinheiro on 7/13/16.
 */
@Entity
@Table(name = "planos")
@ChangeLog
public class Plano extends Model implements Cloneable, Serializable {

    private static final long serialVersionUID = -2879768190025745634L;

    public static Finder<UUID, Plano> find = new Finder<UUID, Plano>(Plano.class);

    @Id
    private UUID id;

    @Column
    private String idJson;

    @Column
    @NotNull(message = "não pode ficar em branco.")
    private Integer posicao;

    @Column
    @NotNull(message = "não pode ficar em branco.")
    private String descricao;

    @Column
    private Integer tempoCiclo;

    @Column
    private Integer defasagem = 0;

    @JsonIgnore
    @Transient
    private Anel anel;

    @ManyToOne
    @NotNull(message = "nao pode ser vazio")
    private VersaoPlano versaoPlano;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL)
    @Valid
    private List<EstagioPlano> estagiosPlanos;

    @OneToMany(mappedBy = "plano", cascade = CascadeType.ALL)
    @Valid
    private List<GrupoSemaforicoPlano> gruposSemaforicosPlanos;

    @Column
    @NotNull(message = "não pode ficar em branco.")
    private ModoOperacaoPlano modoOperacao;

    /**
     * Armazena qual {@link TabelaEntreVerdes} deve ser utiliada pelos grupos semafóricos a paritr da posição
     */
    @Column
    @NotNull(message = "não pode ficar em branco.")
    private Integer posicaoTabelaEntreVerde;

    @Column
    @JsonDeserialize(using = InfluuntDateTimeDeserializer.class)
    @JsonSerialize(using = InfluuntDateTimeSerializer.class)
    @CreatedTimestamp
    private DateTime dataCriacao;

    @Column
    @JsonDeserialize(using = InfluuntDateTimeDeserializer.class)
    @JsonSerialize(using = InfluuntDateTimeSerializer.class)
    @UpdatedTimestamp
    private DateTime dataAtualizacao;

    @Transient
    private boolean impostoPorFalha = false;

    @Transient
    private boolean imposto = false;

    public Plano() {
        super();
        this.idJson = UUID.randomUUID().toString();
    }

    public String getIdJson() {
        return idJson;
    }

    public void setIdJson(String idJson) {
        this.idJson = idJson;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getTempoCiclo() {
        return tempoCiclo;
    }

    public Integer getDefasagem() {
        return defasagem;
    }

    public Anel getAnel() {
        return getVersaoPlano().getAnel();
    }

    public VersaoPlano getVersaoPlano() {
        return versaoPlano;
    }

    public void setVersaoPlano(VersaoPlano versaoPlano) {
        this.versaoPlano = versaoPlano;
    }

    public List<EstagioPlano> getEstagiosPlanos() {
        return estagiosPlanos;
    }

    public void setEstagiosPlanos(List<EstagioPlano> estagios) {
        this.estagiosPlanos = estagios;
    }

    public List<EstagioPlano> getEstagiosPlanosSemEstagioDispensavel() {
        return estagiosPlanos.stream().filter(estagioPlano -> !estagioPlano.isDispensavel()).collect(Collectors.toList());
    }

    public List<GrupoSemaforicoPlano> getGruposSemaforicosPlanos() {
        return gruposSemaforicosPlanos;
    }

    public void setGruposSemaforicosPlanos(List<GrupoSemaforicoPlano> gruposSemaforicosPlanos) {
        this.gruposSemaforicosPlanos = gruposSemaforicosPlanos;
    }

    public ModoOperacaoPlano getModoOperacao() {
        return modoOperacao;
    }

    public void setModoOperacao(ModoOperacaoPlano modoOperacao) {
        this.modoOperacao = modoOperacao;
    }

    public Integer getPosicaoTabelaEntreVerde() {
        return posicaoTabelaEntreVerde;
    }

    public void setPosicaoTabelaEntreVerde(Integer posicaoTableEntreVerde) {
        this.posicaoTabelaEntreVerde = posicaoTableEntreVerde;
    }

    public DateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(DateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public DateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(DateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public boolean isImpostoPorFalha() {
        return this.impostoPorFalha;
    }

    public void setImpostoPorFalha(boolean impostoPorFalha) {
        this.impostoPorFalha = impostoPorFalha;
    }

    @JsonIgnore
    @Transient
    public boolean isTempoFixoIsolado() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.TEMPO_FIXO_ISOLADO.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isTempoFixoCoordenado() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.TEMPO_FIXO_COORDENADO.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isAtuado() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.ATUADO.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isIntermitente() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.INTERMITENTE.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isApagada() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.APAGADO.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isManual() {
        return Objects.nonNull(getModoOperacao()) && ModoOperacaoPlano.MANUAL.equals(getModoOperacao());
    }

    @JsonIgnore
    @Transient
    public boolean isModoOperacaoVerde() {
        return Objects.nonNull(getModoOperacao()) && !ModoOperacaoPlano.APAGADO.equals(getModoOperacao()) && !ModoOperacaoPlano.INTERMITENTE.equals(getModoOperacao());
    }

    @AssertTrue(groups = PlanosCheck.class, message = "Este plano deve ter a mesma quantidade de estágios que os outros planos em modo manual exclusivo.")
    public boolean isNumeroEstagiosEmModoManualOk() {
        if (isManual()) {
            final long totalEstagios = getEstagiosPlanos().stream().filter(ep -> !ep.isDestroy()).count();
            return getAnel().getControlador().getAneis().stream()
                .filter(anel -> anel.isAtivo() && anel.isAceitaModoManual())
                .map(Anel::getPlanos)
                .flatMap(Collection::stream)
                .filter(plano -> plano != null && plano.isManual())
                .allMatch(plano -> plano.getEstagiosPlanos().stream().filter(ep -> !ep.isDestroy()).count() == totalEstagios);
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class,
        message = "Todos os grupos semafóricos devem possuir configurações de ativado/desativado.")
    public boolean isQuantidadeGrupoSemaforicoIgualQuantidadeAnel() {
        return !(this.getGruposSemaforicosPlanos().isEmpty() || this.getAnel().getGruposSemaforicos().size() != this.getGruposSemaforicosPlanos().size());
    }

    @AssertTrue(groups = PlanosCheck.class,
        message = "Deve possuir pelo menos 2 estágios configurados.")
    public boolean isQuantidadeEstagioIgualQuantidadeAnel() {
        if (isModoOperacaoVerde()) {
            return !(this.getEstagiosPlanos().isEmpty() || this.getEstagiosPlanos().size() < 2);
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class,
        message = "A sequência de estágios não é válida.")
    public boolean isPosicaoUnicaEstagio() {
        if (!this.getEstagiosPlanos().isEmpty()) {
            List<EstagioPlano> thisEstagiosPlanos = this.estagiosPlanos.stream().filter(ep -> !ep.isDestroy()).collect(Collectors.toList());
            return thisEstagiosPlanos.size() == thisEstagiosPlanos.stream().map(EstagioPlano::getPosicao).distinct().count();
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class, message = "Tempo de ciclo deve estar entre {min} e {max}")
    public boolean isTempoCiclo() {
        if (isTempoFixoIsolado() || isTempoFixoCoordenado()) {
            return getTempoCiclo() != null && getAnel().getControlador().getRangeUtils().TEMPO_CICLO.contains(getTempoCiclo());
        }
        return true;
    }

    public void setTempoCiclo(Integer tempoCiclo) {
        this.tempoCiclo = tempoCiclo;
    }

    @AssertTrue(groups = PlanosCheck.class, message = "Defasagem deve estar entre {min} e o tempo de ciclo")
    public boolean isDefasagem() {
        if (isTempoFixoCoordenado() && getTempoCiclo() != null) {
            return getDefasagem() != null && getAnel().getControlador().getRangeUtils().TEMPO_DEFASAGEM.contains(getDefasagem()) && Range.between(0, getTempoCiclo()).contains(getDefasagem());
        }
        return true;
    }

    public void setDefasagem(Integer defasagem) {
        this.defasagem = defasagem;
    }

    @AssertTrue(groups = PlanosCheck.class, message = "A soma dos tempos dos estágios ({temposEstagios}s) é diferente do tempo de ciclo ({tempoCiclo}s).")
    public boolean isUltrapassaTempoCiclo() {
        boolean estagiosValidos = !this.getEstagiosPlanos().isEmpty() && isPosicaoUnicaEstagio() && isSequenciaValida();
        boolean isoladoOuCoordenado = isTempoFixoIsolado() || isTempoFixoCoordenado();
        if (estagiosValidos && isoladoOuCoordenado && getAnel().getControlador().getRangeUtils().TEMPO_CICLO.contains(getTempoCiclo())) {
            int tempoEstagios = getEstagiosPlanos().stream().filter(ep -> !ep.isDestroy()).mapToInt(this::getTempoEstagio).sum();
            return getTempoCiclo() == tempoEstagios;
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class,
        message = "A sequência de estágios não é válida.")
    public boolean isSequenciaValida() {
        if (!this.getEstagiosPlanos().isEmpty() && isPosicaoUnicaEstagio()) {
            List<EstagioPlano> estagiosOrdenados = getEstagiosOrdenados().stream().filter(ep -> !ep.isDestroy()).collect(Collectors.toList());
            return sequenciaDeEstagioValida(estagiosOrdenados);
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class, message = "A sequência de estágios não é válida, pois existe uma transição proibida devido à não execução do estágio dispensável.")
    public boolean isSequenciaInvalidaSeExisteEstagioDispensavel() {
        if (!this.getEstagiosPlanos().isEmpty() && getEstagiosPlanos().stream().anyMatch(EstagioPlano::isDispensavel)) {
            return this.sequenciaDeEstagioValida(this.ordenarEstagiosPorPosicaoSemEstagioDispensavel());
        }
        return true;
    }

    @AssertTrue(groups = PlanosCheck.class,
        message = "Configure um detector veicular para cada estágio no modo atuado.")
    public boolean isModoOperacaoValido() {
        if (this.isAtuado()) {
            return !CollectionUtils.isEmpty(this.getEstagiosPlanos()) && this.getEstagiosPlanos().stream().filter(estagioPlano -> estagioPlano.getEstagio().isAssociadoAGrupoSemaforicoVeicular()).allMatch(estagioPlano -> estagioPlano.getEstagio().temDetectorVeicular());
        }
        return true;
    }

    public void addGruposSemaforicoPlano(GrupoSemaforicoPlano grupoPlano) {
        if (getGruposSemaforicosPlanos() == null) {
            setGruposSemaforicosPlanos(new ArrayList<GrupoSemaforicoPlano>());
        }
        getGruposSemaforicosPlanos().add(grupoPlano);
    }

    public void addEstagios(EstagioPlano estagioPlano) {
        if (getEstagiosPlanos() == null) {
            setEstagiosPlanos(new ArrayList<EstagioPlano>());
        }
        getEstagiosPlanos().add(estagioPlano);
    }

    public Estagio getEstagioAnterior(EstagioPlano estagioPlano) {
        return getEstagioAnterior(estagioPlano, getEstagiosOrdenados());
    }

    public Estagio getEstagioAnterior(EstagioPlano estagioPlano, List<EstagioPlano> lista) {
        int posicao = lista.indexOf(estagioPlano);
        if (posicao == 0) {
            return lista.get(lista.size() - 1).getEstagio();
        } else {
            return lista.get(posicao - 1).getEstagio();
        }
    }

    public Integer getTempoEstagio(EstagioPlano estagioPlano) {
        return getTempoEstagio(estagioPlano, getEstagiosOrdenados());
    }

    public Integer getTempoEstagio(EstagioPlano estagioPlano, List<EstagioPlano> listaEstagiosPlanos) {
        Estagio estagio = estagioPlano.getEstagio();
        Estagio estagioAnterior = getEstagioAnterior(estagioPlano, listaEstagiosPlanos);

        Integer tempoEntreVerdes = getTempoEntreVerdeEntreEstagios(estagio, estagioAnterior);

        if (isAtuado()) {
            return tempoEntreVerdes + estagioPlano.getTempoVerdeMaximo();
        }


        return tempoEntreVerdes + estagioPlano.getTempoVerde();
    }

    public Integer getTempoEntreVerdeEntreEstagios(Estagio estagioAtual, Estagio estagioAnterior) {
        return getTempoEntreVerdeEntreEstagios(estagioAtual, estagioAnterior, estagioAnterior.getEstagiosGruposSemaforicos(), false);
    }

    public Integer getTempoEntreVerdeEntreEstagios(Estagio estagioAtual, Estagio estagioAnterior, boolean comAtrasoGrupo) {
        return getTempoEntreVerdeEntreEstagios(estagioAtual, estagioAnterior, estagioAnterior.getEstagiosGruposSemaforicos(), comAtrasoGrupo);
    }

    public Integer getTempoEntreVerdeEntreEstagios(Estagio estagioAtual, Estagio estagioAnterior,
                                                   List<EstagioGrupoSemaforico> estagiosGruposSemaforicos,
                                                   boolean comAtrasoGrupo) {
        Integer tempoEntreVerdes = 0;
        ArrayList<Integer> totalTempoEntreverdes = new ArrayList<Integer>();
        if (!estagioAnterior.equals(estagioAtual) && !estagiosGruposSemaforicos.isEmpty()) {
            for (EstagioGrupoSemaforico estagioGrupoSemaforico : estagiosGruposSemaforicos) {
                GrupoSemaforicoPlano grupoSemaforicoPlano = getGrupoSemaforicoPlano(estagioGrupoSemaforico.getGrupoSemaforico());
                if (grupoSemaforicoPlano.isAtivado() && (estagioAtual == null || !estagioAtual.getGruposSemaforicos().contains(estagioGrupoSemaforico.getGrupoSemaforico()))) {
                    totalTempoEntreverdes.add(getTempoEntreVerdeGrupoSemaforico(estagioAtual, estagioAnterior, estagioGrupoSemaforico, comAtrasoGrupo));
                } else {
                    totalTempoEntreverdes.add(0);
                }
            }
            tempoEntreVerdes = Collections.max(totalTempoEntreverdes);
        }

        return tempoEntreVerdes;
    }

    public Integer getTempoEntreVerdeQueConflitaComGrupoSemaforico(Estagio estagioAtual, Estagio estagioAnterior, GrupoSemaforico grupoSemaforico) {
        List<EstagioGrupoSemaforico> gruposSemaforicos = estagioAnterior.getEstagiosGruposSemaforicos().stream().filter(grupo -> grupo.getGrupoSemaforico().conflitaCom(grupoSemaforico)).collect(Collectors.toList());
        if (gruposSemaforicos.isEmpty()) {
            return getTempoEntreVerdeEntreEstagios(estagioAtual, estagioAnterior, false);
        } else {
            return getTempoEntreVerdeEntreEstagios(estagioAtual, estagioAnterior, gruposSemaforicos, true);
        }
    }

    public GrupoSemaforicoPlano getGrupoSemaforicoPlano(GrupoSemaforico grupoSemaforico) {
        return getGruposSemaforicosPlanos().stream()
            .filter(gsp -> gsp.getGrupoSemaforico().equals(grupoSemaforico)).findFirst().orElse(null);
    }

    private Integer getTempoEntreVerdeGrupoSemaforico(Estagio estagio, Estagio estagioAnterior,
                                                      EstagioGrupoSemaforico estagioGrupoSemaforico,
                                                      boolean comAtrasoGrupo) {
        final GrupoSemaforico grupoSemaforico = estagioGrupoSemaforico.getGrupoSemaforico();
        final TabelaEntreVerdes tabelaEntreVerdes = grupoSemaforico.getTabelasEntreVerdes().stream()
            .filter(tev -> tev.getPosicao().equals(getPosicaoTabelaEntreVerde())).findFirst().orElse(null);
        final Transicao transicao;
        if (estagio == null) {
            transicao = grupoSemaforico.findTransicaoByDestinoIntermitente(estagioAnterior);
        } else {
            transicao = grupoSemaforico.findTransicaoByOrigemDestino(estagioAnterior, estagio);
        }

        if (Objects.nonNull(tabelaEntreVerdes) && Objects.nonNull(transicao)) {
            TabelaEntreVerdesTransicao tabelaEntreVerdesTransicao = tabelaEntreVerdes
                .getTabelaEntreVerdesTransicoes().stream()
                .filter(tvt -> tvt.getTransicao().equals(transicao)).findFirst().orElse(null);
            if (Objects.nonNull(tabelaEntreVerdesTransicao)) {
                if (comAtrasoGrupo) {
                    return tabelaEntreVerdesTransicao.getTotalTempoEntreverdes(grupoSemaforico.getTipo()) +
                        transicao.getTempoAtrasoGrupo();
                } else {
                    return tabelaEntreVerdesTransicao.getTotalTempoEntreverdes(grupoSemaforico.getTipo());
                }
            }
        }
        return 0;
    }

    public List<EstagioPlano> getEstagiosOrdenados() {
        return getEstagiosPlanos().stream().filter(ep -> !ep.isDestroy()).sorted((e1, e2) -> e1.getPosicao().compareTo(e2.getPosicao())).collect(Collectors.toList());
    }

    public List<EstagioPlano> ordenarEstagiosPorPosicaoSemEstagioDispensavel() {
        List<EstagioPlano> listaEstagioPlanos = this.getEstagiosPlanosSemEstagioDispensavel();
        listaEstagioPlanos.sort((anterior, proximo) -> anterior.getPosicao().compareTo(proximo.getPosicao()));
        return listaEstagioPlanos;
    }

    public boolean sequenciaDeEstagioValida(List<EstagioPlano> estagios) {
        int tamanho = estagios.size();
        for (int i = 0; i < tamanho; i++) {
            EstagioPlano origem = estagios.get(i);
            EstagioPlano destino = null;
            if ((i + 1) < tamanho) {
                destino = estagios.get(i + 1);
            } else {
                destino = estagios.get(0);
            }
            if (origem.getEstagio().temTransicaoProibidaParaEstagio(destino.getEstagio())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Plano{"
            + "id=" + id
            + ", idJson='" + idJson + '\''
            + ", posicao=" + posicao
            + ", tempoCiclo=" + tempoCiclo
            + ", defasagem=" + defasagem
            + ", versaoPlano=" + versaoPlano
            + ", posicaoTabelaEntreVerde=" + posicaoTabelaEntreVerde
            + ", dataCriacao=" + dataCriacao
            + ", dataAtualizacao=" + dataAtualizacao
            + '}';
    }

    public HashMap<Pair<Integer, Integer>, Long> tabelaEntreVerde() {
        return tabelaEntreVerde(false);
    }

    public HashMap<Pair<Integer, Integer>, Long> tabelaEntreVerdeComAtraso() {
        return tabelaEntreVerde(true);
    }

    private HashMap<Pair<Integer, Integer>, Long> tabelaEntreVerde(boolean comAtrasoGrupo) {
        HashMap<Pair<Integer, Integer>, Long> tabela = new HashMap<>();
        if (this.isModoOperacaoVerde()) {
            preencheTabelaEntreVerde(tabela, getEstagiosPlanos(), comAtrasoGrupo);
            this.getAnel().getEstagios().stream().filter(Estagio::isDemandaPrioritaria).forEach(e -> {
                preencheTabelaEntreVerde(tabela, e, comAtrasoGrupo);
            });
        } else {
            this.getAnel().getEstagios().stream().forEach(e -> {
                tabela.put(new Pair<Integer, Integer>(e.getPosicao(), null),
                    this.getTempoEntreVerdeEntreEstagios(null, e) * 1000L);
            });
        }
        return tabela;
    }

    private void preencheTabelaEntreVerde(HashMap<Pair<Integer, Integer>, Long> tabela, List<EstagioPlano> lista, boolean comAtrasoGrupo) {
        lista.stream().forEach(origem -> {
            final Estagio anterior = origem.getEstagio();
            lista.stream().forEach(destino -> {
                final Estagio atual = destino.getEstagio();
                if (!anterior.temTransicaoProibidaParaEstagio(atual)) {
                    final long tempo;
                    if (anterior.equals(atual)) {
                        //Estagio duplo
                        tempo = 0L;
                    } else {
                        tempo = this.getTempoEntreVerdeEntreEstagios(atual, anterior, comAtrasoGrupo) * 1000L;
                    }
                    tabela.put(new Pair<Integer, Integer>(anterior.getPosicao(), atual.getPosicao()), tempo);
                }
            });
        });
    }

    private void preencheTabelaEntreVerde(HashMap<Pair<Integer, Integer>, Long> tabela, Estagio estagio, boolean comAtrasoGrupo) {
        getEstagiosPlanos().stream().forEach(ep -> {
            Estagio atual = ep.getEstagio();
            tabela.put(new Pair<Integer, Integer>(estagio.getPosicao(), atual.getPosicao()),
                this.getTempoEntreVerdeEntreEstagios(atual, estagio, comAtrasoGrupo) * 1000L);
            tabela.put(new Pair<Integer, Integer>(atual.getPosicao(), estagio.getPosicao()),
                this.getTempoEntreVerdeEntreEstagios(estagio, atual, comAtrasoGrupo) * 1000L);
        });
    }

    //TODO: Remover a metodo
    public void imprimirTabelaEntreVerde() {
        this.tabelaEntreVerde().forEach((key, value) -> {
            System.out.println("E" + key.getKey() + "-" + "E" + key.getValue() + ": " + value);
        });
    }

    public EstagioPlano getEstagioPlanoNoMomento(Long momentoEntrada) {
        Long fimEstagio = 0L;
        final List<EstagioPlano> lista = getEstagiosOrdenados();
        EstagioPlano estagioPlano = null;

        for (int i = 0; i < lista.size(); i++) {
            fimEstagio += (lista.get(i).getDuracaoEstagio() * 1000L);
            if (momentoEntrada <= fimEstagio) {
                estagioPlano = lista.get(i);
                break;
            }
        }
        return estagioPlano;
    }

    public boolean isImposto() {
        return imposto;
    }

    public void setImposto(boolean imposto) {
        this.imposto = imposto;
    }
}
