# spring-cloud-stream-tutorial

# Examples of Spring Cloud Streams (SCS)

### Links / Documentation

- Spring Cloud Stream doc  
  https://docs.spring.io/spring-cloud-stream/docs/3.1.4/reference/html/

- Consumer properties   
  https://docs.spring.io/spring-cloud-stream/docs/3.1.4/reference/html/spring-cloud-stream.html#_common_binding_properties

- Kafka binder doc   
  https://docs.spring.io/spring-cloud-stream-binder-kafka/docs/3.1.4/reference/html/spring-cloud-stream-binder-kafka.html

- Spring Cloud Stream Sample Applications
  https://github.com/spring-cloud/spring-cloud-stream-samples/


# Prerequisites

### run kafka in docker
```
docker compose up -d
```
or
```
lima nerdctl compose up -d
```

# Tutorial 1
### Send and consume message using Avro and String serializers. Shows how to override default configs for producer/consumer.
1. Run SpringCloudStreamsApplication
2. Open in browser localhost:8080
3. Select `avroTopic-producer`, type any message and hit send button\
The message will be sent to a topic using Avro serializer and consumed by SpringCloudStreamConfiguration->avroTopicFunctionConsumer\
Check corresponding logs in console output.
4. Select `avroTopic-stringSerializer-producer`, type any message and hit send button\
The message will be sent using String serializer. During consumption there will be deserializationException and processing will be delegated to errorHandler.\
Check corresponding logs in console output.

