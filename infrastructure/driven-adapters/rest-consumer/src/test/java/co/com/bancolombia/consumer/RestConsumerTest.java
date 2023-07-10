package co.com.bancolombia.consumer;

import co.com.bancolombia.model.exceptions.TechnicalException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.TECHNICAL_RESTCLIENT_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class RestConsumerTest {

    RestConsumer restConsumer;
    MockWebServer mockBackEnd;

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        String url = mockBackEnd.url("url").toString();
        var webClient = WebClient.builder().baseUrl(url).build();

        restConsumer = new RestConsumer(webClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    @DisplayName("Validate getStatus")
    void validateGetStatus() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.OK.value())
                .setBody("{\"status\" : \"ok\"}"));

        var response = restConsumer.getStatus("id");
        StepVerifier.create(response)
                .expectNextMatches(statusAccount -> statusAccount.getStatus().equals("ok"))
                .verifyComplete();
    }

    @Test
    void validateGetStatusError() {
        mockBackEnd.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setBody("{\"code\" : \"error\"}"));

        restConsumer.getStatus("id")
                .as(StepVerifier::create)
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof TechnicalException);
                    TechnicalException exception = (TechnicalException) throwable;
                    assertEquals(TECHNICAL_RESTCLIENT_ERROR, exception.getErrorMessage());
                }).verify();
    }
}