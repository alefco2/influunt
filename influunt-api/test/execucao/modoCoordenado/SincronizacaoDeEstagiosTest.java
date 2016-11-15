package execucao.modoCoordenado;

import engine.Motor;
import execucao.GerenciadorDeTrocasTest;
import models.Evento;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * Created by rodrigosol on 9/8/16.
 */
public class SincronizacaoDeEstagiosTest extends GerenciadorDeTrocasTest {

    @Test
    public void monentoCicloTroca() {
        controlador.getTabelaHoraria();
        Evento evento = controlador.getTabelaHoraria().getEventos().stream().filter(evento1 -> evento1.getPosicao().equals(13)).findFirst().get();

        assertEquals(20000L, evento.getMomentoEntrada(1).longValue());
        assertEquals(10000L, evento.getMomentoEntrada(2).longValue());

        evento.setHorario(new LocalTime(23, 0, 40));

        assertEquals(2000L, evento.getMomentoEntrada(1).longValue());
        assertEquals(50000L, evento.getMomentoEntrada(2).longValue());
    }

    @Test
    public void entradaCorretaDePlanos() {
        inicioControlador = new DateTime(2016, 11, 15, 22, 59, 30);
        inicioExecucao = inicioControlador;
        instante = inicioControlador;
        Motor motor = new Motor(controlador, inicioControlador, inicioExecucao, this);

        avancarSegundos(motor, 150);

        assertEquals("Plano Atual", 6, listaTrocaPlano.get(inicioExecucao).getPosicaoPlano().intValue());
        assertEquals("Plano Atual", 3, listaTrocaPlano.get(inicioExecucao.plusSeconds(30)).getPosicaoPlano().intValue());

        assertEquals("Plano Atual", 3, getPlanoTrocaEfetiva(1, 47).getPosicao().intValue());
        assertEquals("Plano Atual", 3, getPlanoTrocaEfetiva(2, 31).getPosicao().intValue());
    }

}