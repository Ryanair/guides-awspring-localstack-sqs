# Ryanair Java Guides
## Integration Test SQS with localstack

The following sample shows how to migrate from spring cloud aws 2.2.x to 2.3.x when try to perform an SQS integration test with localstack. 

## Requirements
* Java 11
* Maven

---
**NOTE:**

To perform test we need to create a queue, so mark auto create as true on template

```java
@Bean
public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync async) {
    final var resolver = new DynamicQueueUrlDestinationResolver(async, null);
        resolver.setAutoCreate(true);

    final var template = new QueueMessagingTemplate(async, resolver, null);
        template.setDefaultDestinationName("sample-queue");

        return template;
}
``` 
---

