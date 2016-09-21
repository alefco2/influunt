package models;

import checks.TabelaHorariosCheck;
import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.ChangeLog;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.deserializers.InfluuntDateTimeDeserializer;
import json.serializers.InfluuntDateTimeSerializer;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import utils.CustomCalendar;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import static com.sun.tools.doclint.Entity.and;
import static com.sun.tools.doclint.Entity.ni;
import static com.sun.tools.doclint.Entity.nu;
import static utils.CustomCalendar.getCalendar;

/**
 * Entidade que representa um {@link Evento} no sistema
 *
 * @author Pedro Pires
 */
@Entity
@Table(name = "eventos")
@ChangeLog
public class Evento extends Model implements Cloneable, Serializable, Comparable<Evento> {

    private static final long serialVersionUID = -8164198987601502461L;

    public static Finder<UUID, Evento> find = new Finder<UUID, Evento>(Evento.class);

    @Id
    private UUID id;

    @Column
    private String idJson;

    @Column
    @NotNull(message = "não pode ficar em branco")
    private Integer posicao;

    @Column
    @Enumerated(EnumType.STRING)
    private DiaDaSemana diaDaSemana;

    @Column
    @NotNull(message = "não pode ficar em branco")
    private LocalTime horario;

    @Column
    private Date data;

    @Column
    private String nome;

    @Column
    @NotNull(message = "não pode ficar em branco")
    private Integer posicaoPlano;

    @Column
    @Enumerated(EnumType.STRING)
    @NotNull(message = "não pode ficar em branco")
    private TipoEvento tipo;

    @ManyToOne
    @NotNull(message = "não pode ficar em branco")
    private TabelaHorario tabelaHorario;

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


    public Evento() {
        super();
        this.idJson = UUID.randomUUID().toString();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIdJson() {
        return idJson;
    }

    public void setIdJson(String idJson) {
        this.idJson = idJson;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public DiaDaSemana getDiaDaSemana() {
        return diaDaSemana;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public Date getData() {
        return data;
    }

    public Date setData(Date data) {
        this.data = data;
        return data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public TabelaHorario getTabelaHorario() {
        return tabelaHorario;
    }

    public void setTabelaHorario(TabelaHorario tabelaHorario) {
        this.tabelaHorario = tabelaHorario;
    }

    public Integer getPosicaoPlano() {
        return posicaoPlano;
    }

    public void setPosicaoPlano(Integer posicaoPlano) {
        this.posicaoPlano = posicaoPlano;
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


    @AssertTrue(groups = TabelaHorariosCheck.class,
            message = "Existem eventos configurados no mesmo dia e horário.")
    public boolean isEventosMesmoDiaEHora() {
        if (!this.getTabelaHorario().getEventos().isEmpty() && this.getHorario() != null && this.getDiaDaSemana() != null) {
            return !(this.getTabelaHorario().getEventos().stream().filter(
                    evento -> this.getDiaDaSemana().equals(evento.getDiaDaSemana()) && this.getHorario().equals(evento.getHorario())).count() > 1);
        }
        return true;
    }

    @AssertTrue(groups = TabelaHorariosCheck.class,
            message = "não pode ficar em branco")
    public boolean isDiaDaSemana() {
        if (this.isEventoNormal()) {
            return this.getDiaDaSemana() != null;
        }
        return true;
    }

    public void setDiaDaSemana(DiaDaSemana diaDaSemana) {
        this.diaDaSemana = diaDaSemana;
    }

    @AssertTrue(groups = TabelaHorariosCheck.class,
            message = "não pode ficar em branco")
    public boolean isData() {
        if (this.isEventoEspecialRecorrente() || this.isEventoEspecialNaoRecorrente()) {
            return this.getData() != null;
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;

        return id != null ? id.equals(evento.id) : evento.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean isEventoNormal() {
        return TipoEvento.NORMAL.equals(this.getTipo());
    }

    public boolean isEventoEspecialRecorrente() {
        return TipoEvento.ESPECIAL_RECORRENTE.equals(this.getTipo());
    }

    public boolean isEventoEspecialNaoRecorrente() {
        return TipoEvento.ESPECIAL_NAO_RECORRENTE.equals(this.getTipo());
    }


    @Override
    public int compareTo(Evento o) {
        if(this.getTipo().compareTo(o.getTipo()) == 0){
            if(this.getTipo().equals(TipoEvento.NORMAL)) {
                if (this.getDiaDaSemana().compareTo(o.getDiaDaSemana()) == 0) {
                    return this.getData().compareTo(o.getData());
                } else {
                    return this.getDiaDaSemana().compareTo(o.getDiaDaSemana());
                }
            }else {
                return this.getData().compareTo(o.getData());
            }
        }else{
            return this.getTipo().compareTo(o.getTipo());
        }
    }

    @Override
    public String toString() {
        return "Evento{" +
                "posicaoPlano=" + posicaoPlano +
                ", data=" + data +
                ", diaDaSemana=" + diaDaSemana +
                ", tipo=" + tipo +
                '}';
    }

    public boolean tenhoPrioridade(Evento evento, boolean euSouPetrio, boolean outroEPetrio) {

        if(euSouPetrio || !outroEPetrio){
            return true;
        }else if(!euSouPetrio || outroEPetrio){
            return false;
        }else{
            return true;
        }
//        Calendar calendar = CustomCalendar.getCalendar();
//
//        if(instante < calendar.getTime().getTime()){
//            calendar.add(Calendar.SECOND, (int) instante);
//        }else{
//            calendar = Calendar.getInstance();
//            calendar.setTime(new Date(instante));
//        }
//
//        if(this.tipo.equals(TipoEvento.NORMAL) && evento.getTipo().equals(TipoEvento.NORMAL)){
//
//            if(getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK)) && !evento.getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK)) ){
//                //A instancia atual esta no dia dela e a outra nao
//                return true;
//            }else if(!getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK))  && evento.getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK))) {
//                //A instancia atual esta nao esta no dia dela e a outra nao
//                return false;
//            }else{
//                Calendar meuCalendar = Calendar.getInstance();
//
//                meuCalendar.setTime(getData());
//                int meuInstanteRelativo = CustomCalendar.getTempoAjustado(meuCalendar);
//
//                meuCalendar.setTime(evento.getData());
//                int outraInstanteRelativo = CustomCalendar.getTempoAjustado(meuCalendar);
//
//                if(insercao){
//                    return true;
//                }
//                if(getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK))  && evento.getDiaDaSemana().contains(calendar.get(Calendar.DAY_OF_WEEK))){
//                    return  meuInstanteRelativo < outraInstanteRelativo;
//                }else{
//                    return true;
//                }
//            }
//        }
//        return false;
    }
}
