package com.ryanair.guide.sqs.initial;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.support.destination.DynamicQueueUrlDestinationResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Ryanair
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(final AmazonSQSAsync async) {
        final var resolver = new DynamicQueueUrlDestinationResolver(async, null);
        resolver.setAutoCreate(true);

        final var template = new QueueMessagingTemplate(async, resolver, null);
        template.setDefaultDestinationName("sample-queue");

        return template;
    }
}
