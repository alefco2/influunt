package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import json.deserializers.InfluuntDateTimeDeserializer;
import json.serializers.InfluuntDateTimeSerializer;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * Entidade que representa um {@link Agrupamento} no sistema
 *
 * @author Pedro Pires
 */
@Entity
@Table(name = "agrupamentos")
public class Agrupamento extends Model {

    public static Finder<UUID, Agrupamento> find = new Finder<UUID, Agrupamento>(Agrupamento.class);

    @Id
    private UUID id;

    @Column
    @Enumerated(EnumType.STRING)
    private TipoAgrupamento tipo;

    @ManyToMany
    @JoinTable(name = "agrupamentos_controladores", joinColumns = {@JoinColumn(name = "agrupamento_id")}, inverseJoinColumns = {@JoinColumn(name = "controlador_id")})
    private List<Controlador> controladores;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Controlador> getControladores() {
        return controladores;
    }

    public void setControladores(List<Controlador> controladores) {
        this.controladores = controladores;
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

    public TipoAgrupamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoAgrupamento tipo) {
        this.tipo = tipo;
    }
}
