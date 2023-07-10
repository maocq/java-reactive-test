package co.com.bancolombia.r2dbc.repository.account;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.gateways.AccountRepository;
import co.com.bancolombia.r2dbc.repository.account.data.AccountData;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
@RequiredArgsConstructor
public class AccountDataRepository implements AccountRepository {

    private final AccountDataDAO repository;
    private final ObjectMapper mapper;

    @Override
    public Mono<Account> findById(long id) {
        return repository.findById(id)
                .map(this::toEntity);
    }


    private Account toEntity(AccountData data) {
        return mapper.mapBuilder(data, Account.AccountBuilder.class).build();
    }

    private AccountData toData(Account entity) {
        return mapper.map(entity, AccountData.class);
    }
}
