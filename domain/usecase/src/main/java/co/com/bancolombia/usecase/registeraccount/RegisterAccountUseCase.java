package co.com.bancolombia.usecase.registeraccount;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.gateways.AccountRepository;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.statusaccount.gateways.StatusAccountService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Random;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.ACCOUNT_VALIDATION_ERROR;

@RequiredArgsConstructor
public class RegisterAccountUseCase {

    private final StatusAccountService statusAccountService;
    private final AccountRepository accountRepository;

    public Mono<Account> register(long id, String name, String statusId) {
        return accountRepository.findByIdOptional(id)
                .flatMap(optional -> optional.isPresent() ?
                        Mono.error(() -> new BusinessException(ACCOUNT_VALIDATION_ERROR)) : Mono.just(id))
                .flatMap(idAccount -> statusAccountService.getStatus(statusId))
                .map(status -> Account.newAccount(new Random().nextLong(), name, status.getStatus()));
    }
}
