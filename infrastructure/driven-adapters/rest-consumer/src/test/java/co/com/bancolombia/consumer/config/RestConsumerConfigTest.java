package co.com.bancolombia.consumer.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

class RestConsumerConfigTest {

    @Test
    void getWebClientTest() {
        RestConsumerConfig restConsumerConfig = new RestConsumerConfig("url", 5000);
        WebClient webClient = restConsumerConfig.getWebClient(WebClient.builder());
        assertNotNull(webClient);
    }
}