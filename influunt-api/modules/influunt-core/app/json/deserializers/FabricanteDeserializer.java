package json.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.Fabricante;
import models.ModeloControlador;
import play.libs.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pedropires on 6/19/16.
 */
public class FabricanteDeserializer extends JsonDeserializer<Fabricante> {

    @Override
    public Fabricante deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        Fabricante fabricante = new Fabricante();

        if (node.has("id")) {
            JsonNode id = node.get("id");
            if (!id.isNull()) {
                fabricante.setId(UUID.fromString(id.asText()));
            }
        }
        if (node.get("nome") != null) {
            fabricante.setNome(node.get("nome").asText());
        }

        if (node.get("modelos") != null) {
            List<ModeloControlador> modelos = new ArrayList<ModeloControlador>();
            for (JsonNode nodeModelo : node.get("modelos")) {
                modelos.add(Json.fromJson(nodeModelo, ModeloControlador.class));
            }
            fabricante.setModelos(modelos);
        }

        return fabricante;
    }
}
