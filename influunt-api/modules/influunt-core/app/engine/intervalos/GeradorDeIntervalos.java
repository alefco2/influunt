package engine.intervalos;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import engine.IntervaloEstagio;
import models.EstagioPlano;
import models.ModoOperacaoPlano;
import models.Plano;
import org.apache.commons.math3.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rodrigosol on 10/24/16.
 */
public abstract class GeradorDeIntervalos {
    protected final HashMap<Pair<Integer, Integer>, Long> tabelaDeTemposEntreVerde;
    protected RangeMap<Long, IntervaloEstagio> intervalos;
    protected final ModoOperacaoPlano modoAnterior;
    protected final Plano plano;
    protected final EstagioPlano estagioPlanoAtual;
    protected final List<EstagioPlano> listaEstagioPlanos;

    public abstract Pair<Integer, RangeMap<Long, IntervaloEstagio>> gerar(final int index);

    public GeradorDeIntervalos(RangeMap<Long, IntervaloEstagio> intervalos, Plano plano,
                               ModoOperacaoPlano modoAnterior, List<EstagioPlano> listaEstagioPlanos,
                               EstagioPlano estagioPlanoAtual, HashMap<Pair<Integer, Integer>, Long> tabelaDeTemposEntreVerde) {
        this.intervalos = intervalos;
        this.modoAnterior = modoAnterior;
        this.plano = plano;
        this.listaEstagioPlanos = listaEstagioPlanos;
        this.estagioPlanoAtual = estagioPlanoAtual;
        this.tabelaDeTemposEntreVerde = tabelaDeTemposEntreVerde;
    }

    public static GeradorDeIntervalos getInstance(RangeMap<Long, IntervaloEstagio> intervalos, Plano plano,
                                                  ModoOperacaoPlano modoAnterior, List<EstagioPlano> listaEstagioPlanos,
                                                  EstagioPlano estagioPlanoAtual, HashMap<Pair<Integer, Integer>, Long> tabelaDeTemposEntreVerde,
                                                  int index) {
        if ((!plano.isModoOperacaoVerde() && index == 0) || (!plano.isModoOperacaoVerde() && modoAnterior != null && !modoAnterior.equals(plano.getModoOperacao()))){
            return new GeradorIntermitente(intervalos, plano, modoAnterior, listaEstagioPlanos, estagioPlanoAtual, tabelaDeTemposEntreVerde);
        } else if (!isModoAnteriorVerde(modoAnterior)) {
            return new GeradorVermelhoIntegral(intervalos, plano, modoAnterior, listaEstagioPlanos, estagioPlanoAtual, tabelaDeTemposEntreVerde);
        } else {
            return new GeradorModosVerde(intervalos, plano, modoAnterior, listaEstagioPlanos, estagioPlanoAtual, tabelaDeTemposEntreVerde);
        }
    }

    protected static boolean isModoAnteriorVerde(ModoOperacaoPlano modoAnterior) {
        return  !ModoOperacaoPlano.APAGADO.equals(modoAnterior) && !ModoOperacaoPlano.INTERMITENTE.equals(modoAnterior);
    }

    protected void geraIntervaloEstagio(EstagioPlano estagioPlano, long tempoEntreVerde, long tempoVerde){
        this.intervalos = TreeRangeMap.create();
        this.intervalos.put(Range.closedOpen(0L, tempoEntreVerde), new IntervaloEstagio(tempoEntreVerde, true, estagioPlano, estagioPlanoAtual));
        this.intervalos.put(Range.closedOpen(tempoEntreVerde, tempoEntreVerde + tempoVerde), new IntervaloEstagio(tempoVerde, false, estagioPlano, estagioPlanoAtual));
    }
}