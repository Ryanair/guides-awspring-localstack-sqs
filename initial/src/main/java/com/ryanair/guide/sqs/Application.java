package com.ryanair.guide.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.support.destination.DynamicQueueUrlDestinationResolver;
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
