package security;

import com.avaje.ebean.event.changelog.BeanChange;
import models.Usuario;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import play.api.Play;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lesiopinheiro on 9/2/16.
 */
public class Auditoria {

    public static PlayJongo jongo = Play.current().injector().instanceOf(PlayJongo.class);

    public static MongoCollection auditorias() {
        return jongo.getCollection("auditorias");
    }

    public String _id;

    public BeanChange change;

    public Usuario usuario;

    public Long timestamp = System.currentTimeMillis();

    public static Auditoria findById(String id) {
        return auditorias().findOne("{ _id: # }", new ObjectId(id)).as(Auditoria.class);
    }

    public static List<Auditoria> findAll() {
        MongoCursor<Auditoria> auditorias = auditorias().find().sort("{timestamp: -1}").as(Auditoria.class);
        return toList(auditorias);
    }

    public static List<Auditoria> findAllByLogin(String login) {
        MongoCursor<Auditoria> auditorias = auditorias().find("{usuario.login: #}", login).sort("{timestamp: -1}").as(Auditoria.class);
        return toList(auditorias);
    }

    public static List<Auditoria> findAllByLoginAndTable(String login, String tableName) {
        MongoCursor<Auditoria> auditorias = auditorias().find("{usuario.login: #, change.table: #}", login, tableName).sort("{timestamp: -1}").as(Auditoria.class);
        return toList(auditorias);
    }

    public static List<Auditoria> findAllByTable(String tableName) {
        MongoCursor<Auditoria> auditorias = auditorias().find("{change.table: #}", tableName).sort("{timestamp: -1}").as(Auditoria.class);
        return toList(auditorias);
    }

    public static void deleteAll() {
        auditorias().drop();
    }

    public void insert() {
        auditorias().insert(this);
    }

    @NotNull
    private static List<Auditoria> toList(MongoCursor<Auditoria> auditorias) {
        ArrayList<Auditoria> lista = new ArrayList<Auditoria>(auditorias.count());
        auditorias.forEach(auditoria -> {
            lista.add(auditoria);
        });
        return lista;
    }

}