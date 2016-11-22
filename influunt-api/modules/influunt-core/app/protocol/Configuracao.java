package protocol;

import com.google.gson.Gson;
import json.ControladorCustomSerializer;
import models.Controlador;
import models.StatusVersao;
import utils.RangeUtils;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rodrigosol on 9/6/16.
 */
public class Configuracao {


    private Configuracao() {
    }

    public static Envelope getMensagem(Envelope envelope) {
        Controlador controlador = Controlador.find.byId(UUID.fromString(envelope.getIdControlador()));
        RangeUtils rangeUtils = RangeUtils.getInstance(null);
        if (controlador != null && !controlador.getVersaoControlador().getStatusVersao().equals(StatusVersao.EM_CONFIGURACAO)) {
            Map map = new Gson().fromJson(envelope.getConteudo().toString(),Map.class);
            controlador.setControladorPublicKey(map.get("publicKey").toString());
            controlador.update();
            return new Envelope(TipoMensagem.CONFIGURACAO,
                envelope.getIdControlador(),
                "controlador/".concat(envelope.getIdControlador()).concat("/configuracao"),
                2,
                new ControladorCustomSerializer().getControladorJson(controlador, Collections.singletonList(controlador.getArea().getCidade()), rangeUtils).toString(),
                envelope.getIdMensagem());
        } else {
            return new Envelope(TipoMensagem.ERRO,
                envelope.getIdControlador(),
                "controlador/".concat(envelope.getIdControlador()).concat("/configuracao"),
                2,
                null,
                envelope.getIdMensagem());
        }

    }
}
