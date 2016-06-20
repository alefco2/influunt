package models.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import models.Area;
import models.Cidade;
import models.LimiteArea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by pedropires on 6/19/16.
 */
public class AreaDeserializer extends JsonDeserializer<Area> {

    @Override
    public Area deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        Area area = new Area();

        JsonNode id = node.get("id");
        if (id != null) {
            area.setId(UUID.fromString(node.get("id").asText()));
        }
        area.setDescricao(node.get("descricao").asInt());

        Cidade cidade = new Cidade();
        cidade.setId(UUID.fromString(node.get("cidade").get("id").asText()));
        area.setCidade(cidade);

        if (node.has("limites") && node.get("limites") != null) {
            List<LimiteArea> limites = new ArrayList<LimiteArea>();
            for (JsonNode nodeLimite : node.get("limites")) {
                LimiteArea limite = new LimiteArea();
                limite.setLatitude(nodeLimite.get("latitude").asDouble());
                limite.setLongitude(nodeLimite.get("longitude").asDouble());
                limites.add(limite);
            }
            area.setLimitesGeograficos(limites);
        }

        return area;
    }
}
