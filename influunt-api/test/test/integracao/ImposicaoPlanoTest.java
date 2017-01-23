package test.integracao;

import models.Anel;
import models.Plano;
import org.junit.Test;
import utils.TransacaoHelper;

import java.io.IOException;
import java.util.Arrays;

import static org.awaitility.Awaitility.await;

/**
 * Created by rodrigosol on 6/22/16.
 */
public class ImposicaoPlanoTest extends BasicMQTTTest {

    @Test
    public void imporPlanoOK() throws IOException {
        controlador = new ControladorHelper().setPlanos(controlador);
        startClient();
        await().until(() -> onPublishFutureList.size() > 6);

        Anel anel = getAnel(1);
        Plano plano = anel.getPlanos().get(0);

        long horarioEntrada = System.currentTimeMillis() + 60000L;
        imporPlano(plano.getPosicao(), anel, horarioEntrada, 30);
        assertTransacaoOk();
    }

    @Test
    public void imporPlanoComErro() throws IOException {
        controlador = new ControladorHelper().setPlanos(controlador);
        startClient();
        await().until(() -> onPublishFutureList.size() > 6);

        imporPlano(-1, getAnel(1), System.currentTimeMillis(), -1);
        assertTransacaoErro();
    }

    private void imporPlano(int posicaoPlano, Anel anel, Long horarioEntrada, int duracao) {
        TransacaoHelper transacaoHelper = provideApp.injector().instanceOf(TransacaoHelper.class);
        transacaoHelper.imporPlano(Arrays.asList(anel), posicaoPlano, horarioEntrada, duracao, 60000L);
    }

}