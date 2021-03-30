package com.ryanair.guide.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
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
@SpringBootTest(
    properties = {
        "cloud.aws.region.use-default-aws-region-chain=true",
        "cloud.aws.stack.auto=false",
        "logging.level.com.amazonaws.util.EC2MetadataUtils=error"
    }
)
class ApplicationTests {

    private static final String FULL_IMAGE_NAME = "localstack/localstack:0.12.6";

    @Container
    public static LocalStackContainer localStack = new LocalStackContainer(parse(FULL_IMAGE_NAME))
        .withServices(SQS);

    @Autowired
    private QueueMessagingTemplate template;

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

    @TestConfiguration
    static class AwsClientTestConfig {

        @Bean
        public AmazonSQSBufferedAsyncClient amazonSQS() {
            final AmazonSQSAsync sqsAsync = AmazonSQSAsyncClient
                .asyncBuilder()
                .withCredentials(localStack.getDefaultCredentialsProvider())
                .withEndpointConfiguration(localStack.getEndpointConfiguration(SQS))
                .build();

            return new AmazonSQSBufferedAsyncClient(sqsAsync);
        }
    }
}
