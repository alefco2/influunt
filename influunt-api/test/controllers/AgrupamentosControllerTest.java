package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Singleton;
import models.*;
import org.junit.Test;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import security.AllowAllAuthenticator;
import security.Authenticator;

import java.util.*;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;

public class AgrupamentosControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("DATABASE_TO_UPPER", "FALSE");
        return getApplication(inMemoryDatabase("default", options));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Application getApplication(Map configuration) {
        Application app = new GuiceApplicationBuilder().configure(configuration)
                .overrides(bind(Authenticator.class).to(AllowAllAuthenticator.class).in(Singleton.class))
                .in(Mode.TEST).build();
        return app;
    }

    private Controlador getControlador() {

        Cidade cidade = new Cidade();
        cidade.setNome("São Paulo");
        cidade.save();

        Area area = new Area();
        area.setCidade(cidade);
        area.setDescricao(1);
        area.save();

        Fabricante fabricante = new Fabricante();
        fabricante.setNome("Tesc");
        fabricante.save();

        ModeloControlador modeloControlador = new ModeloControlador();
        modeloControlador.setFabricante(fabricante);
        modeloControlador.setDescricao("Modelo 1");
        modeloControlador.save();

        Controlador controlador = new Controlador();
        controlador.setArea(area);
        controlador.setModelo(modeloControlador);
        controlador.setNumeroSMEE("1234");
        controlador.setNumeroSMEEConjugado1("C1");
        controlador.setNumeroSMEEConjugado2("C2");
        controlador.setNumeroSMEEConjugado3("C3");
        controlador.setFirmware("1.0rc");
        controlador.setLimiteAnel(4);
        controlador.setLimiteGrupoSemaforico(16);
        controlador.setLimiteDetectorPedestre(4);
        controlador.setLimiteDetectorVeicular(8);
        controlador.setLimiteEstagio(16);
        Endereco enderecoPaulista = new Endereco();
        enderecoPaulista.setLocalizacao("Av Paulista");
        enderecoPaulista.setLatitude(1.0);
        enderecoPaulista.setLongitude(2.0);
        enderecoPaulista.setControlador(controlador);

        Endereco enderecoBelaCintra = new Endereco();
        enderecoBelaCintra.setLocalizacao("Rua Bela Cintra");
        enderecoBelaCintra.setLatitude(3.0);
        enderecoBelaCintra.setLongitude(4.0);
        enderecoBelaCintra.setControlador(controlador);

        controlador.addEndereco(enderecoPaulista);
        controlador.addEndereco(enderecoBelaCintra);
        controlador.setNomeEndereco("Av. Paulista com Bela Cintra");
        controlador.save();

        return controlador;
    }

    @Test
    public void testCriarNovoAgrupamento() {
        Controlador controlador1 = getControlador();
        controlador1.save();
        assertNotNull(controlador1.getId());

        List<Controlador> controladores = new ArrayList<Controlador>();
        controladores.add(controlador1);

        Agrupamento agrupamento = new Agrupamento();
        agrupamento.setNome("Teste");
        agrupamento.setTipo(TipoAgrupamento.SUBAREA);
        agrupamento.setControladores(controladores);

        Http.RequestBuilder request = new Http.RequestBuilder().method("POST")
                .uri(routes.AgrupamentosController.create().url()).bodyJson(Json.toJson(agrupamento));
        Result result = route(request);
        JsonNode json = Json.parse(Helpers.contentAsString(result));
        Agrupamento agrupamentoRetornado = Json.fromJson(json, Agrupamento.class);

        assertEquals(200, result.status());
        assertEquals(TipoAgrupamento.SUBAREA, agrupamentoRetornado.getTipo());
        assertEquals(1, agrupamentoRetornado.getControladores().size());
        assertNotNull(agrupamentoRetornado.getControladores().get(0).getId());
        assertNotNull(agrupamentoRetornado.getId());
    }

    @Test
    public void testAtualizarAgrupamentoNaoExistente() {
        Agrupamento agrupamento = new Agrupamento();

        Http.RequestBuilder putRequest = new Http.RequestBuilder().method("PUT")
                .uri(routes.AgrupamentosController.update(UUID.randomUUID().toString()).url())
                .bodyJson(Json.toJson(agrupamento));
        Result putResult = route(putRequest);
        assertEquals(404, putResult.status());
    }

    @Test
    public void testAtualizarAgrupamentoExistente() {
        Controlador controlador1 = getControlador();
        controlador1.save();
        assertNotNull(controlador1.getId());

        List<Controlador> controladores = new ArrayList<Controlador>();
        controladores.add(controlador1);

        Agrupamento agrupamento = new Agrupamento();
        agrupamento.setTipo(TipoAgrupamento.CORREDOR);
        agrupamento.setControladores(controladores);
        agrupamento.setNome("Teste");
        agrupamento.save();

        UUID agrupamentoId = agrupamento.getId();
        assertNotNull(agrupamentoId);


        Agrupamento novoAgrupamento = new Agrupamento();
        novoAgrupamento.setTipo(TipoAgrupamento.ROTA);
        novoAgrupamento.setControladores(controladores);
        novoAgrupamento.setNome("Teste 2");

        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .uri(routes.AgrupamentosController.update(agrupamentoId.toString()).url())
                .bodyJson(Json.toJson(novoAgrupamento));

        Result result = route(request);
        assertEquals(200, result.status());

        JsonNode json = Json.parse(Helpers.contentAsString(result));
        Agrupamento agrupamentoRetornado = Json.fromJson(json, Agrupamento.class);

        assertEquals(TipoAgrupamento.ROTA, agrupamentoRetornado.getTipo());
        assertEquals("Teste 2", agrupamentoRetornado.getNome());
        assertEquals(agrupamentoRetornado.getId(), agrupamentoId);
    }

    @Test
    public void testApagarAgrupamentoExistente() {
        Agrupamento agrupamento = new Agrupamento();
        agrupamento.setTipo(TipoAgrupamento.CORREDOR);
        agrupamento.save();
        assertNotNull(agrupamento.getId());

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(routes.AgrupamentosController.delete(agrupamento.getId().toString()).url());
        Result result = route(request);

        assertEquals(200, result.status());
        assertNull(Agrupamento.find.byId(agrupamento.getId()));
    }

    @Test
    public void testApagarAgrupamentoNaoExistente() {
        Http.RequestBuilder deleteRequest = new Http.RequestBuilder().method("DELETE")
                .uri(routes.AgrupamentosController.delete(UUID.randomUUID().toString()).url());
        Result result = route(deleteRequest);
        assertEquals(404, result.status());
    }

    @Test
    public void testListarAgrupamentos() {
        Agrupamento agrupamento = new Agrupamento();
        agrupamento.setTipo(TipoAgrupamento.SUBAREA);
        agrupamento.save();

        Agrupamento agrupamento1 = new Agrupamento();
        agrupamento1.setTipo(TipoAgrupamento.ROTA);
        agrupamento1.save();

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.AgrupamentosController.findAll().url());
        Result result = route(request);
        JsonNode json = Json.parse(Helpers.contentAsString(result));
        List<Agrupamento> agrupamentos = Json.fromJson(json, List.class);

        assertEquals(200, result.status());
        assertEquals(2, agrupamentos.size());
    }

    @Test
    public void testBuscarDadosAgrupamento() {
        Agrupamento agrupamento = new Agrupamento();
        agrupamento.setTipo(TipoAgrupamento.SUBAREA);
        agrupamento.save();
        UUID agrupamentoId = agrupamento.getId();
        assertNotNull(agrupamentoId);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.AgrupamentosController.findOne(agrupamentoId.toString()).url());
        Result result = route(request);
        JsonNode json = Json.parse(Helpers.contentAsString(result));
        Agrupamento agrupamentoRetornado = Json.fromJson(json, Agrupamento.class);

        assertEquals(200, result.status());
        assertEquals(agrupamentoId, agrupamentoRetornado.getId());
        assertEquals(TipoAgrupamento.SUBAREA, agrupamentoRetornado.getTipo());
    }

}
