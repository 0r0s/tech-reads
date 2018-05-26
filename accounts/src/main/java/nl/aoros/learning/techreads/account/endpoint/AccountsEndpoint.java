package nl.aoros.learning.techreads.account.endpoint;

import nl.aoros.learning.techreads.account.endpoint.dto.AccountDTO;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountMapper;
import nl.aoros.learning.techreads.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author adrian oros
 */
@RestController
@RequestMapping("/api")
public class AccountsEndpoint {

    private AccountService accountService;

    private AccountMapper mapper;

    @Autowired
    public AccountsEndpoint(AccountService accountService, AccountMapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RestApiResponse<String>> createAccount(@RequestBody @Validated(AccountDTO.Create.class) AccountDTO account) {
        return Mono.just(account).map(mapper::fromDto)
                .flatMap(accountService::addAccount)
                .map(RestApiResponse::ok);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> updateAccount(@RequestBody @Validated(AccountDTO.Update.class) AccountDTO accountDTO) {
        return Mono.just(accountDTO).map(mapper::fromDto).flatMap(accountService::updateAccount);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RestApiResponse<AccountDTO>> getAccount(@PathVariable String id) {
        return accountService.getAccount(id).map(mapper::toDto).map(RestApiResponse::ok);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RestApiResponse<List<AccountDTO>>> getAccounts() {
        return accountService.findAllAccounts()
                .map(mapper::toDto)
                .collect(Collectors.toList())
                .map(RestApiResponse::ok);
    }
}
