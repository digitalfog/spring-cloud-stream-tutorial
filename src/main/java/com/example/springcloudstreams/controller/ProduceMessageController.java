package com.example.springcloudstreams.controller;

import com.example.springcloudstreams.avro.MyAvroEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequestMapping("/greeting")
public class ProduceMessageController {

  @Autowired private StreamBridge streamBridge;

  @PostMapping
  public String produceMessage(@RequestParam String channel, @RequestParam String greeting) {
    log.info(">>>>> Sending message [{}] to channel [{}]", greeting, channel);

    Object payload = createPayload(channel, greeting);
    streamBridge.send(channel, payload, MimeType.valueOf("application/avro"));

    return "redirect:message_sent.html";
  }

  private Object createPayload(String channel, String greeting) {
    switch (channel) {
      case "avroTopic-stringSerializer-producer":
        return greeting;
      default:
        return MessageBuilder.withPayload(MyAvroEvent.newBuilder().setGreeting(greeting).build())
            .setHeader("My-Header", "my-header-value")
            .build();
    }
  }
}
