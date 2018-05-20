package nl.aoros.learning.techreads.account.endpoint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountDTO;
import nl.aoros.learning.techreads.account.endpoint.dto.AccountMapper;
import nl.aoros.learning.techreads.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author adrian oros
 */
@RestController
@RequestMapping("/api")
@Api("Accounts web service endpoint")
@ApiResponses({
        @ApiResponse(code = 500, message = "Internal service exception"),
        @ApiResponse(code = 404, message = "The account does not exist")})
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
    @ApiOperation("Create a new account")
    @ApiResponses({
            @ApiResponse(code = 201, message = "The account was created", response = String.class),
            @ApiResponse(code = 400, message = "The account already exists")})
    public Mono<String> createAccount(@RequestBody @Validated(AccountDTO.Create.class) AccountDTO account) {
        return Mono.just(account).map(mapper::fromDto).flatMap(accountService::addAccount);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Delete an account")
    @ApiResponses(@ApiResponse(code = 200, message = "The account was removed"))
    public Mono<Void> deleteAccount(@PathVariable String id) {
        return accountService.deleteAccount(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Update an account")
    @ApiResponses(@ApiResponse(code = 200, message = "The account was updated"))
    public Mono<Void> updateAccount(@RequestBody @Validated(AccountDTO.Update.class) AccountDTO accountDTO) {
        return Mono.just(accountDTO).map(mapper::fromDto).flatMap(accountService::updateAccount);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Retrieve the account details")
    @ApiResponses(@ApiResponse(code = 200, message = "The account was found and present in the response"))
    public Mono<AccountDTO> getAccount(@PathVariable String id) {
        return accountService.getAccount(id).map(mapper::toDto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Retrieves all the account")
    @ApiResponses(@ApiResponse(code = 200, message = "The list of accounts is present in the response"))
    public Flux<AccountDTO> getAccounts() {
        return accountService.findAllAccounts().map(mapper::toDto);
    }
}
