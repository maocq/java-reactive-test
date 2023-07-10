package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.RegisterAccountRequest;
import co.com.bancolombia.api.exceptions.ExceptionHandler;
import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.usecase.registeraccount.RegisterAccountUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.ACCOUNT_VALIDATION_ERROR;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, ExceptionHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private RegisterAccountUseCase registerAccountUseCase;

    @Test
    void testListenGETUseCase() {
        var account = Account.builder().id(99L).name("Foo").build();
        when(registerAccountUseCase.register(anyLong(), anyString(), anyString())).thenReturn(Mono.just(account));

        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .id(1L)
                .name("Foo")
                .statusId("ok").build();

        webTestClient.post()
                .uri("/api/usecase/account")
                .bodyValue(request)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isNotEmpty();
                        }
                );
    }

    @Test
    void testListenGETUseCaseError() {
        when(registerAccountUseCase.register(anyLong(), anyString(), anyString())).thenReturn(Mono.error(() -> new BusinessException(ACCOUNT_VALIDATION_ERROR)));

        RegisterAccountRequest request = RegisterAccountRequest.builder()
                .id(1L)
                .name("Foo")
                .statusId("ok").build();

        webTestClient.post()
                .uri("/api/usecase/account")
                .bodyValue(request)
                .header("Content-Type", "application/json")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isNotEmpty();
                        }
                );
    }

    @Test
    void testListenGETOtherUseCase() {
        webTestClient.get()
                .uri("/api/otherusercase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }
}
