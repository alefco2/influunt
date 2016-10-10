package simulador.parametros;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import engine.EventoMotor;
import engine.TipoEvento;
import json.deserializers.InfluuntDateTimeDeserializer;
import json.serializers.InfluuntDateTimeSerializer;
import models.Detector;
import models.TipoDetector;
import org.joda.time.DateTime;



/**
 * Created by rodrigosol on 10/4/16.
 */
public class ParametroSimulacaoDetector {
    private Detector detector;

    @JsonDeserialize(using = InfluuntDateTimeDeserializer.class)
    @JsonSerialize(using = InfluuntDateTimeSerializer.class)
    private DateTime disparo;

    public Detector getDetector() {
        return detector;
    }

    public void setDetector(Detector detector) {
        this.detector = detector;
    }

    public DateTime getDisparo() {
        return disparo;
    }

    public void setDisparo(DateTime disparo) {
        this.disparo = disparo;
    }

    public EventoMotor toEvento(){
        if(detector.getTipo().equals(TipoDetector.PEDESTRE)) {
            return new EventoMotor(disparo, TipoEvento.ACIONAMENTO_DETECTOR_PEDESTRE, detector);
        }else{
            return new EventoMotor(disparo, TipoEvento.ACIONAMENTO_DETECTOR_VEICULAR, detector);
        }
    }
}