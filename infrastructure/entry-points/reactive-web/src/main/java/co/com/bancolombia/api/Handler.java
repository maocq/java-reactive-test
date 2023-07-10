package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.RegisterAccountRequest;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.usecase.registeraccount.RegisterAccountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.INVALID_REQUEST;

@Component
@RequiredArgsConstructor
public class Handler {

    private final RegisterAccountUseCase registerAccountUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterAccountRequest.class)
                .switchIfEmpty(Mono.error(() -> new BusinessException(INVALID_REQUEST)))
                .flatMap(request ->
                        registerAccountUseCase.register(request.getId(), request.getName(), request.getStatusId()))
                .flatMap(account -> ServerResponse.ok().bodyValue(account));
    }

    public Mono<ServerResponse> listenGETOtherUseCase(ServerRequest serverRequest) {
        // useCase2.logic();
        return ServerResponse.ok().bodyValue("");
    }
}
