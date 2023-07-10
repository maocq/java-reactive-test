package co.com.bancolombia.usecase.registeraccount;

import co.com.bancolombia.model.account.Account;
import co.com.bancolombia.model.account.gateways.AccountRepository;
import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.statusaccount.StatusAccount;
import co.com.bancolombia.model.statusaccount.gateways.StatusAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static co.com.bancolombia.model.exceptions.message.BusinessErrorMessage.ACCOUNT_VALIDATION_ERROR;
import static co.com.bancolombia.model.exceptions.message.TechnicalErrorMessage.TECHNICAL_RESTCLIENT_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class RegisterAccountUseCaseTest {

    @Mock
    StatusAccountService statusAccountService;
    @Mock
    AccountRepository accountRepository;
    RegisterAccountUseCase registerAccountUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        //accountRepository = mock(AccountRepository.class);
        //statusAccountService = mock(StatusAccountService.class);

        registerAccountUseCase = new RegisterAccountUseCase(statusAccountService, accountRepository);

    }

    @Test
    void registerValidAccountTest() {
        var status = StatusAccount.builder().status("ok").build();
        when(accountRepository.findById(anyLong())).thenReturn(Mono.empty());
        when(statusAccountService.getStatus(anyString())).thenReturn(Mono.just(status));

        /*
        StepVerifier.create(registerAccountUseCase.register(99L, "Foo", "1"))
                .assertNext(account -> assertEquals("Foo", account.getName()))
                .verifyComplete();
         */

        registerAccountUseCase.register(99L, "Foo", "1")
                .as(StepVerifier::create)
                .assertNext(account -> {
                    assertEquals("Foo", account.getName(), "xxx yyy zzz");
                    assertEquals("ok", account.getStatus());
                }).verifyComplete();
    }

    @Test
    void registerAccountAlreadyExistTest() {
        var account = Account.builder().id(99L).name("Foo").build();
        //var status = StatusAccount.builder().status("ok").build();
        when(accountRepository.findById(anyLong())).thenReturn(Mono.just(account));

        registerAccountUseCase.register(99L, "Foo", "1")
                .as(StepVerifier::create)
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof BusinessException);
                    BusinessException exception = (BusinessException) throwable;
                    assertEquals(ACCOUNT_VALIDATION_ERROR, exception.getErrorMessage());
                }).verify();
    }

    @Test
    void registerStatusServiceErrorTest() {
        when(accountRepository.findById(anyLong())).thenReturn(Mono.empty());
        when(statusAccountService.getStatus(anyString()))
                .thenReturn(Mono.error(() -> new TechnicalException(TECHNICAL_RESTCLIENT_ERROR)));

        registerAccountUseCase.register(99L, "Foo", "1")
                .as(StepVerifier::create)
                .expectErrorSatisfies(throwable -> {
                    assertTrue(throwable instanceof TechnicalException);
                    TechnicalException exception = (TechnicalException) throwable;
                    assertEquals(TECHNICAL_RESTCLIENT_ERROR, exception.getErrorMessage());
                }).verify();
    }
}