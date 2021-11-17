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
use tag `simple-function-consumer`
```
git checkout simple-function-consumer
```
### Send and consume message using Avro and String serializers. Shows how to override default configs for producer/consumer.
1. Run SpringCloudStreamsApplication
2. Open in browser localhost:8080
3. Select `avroTopic-producer`, type any message and hit send button\
The message will be sent to a topic using Avro serializer and consumed by SpringCloudStreamConfiguration->avroTopicFunctionConsumer\
Check corresponding logs in console output.
4. Select `avroTopic-stringSerializer-producer`, type any message and hit send button\
The message will be sent using String serializer. During consumption there will be deserializationException and processing will be delegated to errorHandler.\
Check corresponding logs in console output.

# Tutorial 2
use tag `integration-flow-consumer`
```
git checkout integration-flow-consumer
```
### Send and consume messages via IntegrationFlow and publish metrics
1. Run SpringCloudStreamsApplication
2. Open in browser localhost:8080
3. Select `second-avroTopic-producer`, type any message and hit send button\
   The message will be sent to a topic using Avro serializer and consumed by SpringCloudStreamIntegrationFlow->avroTopicIntegrationFlow\
   Check corresponding logs in console output.\
   Check metrics.
4. Send message "err". It will throw exception and will be retried 3 times.\
   Check metrics.

### Metrics
#### Lag per consumer group
Metric: `spring.cloud.stream.binder.kafka.offset`  
Example:  
`http://localhost:8080/actuator/metrics/spring.cloud.stream.binder.kafka.offset?tag=group:cloud-streams-tutorial-group-1`

#### A collection of metrics/timers per each channel/handler/source differentiated by tags
Metric: `spring.integration.send`  
Example:  
`http://localhost:8080/actuator/metrics/spring.integration.send?tag=name:avroTopicIntegrationConsumer-in-0`  
It seems, that it shows count and processing time since message is consumed from `avroTopicIntegrationConsumer-in-0`
and until processing is finished.

Count and timer for failures:  
`http://localhost:8080/actuator/metrics/spring.integration.send?tag=name:avroTopicIntegrationConsumer-in-0&tag=result:failure`

Count and timer for success attempts:  
`http://localhost:8080/actuator/metrics/spring.integration.send?tag=name:avroTopicIntegrationConsumer-in-0&tag=result:success`

#### Naming "channels"
Metrics for messages passed to filter:  
see
```
  .filter( ..., spec -> spec.id("filter-handler"))
```
`http://localhost:8080/actuator/metrics/spring.integration.send?tag=name:filter-handler`

Metrics for messages passed to a handler:  
see
```
  .handle(
      (GenericHandler<? extends Object>)  (msg, headers) -> {...},
      (GenericEndpointSpec<ServiceActivatingHandler> spec) -> spec.id("message-handler")
   )
```
`http://localhost:8080/actuator/metrics/spring.integration.send?tag=name:message-handler`

# Tutorial 3
use tag integration-tests
```
git checkout integration-tests
```
### Integration tests
- see SpringCloudStreamIntegrationFlow_ChannelBindersTest as an example of TestBinders.
- see SpringCloudStreamIntegrationFlow_EmbeddedKafkaTest as an example of EmbeddedKafka