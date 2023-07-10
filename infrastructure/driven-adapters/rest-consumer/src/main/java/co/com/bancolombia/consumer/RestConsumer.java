package co.com.bancolombia.consumer;

import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.statusaccount.StatusAccount;
import co.com.bancolombia.model.statusaccount.gateways.StatusAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.ACCOUNT_FIND_ERROR;
import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.TECHNICAL_RESTCLIENT_ERROR;

@Service
@RequiredArgsConstructor
public class RestConsumer implements StatusAccountService {

    private final WebClient client;

    @Override
    public Mono<StatusAccount> getStatus(String idInfo) {
        return client
                .get()
                .uri("/v3/{id}", idInfo)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new BusinessException(ACCOUNT_FIND_ERROR)))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new TechnicalException(TECHNICAL_RESTCLIENT_ERROR)))
                .bodyToMono(StatusAccountDto.class)
                .map(dto -> StatusAccount.builder().status(dto.getStatus()).build())
                .onErrorMap(WebClientRequestException.class, ex -> new TechnicalException(ex, TECHNICAL_RESTCLIENT_ERROR));
    }
}