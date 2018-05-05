package nl.aoros.learning.techreads.account.endpoint;

import nl.aoros.learning.techreads.account.endpoint.dto.AccountDTO;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountMapper;
import nl.aoros.learning.techreads.account.model.Account;
import nl.aoros.learning.techreads.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author adrian oros
 */
@RestController
@RequestMapping(value = "/api")
public class AccountsEndpoint {

    private AccountService accountService;

    private AccountMapper mapper;

    @Autowired
    public AccountsEndpoint(AccountService accountService, AccountMapper mapper) {
        this.accountService = accountService;
        this.mapper = mapper;
    }

    @PostMapping
    public Mono<String> createAccount(@RequestBody AccountDTO account) {
        return Mono.just(account).map(mapper::fromDto).flatMap(acc -> accountService.addAccount(acc));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping
    public Mono<Void> updateAccount(@RequestBody AccountDTO accountDTO) {
        return Mono.just(accountDTO).map(mapper::fromDto).flatMap(account -> accountService.updateAccount(account));
    }

    @GetMapping("/{id}")
    public Mono<AccountDTO> getAccount(@PathVariable String id) {
        return accountService.getAccount(id).map(mapper::toDto);
    }

    @GetMapping("/all")
    public Flux<AccountDTO> getAccounts() {
        return accountService.findAllAccounts().map(acc -> mapper.toDto(acc));
    }
}
