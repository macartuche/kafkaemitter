package ec.gob.loja.kafkalistener;

import ec.gob.loja.kafkalistener.config.AsyncSyncConfiguration;
import ec.gob.loja.kafkalistener.config.EmbeddedSQL;
import ec.gob.loja.kafkalistener.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { KafkaemitterApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
