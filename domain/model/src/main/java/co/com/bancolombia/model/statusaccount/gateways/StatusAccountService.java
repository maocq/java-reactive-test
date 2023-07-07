package co.com.bancolombia.model.statusaccount.gateways;

import co.com.bancolombia.model.statusaccount.StatusAccount;
import reactor.core.publisher.Mono;

public interface StatusAccountService {

    Mono<StatusAccount> getStatus(String idInfo);
}
