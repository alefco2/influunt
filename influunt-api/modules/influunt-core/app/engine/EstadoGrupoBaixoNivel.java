package engine;

import models.EstadoGrupoSemaforico;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by rodrigosol on 9/15/16.
 */
public class EstadoGrupoBaixoNivel {
    public static final byte DESLIGADO = 0;

    public static final byte VERDE = 1;

    public static final byte VERMELHO = 2;

    public static final byte AMARELO = 3;

    public static final byte VERMELHO_INTERMITENTE = 4;

    public static final byte AMARELO_INTERMITENTE = 5;

    public static final byte VERMELHO_LIMPEZA = 6;

    private byte[] estadoGlobal = new byte[8];

    public void mudar(int grupo, byte estado) {
        int index = (grupo - 1) / 2;
        if (grupo % 2 != 0) {
            estadoGlobal[index] = (byte) (estado << 4);
        } else {
            estadoGlobal[index] |= estado;
        }
    }

    public byte[] ler() {
        return estadoGlobal;
    }

}