package co.com.bancolombia.model.account.gateways;

import co.com.bancolombia.model.account.Account;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AccountRepository {

    Mono<Account> findById(long id);
    Mono<Optional<Account>> findByIdOptional(long id);
}
