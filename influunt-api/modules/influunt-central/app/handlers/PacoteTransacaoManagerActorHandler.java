package handlers;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import protocol.Envelope;
import protocol.TipoMensagem;
import status.PacoteTransacao;
import status.StatusPacoteTransacao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rodrigosol on 9/6/16.
 */
public class PacoteTransacaoManagerActorHandler extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final Map<String, ActorRef> pacoteTransacoes = new HashMap<>();

    public void onReceive(Object message) throws Exception {
        if (message instanceof Envelope) {
            Envelope envelope = (Envelope) message;
            if (envelope.getTipoMensagem().equals(TipoMensagem.PACOTE_TRANSACAO)) {
                JsonNode pacoteTransacaoJson = Json.parse(envelope.getConteudo().toString());

                PacoteTransacao pacoteTransacao = PacoteTransacao.fromJson(pacoteTransacaoJson);

                ActorRef ref = null;

                if (pacoteTransacao.getStatusPacoteTransacao().equals(StatusPacoteTransacao.NEW)) {
                    ref = getContext().actorOf(Props.create(PacoteTransacaoActorHandler.class,
                        pacoteTransacao, getSelf()),
                        "pacote-transacao-" + pacoteTransacao.getId());

                    pacoteTransacoes.put(pacoteTransacao.getId(), ref);
                }
            }
        }
    }

}
