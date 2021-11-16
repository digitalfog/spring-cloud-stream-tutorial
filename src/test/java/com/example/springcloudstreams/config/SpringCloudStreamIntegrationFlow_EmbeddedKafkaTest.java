package com.example.springcloudstreams.config;

import com.example.springcloudstreams.avro.MyAvroEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.MimeType;

@EmbeddedKafka(partitions = 1)
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringCloudStreamIntegrationFlow_EmbeddedKafkaTest {

  @Autowired private StreamBridge streamBridge;

  @Test
  void sendMessageViaInputDestination() throws Exception {
    streamBridge.send(
        "second-avroTopic-producer",
        MessageBuilder.withPayload(MyAvroEvent.newBuilder().setGreeting("helloWorld").build())
            .build(),
        MimeType.valueOf("application/avro"));

    Thread.sleep(10000);
  }
}
