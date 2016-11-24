package reports;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import models.Anel;
import models.Controlador;
import models.StatusDevice;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import status.TrocaDePlanoControlador;
import utils.InfluuntQueryBuilder;
import utils.InfluuntUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by lesiopinheiro on 10/10/16.
 */

//TODO: Verificar se pode ser excluida essa clase
public class ControladoresReportService extends ReportService<Controlador> {

    @Inject
    private BaseJasperReport baseJasperReport;

    public InputStream generateControladoresFalhasReport(Map<String, String[]> params, ReportType reportType) {
        switch (reportType) {
            case PDF:
                return generateControladoresFalhasPDFReport(params);
            case CSV:
                return generateControladoresFalhasCSVReport();
            default:
                return generateControladoresFalhasCSVReport();
        }
    }

    @Override
    public InputStream generateReport(Map<String, String[]> params, List<Controlador> lista, ReportType reportType) {
        throw new NotImplementedException("Metodo nao implentado. Favor utilizar outro metodo gerador.");
    }

    public ObjectNode getControladoresStatusReportData(Map<String, String[]> params) {
        List<TrocaDePlanoControlador> trocas = TrocaDePlanoControlador.ultimasTrocasDePlanosDosControladores();
        List<String> aneisIds = new ArrayList<>();

        Map<String, String[]> paramsAux = new HashMap<>();
        paramsAux.putAll(params);
        paramsAux.remove("tipoRelatorio");

        if(params.containsKey("filtrarPor_eq")) {
            if ("Subarea".equalsIgnoreCase(params.get("filtrarPor_eq")[0])) {
                if (params.containsKey("subareaAgrupamento")) {
                    paramsAux.put("subarea.nome", params.get("subareaAgrupamento"));
                    paramsAux.remove("subareaAgrupamento");
                    paramsAux.remove("filtrarPor_eq");
                }
                List<Controlador> controladores = (List<Controlador>) new InfluuntQueryBuilder(Controlador.class, paramsAux).fetch(Arrays.asList("subarea", "aneis")).query().getResult();
                controladores.stream().forEach(c -> c.getAneis().forEach(a -> aneisIds.add(a.getId().toString())));

            } else if ("Agrupamento".equalsIgnoreCase(params.get("filtrarPor_eq")[0])) {
                if (params.containsKey("subareaAgrupamento")) {
                    paramsAux.put("agrupamentos.nome", new String[]{params.get("subareaAgrupamento")[0]});
                    paramsAux.remove("subareaAgrupamento");
                    paramsAux.remove("filtrarPor_eq");
                }

                List<Anel> aneis = (List<Anel>) new InfluuntQueryBuilder(Anel.class, paramsAux).fetch(Arrays.asList("agrupamentos")).query().getResult();
                aneis.stream().forEach(a -> aneisIds.add(a.getId().toString()));
            }
        }

        List<Anel> aneis = Anel.find.select("id, descricao, posicao, endereco").fetch("controlador.subarea").where().in("id", trocas.stream().map((TrocaDePlanoControlador troca) -> troca.getIdAnel()).collect(Collectors.toList())).findList();

        ArrayNode itens = JsonNodeFactory.instance.arrayNode();
        aneis.forEach(anel -> {
            if(aneisIds.isEmpty() || aneisIds.contains(anel.getId().toString())) {
                TrocaDePlanoControlador troca = trocas.stream().filter(t -> t.getIdAnel().equals(anel.getId().toString())).findFirst().get();
                final String estado;
                final String descricaoPlano;
                if (troca.getConteudo().get("impostoPorFalha").booleanValue()) {
                    estado = "Em falha";
                    descricaoPlano = "";
                } else if (troca.getConteudo().get("imposicaoDePlano").booleanValue()) {
                    estado = "Sob Imposição de Plano";
                    descricaoPlano = troca.getConteudo().get("plano").get("descricao").asText();
                } else if (troca.getConteudo().get("plano").get("modoOperacao").asText().equalsIgnoreCase("MANUAL")) {
                    estado = "Em Operação Manual";
                    descricaoPlano = troca.getConteudo().get("plano").get("descricao").asText();
                } else {
                    estado = "Sob plano vigente";
                    descricaoPlano = troca.getConteudo().get("plano").get("descricao").asText();
                }
                itens.addObject().put("cla", anel.getCLA().toString()).putPOJO("endereco", anel.getEndereco().nomeEndereco()).put("estado", estado).put("plano", descricaoPlano).put("modo", troca.getConteudo().get("plano").get("modoOperacao").asText());
            }
        });

        ObjectNode retorno = JsonNodeFactory.instance.objectNode();
        retorno.putArray("data").addAll(itens);

        return retorno;
    }

    /**
     * Retorna um CSV com dados dos {@link models.Controlador} por {@link StatusDevice}
     * <p>
     * FORMATO: CLA | ENDERECO | ESTADO | PLANO | MODO
     *
     * @return {@link InputStream} do csv
     */
    public InputStream generateControladoresStatusCSVReport(Map<String, String[]> params) {
        StringBuilder buffer = new StringBuilder();

        buffer.append("Relatório de Controladores por Status").append(NEW_LINE_SEPARATOR);

        buffer.append("Gerado em:").append(COMMA_DELIMITER).append(InfluuntUtils.formatDateToString(new DateTime(), FORMAT_DATE_HOUR_COMPLETE));
        buffer.append(NEW_LINE_SEPARATOR).append(NEW_LINE_SEPARATOR);

        // Write the CSV file header
        buffer.append("CLA").append(COMMA_DELIMITER).append("ENDEREÇO").append(COMMA_DELIMITER)
            .append("ESTADO").append(COMMA_DELIMITER).append("PLANO").append(COMMA_DELIMITER)
            .append("MODO").append(NEW_LINE_SEPARATOR);

        ObjectNode retorno = getControladoresStatusReportData(params);
        retorno.get("data").forEach(jsonNode -> {
            buffer.append(StringUtils.defaultIfBlank(jsonNode.get("cla").asText(), StringUtils.EMPTY)).append(COMMA_DELIMITER)
                .append(StringUtils.defaultIfBlank(jsonNode.get("endereco").asText(), StringUtils.EMPTY)).append(COMMA_DELIMITER)
                .append(StringUtils.defaultIfBlank(jsonNode.get("estado").asText(), StringUtils.EMPTY)).append(COMMA_DELIMITER)
                .append(StringUtils.defaultIfBlank(jsonNode.get("plano").asText(), StringUtils.EMPTY)).append(COMMA_DELIMITER)
                .append(StringUtils.defaultIfBlank(jsonNode.get("modo").asText(), StringUtils.EMPTY)).append(NEW_LINE_SEPARATOR);
        });

        return new ByteArrayInputStream(buffer.toString().getBytes(Charset.forName("UTF-8")));
    }


    /**
     * Retorna um PDF dos {@link models.Controlador} por Falhas
     *
     * @return {@link InputStream} do pdf
     */
    private InputStream generateControladoresFalhasPDFReport(Map<String, String[]> queryStringParams) {
        Map<String, Object> params = controladoresFalhasReportParams(queryStringParams);
        return baseJasperReport.generateReport("controladoresFalhas", params, getControladoresPorFalhas());
    }

    /**
     * Retorna um CSV com dados dos {@link models.Controlador} por Falhas
     * <p>
     * FORMATO: STATUS | TOTAL
     *
     * @return {@link InputStream} do csv
     */
    private InputStream generateControladoresFalhasCSVReport() {
        StringBuilder buffer = new StringBuilder();

        buffer.append("Relatório de Controladores por Falhas").append(NEW_LINE_SEPARATOR);

        buffer.append("Gerado em:").append(COMMA_DELIMITER).append(InfluuntUtils.formatDateToString(new DateTime(), FORMAT_DATE_HOUR_COMPLETE));
        buffer.append(NEW_LINE_SEPARATOR).append(NEW_LINE_SEPARATOR);

        // Write the CSV file header
        buffer.append("Status").append(COMMA_DELIMITER)
            .append("Total").append(NEW_LINE_SEPARATOR);

        for (ControladorFalhasVO controladorFalhasVO : getControladoresPorFalhas()) {
            buffer.append(StringUtils.defaultIfBlank(controladorFalhasVO.getNomeFabricante(), StringUtils.EMPTY)).append(NEW_LINE_SEPARATOR);
            for (FalhaPorFabricanteVO falha : controladorFalhasVO.getFalhas()) {
                buffer.append(StringUtils.defaultIfBlank(falha.getFalha(), StringUtils.EMPTY)).append(COMMA_DELIMITER)
                    .append(StringUtils.defaultIfBlank(falha.getTotal().toString(), StringUtils.EMPTY)).append(NEW_LINE_SEPARATOR);
            }

        }


        return new ByteArrayInputStream(buffer.toString().getBytes(Charset.forName("UTF-8")));
    }

    private List<ControladorFalhasVO> getControladoresPorFalhas() {
//        Gson gson = new Gson();
//        ArrayList<ControladorStatusVO> controladores = new ArrayList<>();
//        for (String erro : errosPorFabricantes) {
//            JsonObject jobj = gson.fromJson(erro, JsonObject.class);
//            JsonObject controladorJson = jobj.get("_id").getAsJsonObject();
//            ControladorStatusVO statusVO = new ControladorStatusVO(controladorJson.get("status").getAsString(), jobj.get("total").getAsInt());
//            statusVO.setIdFabricante(controladorJson.get("idFabricante").getAsString());
//            controladores.add(statusVO);
//        }
//
//        Map<String, List<ControladorStatusVO>> controladoresAux = controladores.stream().collect(Collectors.groupingBy(ControladorStatusVO::getIdFabricante));
//
//        ArrayList<ControladorFalhasVO> controladoresFalhas = new ArrayList<>();
//        for (Map.Entry<String, List<ControladorStatusVO>> entry : controladoresAux.entrySet()) {
//            Fabricante fabricante = Fabricante.find.byId(UUID.fromString(entry.getKey()));
//            ControladorFalhasVO controladorFalha = new ControladorFalhasVO();
//            controladorFalha.setIdFabricante(entry.getKey());
//            controladorFalha.setNomeFabricante(fabricante.getNome());
//            for (ControladorStatusVO statusAux : entry.getValue()) {
//                controladorFalha.addFalha(new FalhaPorFabricanteVO(statusAux.getStatus(), statusAux.getTotal()));
//            }
//
//            controladoresFalhas.add(controladorFalha);
//        }
//
//        return controladoresFalhas;
        return null;
    }

    private Map<String, Object> controladoresFalhasReportParams(Map<String, String[]> queryStringParams) {
        Map<String, Object> params = getBasicReportMetadata();
        params.put("relatorioPor", "Fabricante");
        return params;
    }
}
