package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Usuario;
import org.junit.Before;
import org.junit.Test;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.Helpers;
import play.test.WithApplication;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.*;

public class SecurityControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return getApplication(inMemoryDatabase());
    }

    private Application getApplication(Map configuration) {
        return new GuiceApplicationBuilder().configure(configuration)
                .in(Mode.TEST).build();
    }

    @Before
    public void setup() {
        Http.Context context = new Http.Context(fakeRequest());
        context.args.put("user", null);
        Http.Context.current.set(context);
    }

    @Test
    public void testUnauthorizedLogin() throws InterruptedException, ExecutionException {
        Http.RequestBuilder postRequest = new Http.RequestBuilder().method("POST")
                .uri(routes.SecurityController.login().url());
        Result postResult = route(postRequest);
        assertEquals(UNAUTHORIZED, postResult.status());
    }

    @Test
    public void testAuthorizedLogin() throws InterruptedException, ExecutionException {

        Usuario usuario = new Usuario();
        usuario.setNome("Admin");
        usuario.setLogin("admin");
        usuario.setSenha("1234");
        usuario.setRoot(true);
        usuario.setEmail("root@influunt.com.br");
        usuario.save();
        JsonNode jsonUsuario = Json.parse("{\"login\":\"admin\",\"senha\":\"1234\"}");

        Http.RequestBuilder postRequest = new Http.RequestBuilder().method("POST")
                .uri(routes.SecurityController.login().url()).bodyJson(jsonUsuario);
        Result postResult = route(postRequest);
        assertEquals(OK, postResult.status());

        JsonNode json = Json.parse(Helpers.contentAsString(postResult));

        usuario = Json.fromJson(json, Usuario.class);
        assertEquals("admin", usuario.getLogin());
        assertEquals("Admin", usuario.getNome());

    }

}
