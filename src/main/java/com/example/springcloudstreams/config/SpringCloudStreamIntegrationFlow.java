package com.example.springcloudstreams.config;

import com.example.springcloudstreams.avro.MyAvroEvent;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.GenericEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.handler.GenericHandler;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

@Configuration
@Slf4j
public class SpringCloudStreamIntegrationFlow {

  private AtomicInteger counter = new AtomicInteger(0);

  @Bean
  public IntegrationFlow avroTopicIntegrationFlow() {
    return IntegrationFlows.from(
            MessageConsumer.class, gateway -> gateway.beanName("avroTopicIntegrationConsumer"))
        .filter(Message.class, this::filterHandler, spec -> spec.id("filter-handler"))
        .handle(
            (GenericHandler<? extends MyAvroEvent>) this::messageHandler,
            (GenericEndpointSpec<ServiceActivatingHandler> spec) -> spec.id("message-handler"))
        .get();
  }

  private MyAvroEvent messageHandler(MyAvroEvent payload, MessageHeaders headers) {
    if (payload.getGreeting().toString().contains("err") && counter.getAndIncrement() < 3) {
      throw new RuntimeException("Exception-if-greeting-contains-err");
    }
    counter.set(0);
    log.info(">>>>> HANDLER. Headers = {}, Payload = {}", headers, payload);
    return payload;
  }

  private boolean filterHandler(Message<MyAvroEvent> msg) {
    log.info(
        ">>>>> Filter. Header = {}, greeting = {}",
        msg.getHeaders().get("My-Header", String.class),
        msg.getPayload().getGreeting());
    return true;
  }

  interface MessageConsumer extends Consumer<Message<MyAvroEvent>> {}
}
