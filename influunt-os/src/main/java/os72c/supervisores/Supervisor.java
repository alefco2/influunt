package os72c.supervisores;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.google.common.collect.ImmutableList;
import com.typesafe.config.Config;
import os72c.Application;
import os72c.Constants;
import os72c.controladores.ControladorI2C;
import os72c.controladores.ControladorSerial;
import os72c.models.EstadoGrupo;
import os72c.models.Intervalos;
import os72c.models.Troca;
import os72c.procolos.EventoControladorSupervisor;
import os72c.procolos.TipoEvento;

/**
 * Created by rodrigosol on 6/24/16.
 */
public class Supervisor extends UntypedActor {
    private Intervalos intervalos;
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    Long i = 0l;
    private ActorRef controlador;
    private ImmutableList<Troca> trocas;
    private Config conf;
    private boolean falhando = false;


    @Override
    public void preStart() throws Exception {
        conf = Application.conf72c.getConfig("local.driver");
        String args[] = new String[6];
        if (conf.getString("type").equals("serial")) {
            setupSerial(args);
            controlador = getContext().actorOf(Props.create(ControladorSerial.class), "controlador");
        } else if (conf.getString("type").equals("i2c")) {
            controlador = getContext().actorOf(Props.create(ControladorI2C.class), "controlador");
        } else {
            //Modo de comunicação com o hardware não definido
            throw new RuntimeException("O tipo de driver não foi especificado");
        }
        intervalos = new Intervalos(getContext(), controlador, getSelf());
        controlador.tell(new EventoControladorSupervisor(TipoEvento.SUPERVISOR_PRONTO, null, args), getSelf());

    }

    private void setupSerial(String[] args) {
        args[Constants.SERIAL_PORTA] = conf.getString("serial.porta");
        args[Constants.SERIAL_BAUDRATE] = conf.getString("serial.baudrate");
        args[Constants.SERIAL_DATABITS] = conf.getString("serial.databits");
        args[Constants.SERIAL_STOPBITS] = conf.getString("serial.stopbits");
        args[Constants.SERIAL_PARITY] = conf.getString("serial.parity");
        args[Constants.SERIAL_START_DELAY] = conf.getString("serial.startdelay");
    }

    public static Props props() {
        return Props.create(Supervisor.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {


        if (message instanceof EventoControladorSupervisor) {
            final EventoControladorSupervisor eventoControladorSupervisor = (EventoControladorSupervisor) message;
            System.out.println(eventoControladorSupervisor);
            log.debug(eventoControladorSupervisor.toString());
            switch (eventoControladorSupervisor.tipoEvento) {
                case CONTROLADOR_PRONTO:
                    registraControlador(eventoControladorSupervisor);
                    break;
                case PROXIMO_CICLO:
                    if(!falhando) {
                        intervalos.proximoCiclo(Integer.valueOf(eventoControladorSupervisor.argumentos[0]));
                    }
                    break;
                case MUDANCA_MANUAL:
                    if(!falhando) {
                        intervalos.proximoEstagio();
                    }
                    break;
                case FALHA:
                    falhando = true;
                    intervalos.falha();
                    break;

            }
        }
    }

    private void registraControlador(EventoControladorSupervisor eventoControladorSupervisor) {
        trocas = ImmutableList.of(
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERDE, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO_INTERMITENTE,EstadoGrupo.VERMELHO, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.AMARELO, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO_INTERMITENTE,EstadoGrupo.VERMELHO, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO, EstadoGrupo.DESLIGADO),
                new Troca(18000, EstadoGrupo.VERDE, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO,EstadoGrupo.VERDE, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.AMARELO, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO,EstadoGrupo.VERDE, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO,EstadoGrupo.VERMELHO,EstadoGrupo.VERDE, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO, EstadoGrupo.VERDE,EstadoGrupo.VERDE,EstadoGrupo.VERDE, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO_INTERMITENTE,EstadoGrupo.VERDE,EstadoGrupo.VERMELHO_INTERMITENTE, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO, EstadoGrupo.VERMELHO,EstadoGrupo.VERDE,EstadoGrupo.VERMELHO, EstadoGrupo.DESLIGADO),
                new Troca(6000, EstadoGrupo.VERMELHO, EstadoGrupo.VERDE, EstadoGrupo.VERMELHO,EstadoGrupo.VERDE,EstadoGrupo.VERMELHO, EstadoGrupo.DESLIGADO)
        );


        intervalos.setTrocas(trocas);
        intervalos.start(0);

    }

}


