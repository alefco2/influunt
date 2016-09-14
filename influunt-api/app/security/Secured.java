package security;

import be.objectify.deadbolt.java.models.Subject;
import com.google.inject.Inject;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

    private InfluuntContextManager ctxManager;

    @Inject
    public Secured(InfluuntContextManager ctxManager) {
        this.ctxManager = ctxManager;
    }

    @Override
    public String getUsername(Context ctx) {
        Subject usuario = ctxManager.getUsuario(ctx);
        if (usuario != null) {
            return usuario.getIdentifier();
        }
        return null;

//        Subject usuario = null;
//        final String[] authTokenHeaderValues = ctx.request().headers().get(SecurityController.AUTH_TOKEN);
//        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1)
//                && (authTokenHeaderValues[0] != null)) {
//            usuario = authenticator.getSubjectByToken(authTokenHeaderValues[0]);
//        } else {
//            usuario = authenticator.getSubjectByToken("");
//        }
//        if (usuario != null) {
//            ctx.args.put("user", usuario);
//            return usuario.getIdentifier();
//        }
//
//        return null;
    }

    @Override
    public Result onUnauthorized(final Context ctx) {
        return unauthorized();
    }

}
