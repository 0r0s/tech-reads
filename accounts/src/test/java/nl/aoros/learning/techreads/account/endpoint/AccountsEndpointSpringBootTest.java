package nl.aoros.learning.techreads.account.endpoint;

import nl.aoros.learning.techreads.account.AccountsApplication;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountDTO;
import nl.aoros.learning.techreads.account.model.Account;
import nl.aoros.learning.techreads.account.repository.AccountRepository;
import org.junit.After;
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

    @After
    public void cleanup() {
        accountRepository.deleteAll().block();
    }

    @Test
    public void shouldCreateAccount() throws Exception {
        accountRepository.deleteAll().block();
        AccountDTO account = validAccountDto();

        this.client.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(account))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isCreated();

        Account accountByName = accountRepository.findByName("jim").block();
        assertThat(accountByName).isNotNull();
    }

    @Test
    public void shouldGetValidationErrorWhenFieldsNotSetOnCreate() {
        Object[][] userInvalidData = createUserInvalidData();
        for (Object[] data : userInvalidData) {
            this.client.post()
                    .uri("/api")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(data[0]))
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange().expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.errorCode").isEqualTo("validation.error")
                    .jsonPath("$.message").isEqualTo(data[1]);
        }
    }

    @Test
    public void shouldGetAlreadyExistsErrorWhenCreatingUserWithExistingEmail() {
        Account account = validAccount();
        accountRepository.save(account).block();

        this.client.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(account))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errorCode").isEqualTo("account.alreadyexists");
    }

    @Test
    public void shouldGetAccount() throws Exception {
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
                .expectBody().jsonPath("$.body.name").isEqualTo("test");
    }

    @Test
    public void shouldGetNotFoundErrorWhenRetrievingInvalidUser() {
        this.client.get()
                .uri("/api/{id}", "12345")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.errorCode").isEqualTo("account.notfound");
    }

    @Test
    public void shouldUpdateAccount() throws Exception {
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
    public void shouldGetNotFoundErrorWhenUpdatingInvalidUser() {
        Account account = new Account();
        account.setId("id23456");
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");

        this.client.put()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(account))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.errorCode").isEqualTo("account.notfound");
    }

    @Test
    public void shouldGetValidationErrorWhenFieldsNotSetOnUpdate() {
        Object[][] objects = updateUserInvalidData();
        for (Object[] data : objects) {
            this.client.put()
                    .uri("/api")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .body(BodyInserters.fromObject(data[0]))
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .exchange().expectStatus().isBadRequest()
                    .expectBody()
                    .jsonPath("$.errorCode").isEqualTo("validation.error")
                    .jsonPath("$.message").isEqualTo(data[1]);
        }
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
    public void shouldGetNotFoundErrorWhenDeletingInvalidUser() {
        this.client.delete()
                .uri("/api/{id}", "12345")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.errorCode").isEqualTo("account.notfound");
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

        String secondId = accountRepository.save(secondAccount).block().getId();

        this.client.get()
                .uri("/api/all")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.body[0].id").isEqualTo(firstId)
                .jsonPath("$.body[0].name").isEqualTo("jim")
                .jsonPath("$.body[0].location").isEqualTo("Amsterdam")
                .jsonPath("$.body[0].enabled").isEqualTo("true")
                .jsonPath("$.body[0].email").isEqualTo("test@test.com")
                .jsonPath("$.body[0].createDate").isNotEmpty()
                .jsonPath("$.body[1].id").isEqualTo(secondId)
                .jsonPath("$.body[1].name").isEqualTo("jim2")
                .jsonPath("$.body[1].location").isEqualTo("Cluj")
                .jsonPath("$.body[1].enabled").isEqualTo("true")
                .jsonPath("$.body[1].email").isEqualTo("test2@test.com")
                .jsonPath("$.body[1].createDate").isNotEmpty();
    }

    public static Object[][] createUserInvalidData() {
        AccountDTO first = validAccountDto();
        first.setEmail(null);

        AccountDTO second = validAccountDto();
        second.setEnabled(null);

        AccountDTO third = validAccountDto();
        third.setLocation(null);

        AccountDTO fourth = validAccountDto();
        fourth.setName(null);

        return new Object[][]{
                {first,     "The email is required"},
                {second,    "The enabled flag is required"},
                {third,     "The location is required"},
                {fourth,    "The name is required"},
        };
    }

    public static Object[][] updateUserInvalidData() {
        AccountDTO first = validAccountDto();
        first.setId("id");
        first.setEmail(null);

        AccountDTO second = validAccountDto();
        second.setId("id2");
        second.setEnabled(null);

        AccountDTO third = validAccountDto();
        third.setId("3");
        third.setLocation(null);

        AccountDTO fourth = validAccountDto();
        fourth.setId("4");
        fourth.setName(null);

        AccountDTO fifth = validAccountDto();
        fifth.setId("5");
        fifth.setId(null);

        return new Object[][]{
                {first,     "The email is required"},
                {second,    "The enabled flag is required"},
                {third,     "The location is required"},
                {fourth,    "The name is required"},
                {fifth,    "The id is required"},
        };
    }

    private static AccountDTO validAccountDto() {
        AccountDTO account = new AccountDTO();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        account.setCreateDate(LocalDateTime.now());
        account.setEmail("test@test.com");
        return account;
    }

    private static Account validAccount() {
        Account account = new Account();
        account.setName("jim");
        account.setLocation("Amsterdam");
        account.setEnabled(true);
        LocalDateTime now = LocalDateTime.now();
        account.setCreateDate(now);
        account.setEmail("test@test.com");
        account.setLastPasswordChange(now);
        return account;
    }
}
