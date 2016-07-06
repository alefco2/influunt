package models;

import com.google.inject.Singleton;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;
import security.AllowAllAuthenticator;
import security.Authenticator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static play.inject.Bindings.bind;
import static play.test.Helpers.inMemoryDatabase;

/**
 * Created by rodrigosol on 6/22/16.
 */
public abstract class ControladorTest extends WithApplication {

    private static Cidade cidade;
    private static Area area;
    private static Fabricante fabricante;
    private static ConfiguracaoControlador configuracaoControlador;
    private static ModeloControlador modeloControlador;

    @Override
    protected Application provideApplication() {
        Map<String, String> options = new HashMap<String, String>();
        options.put("DATABASE_TO_UPPER", "FALSE");
        return getApplication(inMemoryDatabase("default", options));
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private Application getApplication(Map configuration) {
        return new GuiceApplicationBuilder().configure(configuration)
                .overrides(bind(Authenticator.class).to(AllowAllAuthenticator.class).in(Singleton.class))
                .in(Mode.TEST).build();
    }

    protected Controlador getControlador() {
        return new Controlador();
    }

    protected Controlador getControladorDadosBasicos() {

        cidade = new Cidade();
        cidade.setNome("São Paulo");
        cidade.save();

        area = new Area();
        area.setCidade(cidade);
        area.setDescricao(1);
        area.save();

        fabricante = new Fabricante();
        fabricante.setNome("Tesc");
        fabricante.save();

        configuracaoControlador = new ConfiguracaoControlador();
        configuracaoControlador.setLimiteAnel(4);
        configuracaoControlador.setLimiteGrupoSemaforico(16);
        configuracaoControlador.setLimiteDetectorPedestre(4);
        configuracaoControlador.setLimiteDetectorVeicular(8);
        configuracaoControlador.setLimiteEstagio(16);
        configuracaoControlador.save();

        modeloControlador = new ModeloControlador();
        modeloControlador.setFabricante(fabricante);
        modeloControlador.setConfiguracao(configuracaoControlador);
        modeloControlador.setDescricao("Modelo 1");
        modeloControlador.save();

        Controlador controlador = getControlador();
        controlador.setLocalizacao("Av Paulista com Bela Cintra");
        controlador.setLatitude(1.0);
        controlador.setLongitude(2.0);
        controlador.setArea(this.area);
        controlador.setModelo(this.modeloControlador);
        controlador.setNumeroSMEE("1234");
        controlador.setNumeroSMEEConjugado1("C1");
        controlador.setNumeroSMEEConjugado2("C2");
        controlador.setNumeroSMEEConjugado3("C3");
        controlador.setFirmware("1.0rc");

        return controlador;
    }

    protected Controlador getControladorAneis() {
        Controlador controlador = getControladorDadosBasicos();
        controlador.save();

        Anel anel1 = controlador.getAneis().get(0);
        anel1.setAtivo(true);
        anel1.setEstagios(Arrays.asList(new Estagio(), new Estagio(), new Estagio(), new Estagio()));

        anel1.setLatitude(1.0);
        anel1.setLongitude(1.0);
        anel1.setQuantidadeGrupoPedestre(1);
        anel1.setQuantidadeGrupoVeicular(1);
        anel1.setQuantidadeDetectorPedestre(4);
        anel1.setQuantidadeDetectorVeicular(8);

        return controlador;
    }

    protected Controlador getControladorAssociacao() {
        Controlador controlador = getControladorAneis();
        controlador.save();

        Anel anelAtivo = controlador.getAneis().stream().filter(anel -> anel.isAtivo()).findFirst().get();

        Estagio estagio1 = anelAtivo.getEstagios().get(0);
        Estagio estagio2 = anelAtivo.getEstagios().get(1);
        Estagio estagio3 = anelAtivo.getEstagios().get(2);
        Estagio estagio4 = anelAtivo.getEstagios().get(3);

        GrupoSemaforico grupoSemaforico1 = anelAtivo.getGruposSemaforicos().get(0);
        grupoSemaforico1.setTipo(TipoGrupoSemaforico.PEDESTRE);
        GrupoSemaforico grupoSemaforico2 = anelAtivo.getGruposSemaforicos().get(1);
        grupoSemaforico2.setTipo(TipoGrupoSemaforico.VEICULAR);


        EstagioGrupoSemaforico estagioGrupoSemaforico1 = new EstagioGrupoSemaforico(estagio1, grupoSemaforico1);
        EstagioGrupoSemaforico estagioGrupoSemaforico2 = new EstagioGrupoSemaforico(estagio2, grupoSemaforico2);
        EstagioGrupoSemaforico estagioGrupoSemaforico3 = new EstagioGrupoSemaforico(estagio3, grupoSemaforico1);
        EstagioGrupoSemaforico estagioGrupoSemaforico4 = new EstagioGrupoSemaforico(estagio4, grupoSemaforico2);

        estagio1.setDemandaPrioritaria(true);
        estagio1.setTempoMaximoPermanencia(100);
        estagio1.addEstagioGrupoSemaforico(estagioGrupoSemaforico1);
        estagio2.setDemandaPrioritaria(false);
        estagio2.setTempoMaximoPermanencia(200);
        estagio2.addEstagioGrupoSemaforico(estagioGrupoSemaforico2);

        estagio3.addEstagioGrupoSemaforico(estagioGrupoSemaforico3);
        estagio4.addEstagioGrupoSemaforico(estagioGrupoSemaforico4);

        grupoSemaforico1.addEstagioGrupoSemaforico(estagioGrupoSemaforico1);
        grupoSemaforico2.addEstagioGrupoSemaforico(estagioGrupoSemaforico2);
        grupoSemaforico1.addEstagioGrupoSemaforico(estagioGrupoSemaforico3);
        grupoSemaforico2.addEstagioGrupoSemaforico(estagioGrupoSemaforico4);

        return controlador;
    }

    protected Controlador getControladorVerdesConflitantes() {
        Controlador controlador = getControladorAssociacao();

        Anel anelAtivo = controlador.getAneis().stream().filter(anel -> !anel.isAtivo()).findFirst().get();
        anelAtivo.setAtivo(Boolean.TRUE);
        anelAtivo.setLatitude(1.0);
        anelAtivo.setLongitude(1.0);

        anelAtivo.setQuantidadeGrupoVeicular(2);
        anelAtivo.setEstagios(Arrays.asList(new Estagio(), new Estagio()));

        Estagio estagio1 = anelAtivo.getEstagios().get(0);
        Estagio estagio2 = anelAtivo.getEstagios().get(1);

        controlador.save();

        GrupoSemaforico grupoSemaforico3 = anelAtivo.getGruposSemaforicos().get(0);
        grupoSemaforico3.setTipo(TipoGrupoSemaforico.VEICULAR);
        GrupoSemaforico grupoSemaforico4 = anelAtivo.getGruposSemaforicos().get(1);
        grupoSemaforico4.setTipo(TipoGrupoSemaforico.VEICULAR);


        EstagioGrupoSemaforico estagioGrupoSemaforico1 = new EstagioGrupoSemaforico(estagio1, grupoSemaforico3);
        estagio1.addEstagioGrupoSemaforico(estagioGrupoSemaforico1);
        EstagioGrupoSemaforico estagioGrupoSemaforico2 = new EstagioGrupoSemaforico(estagio2, grupoSemaforico4);
        estagio2.addEstagioGrupoSemaforico(estagioGrupoSemaforico2);

        grupoSemaforico3.addEstagioGrupoSemaforico(estagioGrupoSemaforico1);
        grupoSemaforico4.addEstagioGrupoSemaforico(estagioGrupoSemaforico2);

        GrupoSemaforico grupoSemaforico1 = controlador.getGruposSemaforicos().get(0);
        GrupoSemaforico grupoSemaforico2 = controlador.getGruposSemaforicos().get(1);

        grupoSemaforico1.setVerdesConflitantes(Arrays.asList(grupoSemaforico2));
        grupoSemaforico2.setVerdesConflitantes(Arrays.asList(grupoSemaforico1));
        grupoSemaforico3.setVerdesConflitantes(Arrays.asList(grupoSemaforico4));
        grupoSemaforico4.setVerdesConflitantes(Arrays.asList(grupoSemaforico3));

        grupoSemaforico1.setDescricao("G1");
        grupoSemaforico2.setDescricao("G2");
        grupoSemaforico3.setDescricao("G3");
        grupoSemaforico4.setDescricao("G4");

        return controlador;
    }

    protected Controlador getControladorTransicoesProibidas() {
        Controlador controlador = getControladorVerdesConflitantes();

        Anel anelCom4Estagios = controlador.getAneis().stream().filter(anel -> anel.isAtivo() && anel.getEstagios().size() == 4).findFirst().get();

        Estagio estagio1AnelCom4Estagios = anelCom4Estagios.getEstagios().get(0);
        estagio1AnelCom4Estagios.setDescricao("estagio1AnelCom4Estagios");
        Estagio estagio2AnelCom4Estagios = anelCom4Estagios.getEstagios().get(1);
        estagio2AnelCom4Estagios.setDescricao("estagio2AnelCom4Estagios");
        Estagio estagio3AnelCom4Estagios = anelCom4Estagios.getEstagios().get(2);
        estagio3AnelCom4Estagios.setDescricao("estagio3AnelCom4Estagios");
        Estagio estagio4AnelCom4Estagios = anelCom4Estagios.getEstagios().get(3);
        estagio4AnelCom4Estagios.setDescricao("estagio4AnelCom4Estagios");

        TransicaoProibida transicaoProibida = new TransicaoProibida();
        transicaoProibida.setOrigem(estagio1AnelCom4Estagios);
        transicaoProibida.setDestino(estagio2AnelCom4Estagios);
        transicaoProibida.setAlternativo(estagio4AnelCom4Estagios);

        TransicaoProibida transicaoProibidaEstagio1ComEstagio3 = new TransicaoProibida();
        transicaoProibidaEstagio1ComEstagio3.setOrigem(estagio1AnelCom4Estagios);
        transicaoProibidaEstagio1ComEstagio3.setDestino(estagio3AnelCom4Estagios);
        transicaoProibidaEstagio1ComEstagio3.setAlternativo(estagio4AnelCom4Estagios);


        estagio1AnelCom4Estagios.setOrigemDeTransicoesProibidas(Arrays.asList(transicaoProibida, transicaoProibidaEstagio1ComEstagio3));
        estagio2AnelCom4Estagios.setDestinoDeTransicoesProibidas(Arrays.asList(transicaoProibida));
        estagio3AnelCom4Estagios.setDestinoDeTransicoesProibidas(Arrays.asList(transicaoProibidaEstagio1ComEstagio3));
        estagio4AnelCom4Estagios.setAlternativaDeTransicoesProibidas(Arrays.asList(transicaoProibida, transicaoProibidaEstagio1ComEstagio3));


        return controlador;
    }


    protected Controlador getControladorEstagios() {
        Controlador controlador = getControladorVerdesConflitantes();
        return controlador;
    }

    protected Controlador getControladorTabelaDeEntreVerdes() {
        Controlador controlador = getControladorEstagios();
        return controlador;
    }

    protected Controlador getControladorConfiguracaoGrupoSemafórico() {
        Controlador controlador = getControladorEstagios();
        return controlador;
    }

    public abstract void testVazio();

    public abstract void testNoValidationErro();

    public abstract void testORM();

    public abstract void testJSON();

    public abstract void testControllerValidacao();

    public abstract void testController();


}
