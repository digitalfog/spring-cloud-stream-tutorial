package com.example.springcloudstreams.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.springcloudstreams.avro.MyAvroEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Import(TestChannelBinderConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class SpringCloudStreamIntegrationFlow_ChannelBindersTest {

  @Autowired private InputDestination input;
  @Autowired private OutputDestination output;

  @Test
  void sendMessageViaInputDestination() throws Exception {
    input.send(
        MessageBuilder.withPayload(MyAvroEvent.newBuilder().setGreeting("helloWorld").build())
            .build(),
        "my-second-topic-avro");

    Message<byte[]> outputMessage = output.receive(5000, "my-third-topic-avro");
    // for some reason it does not want to cast or decode payload. But implicit cast to String -
    // works.
    String payload = "" + outputMessage.getPayload();
    assertThat(payload).contains("[Forward]");
    assertThat(outputMessage.getHeaders().get("My-Header-Forward", String.class)).isEqualTo("true");
  }
}
