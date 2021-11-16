package com.example.springcloudstreams.config;

import com.example.springcloudstreams.avro.MyAvroEvent;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.AbstractMessageListenerContainer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

@Configuration
@Slf4j
public class SpringCloudStreamConfiguration {

  @Bean
  public Consumer<MyAvroEvent> avroTopicFunctionConsumer() {
    return msg -> log.info(">>>>> [CONSUMER] avroTopic. Consumed message [{}]", msg);
  }

  // custom listener container error handler to handle deserializationErrors
  // see
  // https://github.com/spring-cloud/spring-cloud-stream-samples/blob/main/recipes/recipe-3-handling-deserialization-errors-dlq-kafka.adoc
  @Bean
  public ListenerContainerCustomizer<AbstractMessageListenerContainer<byte[], byte[]>> customizer(
      SeekToCurrentErrorHandler errorHandler) {
    return (container, dest, group) -> {
      container.setErrorHandler(errorHandler);
    };
  }

  @Bean
  public SeekToCurrentErrorHandler errorHandler() {
    BiConsumer<ConsumerRecord<?, ?>, Exception> loggingRecoverer =
        (rec, ex) -> log.warn(">>>>> ERROR HANDLER {}", rec == null ? "null" : rec.toString(), ex);
    return new SeekToCurrentErrorHandler(loggingRecoverer);
  }
}
