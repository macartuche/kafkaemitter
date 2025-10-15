package ec.gob.loja.kafkalistener.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.gob.loja.web.rest.dto.NotificacionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/kafka")
public class KafkaResource {

    private final Logger log = LoggerFactory.getLogger(KafkaResource.class);

    private final StreamBridge streamBridge;

    public KafkaResource(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendMessageToKafka(@RequestBody NotificacionDTO notificacion) {
        log.info("REST request to send message to Kafka for citizen {}: {}", notificacion.getIdentification(), notificacion.toString());

        String jsonPayload;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonPayload = mapper.writeValueAsString(notificacion);
            Message<String> message = MessageBuilder.withPayload(jsonPayload)
                .setHeader(KafkaHeaders.KEY, notificacion.getIdentification())
                .build();

            streamBridge.send("notificaciones-ciudadano-out-0", message);

            return ResponseEntity.ok().body("{\"mensaje\": \"MENSAJE ENVIADO\"}");
        } catch (JsonProcessingException e) {
            log.error("Error al serializar NotificacionDTO a JSON", e);
        }

        String errorMsg = "{\"mensaje\": \"ERROR: Fallo al publicar en Kafka.\"}";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMsg);
    }
}
