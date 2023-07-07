package co.com.bancolombia.model.statusaccount.gateways;

import co.com.bancolombia.model.statusaccount.StatusAccount;
import reactor.core.publisher.Mono;

public interface StatusAccountRepository {

    Mono<StatusAccount> getStatus(String idInfo);
}
