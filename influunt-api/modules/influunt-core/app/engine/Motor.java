package engine;

import models.Controlador;
import models.EstadoGrupoSemaforico;
import models.Evento;
import models.Plano;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by rodrigosol on 9/26/16.
 */
public class Motor implements MotorEvents, GerenciadorDeIntervalosCallBack {
    private final Controlador controlador;

    private final DateTime inicioSimulacao;

    private GerenciadorDeEventos gerenciadorDeEventos;

    private List<MotorCallback> callbacks = new ArrayList<>();

    private Evento eventoAtual;

    private List<EstadoGrupoSemaforico> estadoAtual;

    public Motor(Controlador controlador, MotorCallback callback, DateTime inicioSimulacao) {
        callbacks.add(callback);
        this.controlador = controlador;
        this.inicioSimulacao = inicioSimulacao;
        gerenciadorDeEventos = new GerenciadorDeEventos();
        gerenciadorDeEventos.addEventos(controlador.getTabelaHoraria().getEventos());

    }

    public void start(DateTime timestamp) {
        callbacks.stream().forEach(motorCallback -> motorCallback.onStart(timestamp));
    }

    public void stop(DateTime timestamp) {
        callbacks.stream().forEach(motorCallback -> motorCallback.onStop(timestamp));
    }

    public void onEventoChange(DateTime timestamp, Evento atual, Evento novo) {
        for (MotorCallback callback : callbacks) {
            callback.onChangeEvento(timestamp, atual, novo);
        }
    }

    public void onGrupoChange(DateTime timestamp, List<EstadoGrupoSemaforico> atual, List<EstadoGrupoSemaforico> novo) {
        callbacks.stream().forEach(motorCallback -> motorCallback.onGrupoChange(timestamp, atual, novo));
    }

    @Override
    public void onAgendamentoDeTrocaDePlanos(DateTime timestamp, DateTime momento, int anel, int plano, int planoAnterior) {
        callbacks.stream().forEach(motorCallback -> motorCallback.onAgendamentoTrocaDePlanos(timestamp, momento, anel, plano, planoAnterior));
    }

    public void tick(DateTime instante) {
        Evento evento = gerenciadorDeEventos.eventoAtual(instante);
        boolean iniciarGrupos = false;
        if (eventoAtual == null) {
            onEventoChange(instante, null, evento);
            eventoAtual = evento;
            iniciarGrupos = true;
        } else {
            if (!evento.equals(eventoAtual)) {
                onEventoChange(instante, eventoAtual, evento);
                eventoAtual = evento;
//                gerenciadorDeIntervalos.trocarPlanos(instante, getPlanos(eventoAtual));
            }
        }

        if (iniciarGrupos) {
//            gerenciadorDeIntervalos = new GerenciadorDeIntervalos(inicioSimulacao, getPlanos(eventoAtual), this);
            iniciarGrupos = false;
        }

        if (estadoAtual == null) {
//            estadoAtual = gerenciadorDeIntervalos.getEstadosGrupo(instante);
            onGrupoChange(instante, null, estadoAtual);
        } else {
//            List<EstadoGrupoSemaforico> estadoGrupoSemaforico = gerenciadorDeIntervalos.getEstadosGrupo(instante);
//            if (!estadoAtual.equals(estadoGrupoSemaforico)) {
//                onGrupoChange(instante, estadoAtual, estadoGrupoSemaforico);
//                estadoAtual = estadoGrupoSemaforico;
//            }
        }

        callbacks.stream().forEach(motorCallback -> motorCallback.onEstado(instante, EstadoGrupoBaixoNivel.parse(estadoAtual)));
    }

    private List<Plano> getPlanos(Evento evento) {
        return controlador.getAneis().stream().sorted((a1, a2) -> a1.getPosicao().compareTo(a2.getPosicao()))
                .flatMap(anel -> anel.getPlanos().stream())
                .filter(plano -> plano.getPosicao() == eventoAtual.getPosicaoPlano())
                .collect(Collectors.toList());
    }


    @Override
    public void onEvento(EventoMotor eventoMotor) {
        switch (eventoMotor.getTipoEvento().getTipoEventoControlador()) {
            case ALARME:
                trataAlarme(eventoMotor);
                break;
            case FALHA:
                trataFalha(eventoMotor);
                break;
            case DETECTOR_PEDESTRE:
                trataDetectorPedestre(eventoMotor);
                break;
            case DETECTOR_VEICULAR:
                trataDetectorVeicular(eventoMotor);
                break;
            case IMPOSICAO_PLANO:
                trataImposicaoPlano(eventoMotor);
                break;
        }
    }

    private void trataImposicaoPlano(EventoMotor eventoMotor) {
    }

    private void trataDetectorVeicular(EventoMotor eventoMotor) {

    }

    private void trataDetectorPedestre(EventoMotor eventoMotor) {

    }

    private void trataFalha(EventoMotor eventoMotor) {

    }

    private void trataAlarme(EventoMotor eventoMotor) {

    }

}
