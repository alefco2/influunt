package protocol;

/**
 * Created by rodrigosol on 9/6/16.
 */
public enum TipoMensagem {
    CONTROLADOR_ONLINE,
    CONTROLADOR_OFFLINE,
    ECHO,
    CONFIGURACAO_INICIAL,
    CONFIGURACAO,
    OK,
    ERRO,
    MUDANCA_STATUS_CONTROLADOR,
    TRANSACAO,
    ENVIO_PLANOS,
    ALARME_FALHA,
    TROCA_DE_PLANO,
    IMPOSICAO_DE_PLANO,
    REMOCAO_FALHA;
}
