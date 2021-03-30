package com.ryanair.guide.sqs.initial;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.SQS;
import static org.testcontainers.utility.DockerImageName.parse;

/**
 * @author Ryanair
 */
@Testcontainers
@SpringBootTest
class ApplicationTests {

    private static final String FULL_IMAGE_NAME = "localstack/localstack:0.12.6";

    @Container
    public static LocalStackContainer localStack = new LocalStackContainer(parse(FULL_IMAGE_NAME))
        .withServices(SQS);

    @Autowired
    private QueueMessagingTemplate template;

    /**
     * overriding aws sqs client endpoint
     */
    @DynamicPropertySource
    static void dynamicProperties(final DynamicPropertyRegistry registry) {
        registry
            .add("cloud.aws.sqs.endpoint", () -> localStack
                .getEndpointConfiguration(SQS)
                .getServiceEndpoint());
    }

    @Test
    void writingOnQueue() {
        //given
        final Message<String> message = MessageBuilder
            .withPayload("hello")
            .build();

        //when
        template.send(message);

        //then
        assertThat(template.receiveAndConvert(String.class))
            .isEqualTo(message.getPayload());
    }
}
