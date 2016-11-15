package device;

import config.WithInfluuntApplicationNoAuthentication;
import config.WithLocalInfluuntApplicationNoAuthentication;
import org.junit.Test;
import os72c.client.Client;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Created by rodrigosol on 11/15/16.
 */
public class DeviceClientTest extends WithLocalInfluuntApplicationNoAuthentication{

    @Test
    public void execucao() throws InterruptedException {

        Client client = provideApp.injector().instanceOf(Client.class);
        Thread.sleep(600000l);

    }
}
