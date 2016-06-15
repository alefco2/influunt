import com.fasterxml.jackson.databind.JsonNode;
import controllers.routes;
import models.Area;
import org.junit.Assert;
import org.junit.Test;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;
import security.Authenticator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.*;
import static play.inject.Bindings.bind;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.route;

public class AreasControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("DATABASE_TO_UPPER", "FALSE");
        return getApplication(inMemoryDatabase("default", options));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Application getApplication(Map configuration) {
        return new GuiceApplicationBuilder().configure(configuration)
                .overrides(bind(Authenticator.class).to(TestAuthenticator.class)).in(Mode.TEST).build();
    }

    @Test
    public void testCriarNovaArea() {
        Area area = new Area();
        area.setDescricao("CTA 1");

        Http.RequestBuilder postRequest = new Http.RequestBuilder().method("POST")
                .uri(routes.AreasController.create().url()).bodyJson(Json.toJson(area));
        Result postResult = route(postRequest);
        JsonNode json = Json.parse(Helpers.contentAsString(postResult));
        Area areaRetornada = Json.fromJson(json, Area.class);

        assertEquals(200, postResult.status());
        assertEquals("CTA 1", areaRetornada.getDescricao());
        assertNotNull(areaRetornada.getId());
    }

    @Test
    public void testAtualizarAreaNaoExistente() {
        Area area = new Area();
        area.setDescricao("CTA 1");

        Http.RequestBuilder putRequest = new Http.RequestBuilder().method("PUT")
                .uri(routes.AreasController.update(UUID.randomUUID().toString()).url())
                .bodyJson(Json.toJson(area));
        Result putResult = route(putRequest);
        assertEquals(404, putResult.status());
    }

    @Test
    public void testAtualizarAreaExistente() {
        Area area = new Area();
        area.setDescricao("CTA 1");
        area.save();


        UUID areaId = area.getId();
        assertNotNull(areaId);

        Area areaSalvada = Area.find.byId(areaId);

        Area novaArea = new Area();
        novaArea.setDescricao("Teste atualizar");

        Http.RequestBuilder request = new Http.RequestBuilder().method("PUT")
                .uri(routes.AreasController.update(areaId.toString()).url())
                .bodyJson(Json.toJson(novaArea));

        Result result = route(request);
        assertEquals(200, result.status());

        JsonNode json = Json.parse(Helpers.contentAsString(result));
        Area areaRetornada = Json.fromJson(json, Area.class);

        assertEquals("Teste atualizar", areaRetornada.getDescricao());
        assertNotNull(areaRetornada.getId());
    }

    @Test
    public void testApagarAreaExistente() {
        Area area = new Area();
        area.setDescricao("Teste");
        area.save();

        Http.RequestBuilder request = new Http.RequestBuilder().method("DELETE")
                .uri(routes.AreasController.delete(area.getId().toString()).url());
        Result result = route(request);

        assertEquals(200, result.status());
        assertNull(Area.find.byId(area.getId()));
    }

    @Test
    public void testApagarAreaNaoExistente() {
        Http.RequestBuilder deleteRequest = new Http.RequestBuilder().method("DELETE")
                .uri(routes.AreasController.delete(UUID.randomUUID().toString()).url());
        Result result = route(deleteRequest);
        assertEquals(404, result.status());
    }

    @Test
    public void testListarAreas() {
        Area area = new Area();
        area.setDescricao("CTA-1");
        area.save();

        Area area1 = new Area();
        area1.setDescricao("CTA-2");
        area1.save();

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.AreasController.findAll().url());
        Result result = route(request);
        JsonNode json = Json.parse(Helpers.contentAsString(result));
        List<Area> areas = Json.fromJson(json, List.class);

        assertEquals(200, result.status());
        assertEquals(2, areas.size());
    }

    @Test
    public void testBuscarDadosArea() {
        Area area = new Area();
        area.setDescricao("Teste");
        area.save();
        UUID areaId = area.getId();
        Assert.assertNotNull(areaId);

        Http.RequestBuilder request = new Http.RequestBuilder().method("GET")
                .uri(routes.AreasController.findOne(areaId.toString()).url());
        Result result = route(request);
        JsonNode json = Json.parse(Helpers.contentAsString(result));
        Area areaRetornada = Json.fromJson(json, Area.class);

        assertEquals(200, result.status());
        assertEquals(areaId, areaRetornada.getId());
    }

}
