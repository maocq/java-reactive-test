package co.com.bancolombia.r2dbc.repository.account;

import co.com.bancolombia.r2dbc.repository.account.data.AccountData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapperImp;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AccountDataRepositoryTest {

    @Mock
    AccountDataDAO repository;

    AccountDataRepository accountDataRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountDataRepository = new AccountDataRepository(repository, new ObjectMapperImp());
    }

    @Test
    void findByIdTest() {
        var data = new AccountData(0L, "name", "status");
        when(repository.findById(anyLong()))
                .thenReturn(Mono.just(data));

        accountDataRepository.findById(1l)
                .as(StepVerifier::create)
                .assertNext(accountData ->  {
                    Assertions.assertThat(accountData.getName()).isEqualTo("name");
                    assertEquals("status", accountData.getStatus());
                }).verifyComplete();
    }
}