package nl.aoros.learning.techreads.account.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.aoros.learning.techreads.account.AccountsApplication;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountDTO;
import nl.aoros.learning.techreads.account.model.Account;
import nl.aoros.learning.techreads.account.repository.AccountRepository;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author adrian oros
 */
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(classes = AccountsApplication.class)
public class AccountsEndpointSpringBootTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private AccountRepository accountRepository;

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setupTest() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @After
    public void cleanup() {
        accountRepository.deleteAll().block();
    }

    @Test
    public void shouldCreateAccount() throws  Exception {
        AccountDTO account = new AccountDTO();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        this.client.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(account))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk();

        Account accountByName = accountRepository.findByName("jim").block();
        assertThat(accountByName).isNotNull();
    }

    @Test
    public void shouldGetAccount() throws  Exception {
        Account account = new Account();
        account.setName("test");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        Mono<Account> save = accountRepository.save(account);
        Account saved = save.block();

        this.client.get()
                .uri("/api/{id}", saved.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.name").isEqualTo("test");
    }

    @Test
    public void shouldUpdateAccount() throws  Exception {
        Account account = new Account();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        String id = accountRepository.save(account).block().getId();

        account.setLocation("Cluj");
        account.setEnabled(false);
        account.setName("jim2");
        account.setId(id);


        this.client.put()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(account))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();

        Account updatedAccount = accountRepository.findById(id).block();
        assertThat(updatedAccount).isNotNull();
        assertThat(updatedAccount.getLocation()).isEqualTo("Cluj");
        assertThat(updatedAccount.getName()).isEqualTo("jim2");
        assertThat(updatedAccount.isEnabled()).isFalse();
    }

    @Test
    public void shouldDeleteAccount() throws Exception {
        Account account = new Account();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        String id = accountRepository.save(account).block().getId();

        this.client.delete()
                .uri("/api/{id}", id)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk();

        assertThat(accountRepository.findById(id).block()).isNull();
    }

    @Test
    public void shouldReturnAccounts() throws InterruptedException {
        Account account = new Account();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        String firstId = accountRepository.save(account).block().getId();

        Account secondAccount = new Account();
        secondAccount.setName("jim2");
        secondAccount.setLocation("Cluj");
        secondAccount.setEnabled(true);
        secondAccount.setCreateDate(LocalDateTime.now());
        secondAccount.setEmail("test2@test.com");

        Thread.sleep(100l);
        String secondId = accountRepository.save(secondAccount).block().getId();

        this.client.get()
                .uri("/api/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("[0].id").isEqualTo(firstId)
                .jsonPath("[0].name").isEqualTo("jim")
                .jsonPath("[0].location").isEqualTo("Amsterdam")
                .jsonPath("[0].enabled").isEqualTo("true")
                .jsonPath("[0].email").isEqualTo("test@test.com")
                .jsonPath("[0].createDate").isNotEmpty()
                .jsonPath("[1].id").isEqualTo(secondId)
                .jsonPath("[1].name").isEqualTo("jim2")
                .jsonPath("[1].location").isEqualTo("Cluj")
                .jsonPath("[1].enabled").isEqualTo("true")
                .jsonPath("[1].email").isEqualTo("test2@test.com")
                .jsonPath("[1].createDate").isNotEmpty();
    }
}
