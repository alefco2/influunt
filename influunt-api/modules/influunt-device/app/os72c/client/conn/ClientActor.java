package os72c.client.conn;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Function;
import os72c.client.device.DeviceActor;
import os72c.client.device.DeviceBridge;
import os72c.client.storage.Storage;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by rodrigosol on 7/7/16.
 */
public class ClientActor extends UntypedActor {

    private static SupervisorStrategy strategy =
        new OneForOneStrategy(-1, Duration.Undefined(),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable t) {
                    return SupervisorStrategy.stop();
                }
            }, false);

    private final String id;

    private final String host;

    private final String port;

    private final Storage storage;

    private final String centralPublicKey;

    private final String controladorPrivateKey;

    private ActorRef device;

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private ActorRef mqqtControlador;

    public ClientActor(final String id, final String host, final String port, final String centralPublicKey, final String controladorPrivateKey, Storage storage, DeviceBridge deviceBridge) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.storage = storage;
        this.centralPublicKey = centralPublicKey;
        this.controladorPrivateKey = controladorPrivateKey;
        if (storage.getCentralPublicKey() == null) {
            storage.setCentralPublicKey(centralPublicKey);
            storage.setPrivateKey(controladorPrivateKey);
        }
        this.device = getContext().actorOf(Props.create(DeviceActor.class, storage, deviceBridge, id), "motor");
    }


    @Override
    public void preStart() throws Exception {
        super.preStart();
        setup();
    }

    private void setup() {

        mqqtControlador = getContext().actorOf(Props.create(MQTTClientActor.class, id, host, port, storage), "ControladorMQTT");
        this.getContext().watch(mqqtControlador);
        mqqtControlador.tell("CONNECT", getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Terminated) {
            final Terminated t = (Terminated) message;
            getContext().system().scheduler().scheduleOnce(Duration.create(30, TimeUnit.SECONDS), getSelf(), "RESTART", getContext().system().dispatcher(), getSelf());
        } else if ("RESTART".equals(message)) {
            setup();
        }

    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return strategy;
    }
}


