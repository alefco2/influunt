package models;

import checks.*;
import com.fasterxml.jackson.databind.JsonNode;
import controllers.routes;
import org.hamcrest.Matchers;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;

import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNPROCESSABLE_ENTITY;
import static play.test.Helpers.route;

/**
 * Created by rodrigosol on 6/22/16.
 */
public class ControladorGruposSemaforicosTest extends ControladorTest {

    @Override
    @Test
    public void testVazio() {

        Controlador controlador = getControladorAneis();
        controlador.save();

        List<Erro> erros = getErros(controlador);

        assertEquals(1, erros.size());
        assertThat(erros, Matchers.hasItems(
                new Erro("Controlador", "Esse anel deve ter no mínimo 2 grupos semáforicos", "aneis[0]")
        ));

        Anel anelAtivo = controlador.getAneis().stream().filter(anel -> anel.isAtivo()).findFirst().get();
        GrupoSemaforico grupoSemaforicoVeicular = new GrupoSemaforico();
        grupoSemaforicoVeicular.setAnel(anelAtivo);
        grupoSemaforicoVeicular.setTipo(TipoGrupoSemaforico.VEICULAR);
        anelAtivo.addGruposSemaforicos(grupoSemaforicoVeicular);

        GrupoSemaforico grupoSemaforicoPedestre = new GrupoSemaforico();
        grupoSemaforicoPedestre.setAnel(anelAtivo);
        anelAtivo.addGruposSemaforicos(grupoSemaforicoPedestre);

        erros = getErros(controlador);

        assertEquals(1, erros.size());
        assertThat(erros, Matchers.hasItems(
                new Erro("Controlador", "não pode ficar em branco", "aneis[0].gruposSemaforicos[1].tipo")
        ));

        grupoSemaforicoPedestre.setTipo(TipoGrupoSemaforico.PEDESTRE);

        erros = getErros(controlador);

        assertThat(erros, Matchers.empty());
    }

    @Override
    @Test
    public void testNoValidationErro() {
        Controlador controlador = getControladorGrupoSemaforicos();
        controlador.save();
        List<Erro> erros = getErros(controlador);
        assertThat(erros, Matchers.empty());
    }

    @Override
    @Test
    public void testORM() {
        Controlador controlador = getControladorGrupoSemaforicos();
        controlador.save();
        assertNotNull(controlador.getId());
        assertEquals("Criação de grupos semafóricos no Controlador", 2, controlador.getGruposSemaforicos().size());
        Anel anelAtivo = controlador.getAneis().stream().filter(anel -> anel.isAtivo()).findFirst().get();
        assertEquals("Criação de grupos semafóricos no Anel", 2, anelAtivo.getGruposSemaforicos().size());

        GrupoSemaforico grupoVeicular = anelAtivo.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isVeicular()).findFirst().get();
        GrupoSemaforico grupoPedestre = anelAtivo.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isPedestre()).findFirst().get();

        assertEquals("Grupo Veicular", TipoGrupoSemaforico.VEICULAR, grupoVeicular.getTipo());
        assertEquals("Grupo Pedestre", TipoGrupoSemaforico.PEDESTRE, grupoPedestre.getTipo());

        assertTrue("Grupo Veicular com Fase apagada colocar em Amarelo Intermitente", grupoVeicular.getFaseVermelhaApagadaAmareloIntermitente());
        assertFalse("Grupo Pedestre com Fase apagada não colocar em Amarelo Intermitente", grupoPedestre.getFaseVermelhaApagadaAmareloIntermitente());
    }

    @Override
    @Test
    public void testJSON() {
        Controlador controlador = getControladorGrupoSemaforicos();
        controlador.save();

        Controlador controladorJson = Json.fromJson(Json.toJson(controlador), Controlador.class);

        assertEquals(controlador.getId(), controladorJson.getId());
        assertNotNull(controladorJson.getId());

        Anel anelAtivoJson = controladorJson.getAneis().stream().filter(anel -> anel.isAtivo()).findFirst().get();
        assertEquals("Criação de grupos semafóricos no Anel", 2, anelAtivoJson.getGruposSemaforicos().size());

        GrupoSemaforico grupoVeicularJson = anelAtivoJson.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isVeicular()).findFirst().get();
        GrupoSemaforico grupoPedestreJson = anelAtivoJson.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isPedestre()).findFirst().get();

        assertEquals("Grupo Veicular", TipoGrupoSemaforico.VEICULAR, grupoVeicularJson.getTipo());
        assertEquals("Grupo Pedestre", TipoGrupoSemaforico.PEDESTRE, grupoPedestreJson.getTipo());
    }


    @Override
    @Test
    public void testControllerValidacao() {
        Controlador controlador = getControladorAneis();
        controlador.save();

        Http.RequestBuilder postRequest = new Http.RequestBuilder().method("POST")
                .uri(routes.ControladoresController.gruposSemaforicos().url()).bodyJson(Json.toJson(controlador));
        Result postResult = route(postRequest);

        assertEquals(UNPROCESSABLE_ENTITY, postResult.status());

        JsonNode json = Json.parse(Helpers.contentAsString(postResult));
        assertEquals(1, json.size());

    }

    @Override
    @Test
    public void testController() {
        Controlador controlador = getControladorGrupoSemaforicos();


        Http.RequestBuilder postRequest = new Http.RequestBuilder().method("POST")
                .uri(routes.ControladoresController.gruposSemaforicos().url()).bodyJson(Json.toJson(controlador));
        Result postResult = route(postRequest);

        assertEquals(OK, postResult.status());

        JsonNode json = Json.parse(Helpers.contentAsString(postResult));
        Controlador controladorRetornado = Json.fromJson(json, Controlador.class);

        assertNotNull(controladorRetornado.getId());
        Anel anelAtivoJson = controladorRetornado.getAneis().stream().filter(anel -> anel.isAtivo()).findFirst().get();
        assertEquals("Criação de grupos semafóricos no Anel", 2, anelAtivoJson.getGruposSemaforicos().size());

        GrupoSemaforico grupoVeicularJson = anelAtivoJson.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isVeicular()).findFirst().get();
        GrupoSemaforico grupoPedestreJson = anelAtivoJson.getGruposSemaforicos().stream().filter(grupoSemaforico -> grupoSemaforico.isPedestre()).findFirst().get();

        assertEquals("Grupo Veicular", TipoGrupoSemaforico.VEICULAR, grupoVeicularJson.getTipo());
        assertEquals("Grupo Pedestre", TipoGrupoSemaforico.PEDESTRE, grupoPedestreJson.getTipo());
    }

    @Override
    public List<Erro> getErros(Controlador controlador) {
        return new InfluuntValidator<Controlador>().validate(controlador,
                Default.class, ControladorAneisCheck.class, ControladorGruposSemaforicosCheck.class);
    }
}
