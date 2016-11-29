package integracao;

import config.WithInfluuntApplicationNoAuthentication;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.*;
import io.moquette.server.Server;
import io.moquette.server.config.IConfig;
import io.moquette.server.config.MemoryConfig;
import models.Controlador;
import org.junit.After;
import org.junit.Before;
import os72c.client.Client;
import os72c.client.conf.DeviceConfig;
import os72c.client.conf.TestDeviceConfig;
import server.Central;
import status.StatusConexaoControlador;
import status.StatusControladorFisico;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;


/**
 * Created by rodrigosol on 9/8/16.
 */
public class BasicMQTTTest extends WithInfluuntApplicationNoAuthentication {

    protected Server mqttBroker;

    protected Central central;

    protected List<String> onConnectFutureList = new ArrayList<>();

    protected List<String> onDisconectFutureList = new ArrayList<>();

    protected List<String> onSubscribeFutureList = new ArrayList<>();

    protected List<byte[]> onPublishFutureList = new ArrayList<>();

    protected Client client;

    protected PlayJongo jongo;

    protected Controlador controlador;

    protected String idControlador;

    protected DeviceConfig deviceConfig;


    @Before
    public void setup() throws IOException, InterruptedException {
        controlador = new ControladorHelper().getControlador();
        idControlador = controlador.getId().toString();
        this.deviceConfig = new TestDeviceConfig();
        this.deviceConfig.setDeviceId(controlador.getId().toString());
        this.deviceConfig.setCentralPublicKey(controlador.getCentralPublicKey());
        this.deviceConfig.setPrivateKey(controlador.getControladorPrivateKey());

        setConfig();
    }

    @After
    public void cleanUp() {
        client.finish();
        central.finish();
        mqttBroker.stopServer();
        mqttBroker = null;
        onConnectFutureList.clear();
        onDisconectFutureList.clear();
        onSubscribeFutureList.clear();
        onPublishFutureList.clear();
        System.gc();
    }

    protected void setConfig() throws IOException, InterruptedException {
        Properties properties = new Properties();
        properties.put("persistent_store", "");

        final IConfig classPathConfig = new MemoryConfig(properties);
        List<? extends InterceptHandler> userHandlers = asList(new InterceptHandler() {
            @Override
            public void onConnect(InterceptConnectMessage interceptConnectMessage) {
                onConnectFutureList.add(interceptConnectMessage.getClientID());
            }

            @Override
            public void onDisconnect(InterceptDisconnectMessage interceptDisconnectMessage) {
                onDisconectFutureList.add(interceptDisconnectMessage.getClientID());
            }

            @Override
            public void onPublish(InterceptPublishMessage interceptPublishMessage) {
                onPublishFutureList.add(interceptPublishMessage.getPayload().array());
                System.out.println("onPublishFutureList.size() : " + onPublishFutureList.size());
                System.out.println("MSG : " + interceptPublishMessage.getTopicName());
            }

            @Override
            public void onSubscribe(InterceptSubscribeMessage interceptSubscribeMessage) {
                onSubscribeFutureList.add(interceptSubscribeMessage.getTopicFilter());
            }

            @Override
            public void onUnsubscribe(InterceptUnsubscribeMessage interceptUnsubscribeMessage) {
            }
        });

        jongo = provideApp.injector().instanceOf(PlayJongo.class);
        StatusConexaoControlador.jongo = jongo;
        StatusControladorFisico.jongo = jongo;

        jongo.getCollection(StatusConexaoControlador.COLLECTION).drop();
        jongo.getCollection(StatusControladorFisico.COLLECTION).drop();

        mqttBroker = new Server();
        mqttBroker.startServer(classPathConfig, userHandlers);
        Thread.sleep(100);
        central = provideApp.injector().instanceOf(Central.class);
    }

    protected void startClient() {
        client = provideApp.injector().instanceOf(Client.class);
    }

}
