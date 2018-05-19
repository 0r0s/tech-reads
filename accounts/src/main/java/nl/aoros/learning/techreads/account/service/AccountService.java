package nl.aoros.learning.techreads.account.service;

import nl.aoros.learning.techreads.account.model.Account;
import nl.aoros.learning.techreads.account.repository.AccountRepository;
import nl.aoros.learning.techreads.account.service.exception.AlreadyExistsException;
import nl.aoros.learning.techreads.account.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * @author adrian oros
 */
@Service
public class AccountService {

    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Mono<String> addAccount(Account account) {
        return validateAccountFields(account)
                .flatMap(acc -> {
                    accountRepository.findByName(account.getName())
                            .blockOptional()
                            .orElseThrow(() -> {
                                String message = String.format("Account with name %s already exists", account.getName());
                                return new AlreadyExistsException(message);
                            });

                    acc.setCreateDate(LocalDateTime.now());
                    acc.setEnabled(true);
                    return accountRepository.save(acc);
                }).map(Account::getId);
    }

    public Mono<Void> updateAccount(Account account) {
        return validateAccountFields(account)
                .flatMap(acc -> accountRepository.findById(account.getId()))
                .flatMap(existingAccount -> {
                    if (existingAccount == null) {
                        throw new NotFoundException("Could not find account with id: " + account.getId());
                    }
                    existingAccount.setEnabled(account.isEnabled());
                    existingAccount.setLocation(account.getLocation());
                    existingAccount.setName(account.getName());
                    return accountRepository.save(existingAccount);
                }).then();
    }

    public Flux<Account> findAllAccounts() {
        return accountRepository.findAllByOrderByCreateDateAsc();
    }

    public Mono<Account> getAccount(String id) {
        return accountRepository.findById(id);
    }

    public Mono<Void> deleteAccount(String id) {
        return accountRepository.deleteById(id);
    }

    private Mono<Account> validateAccountFields(Account account) {
        // nothing to validate for now
        return Mono.just(account);
    }
}
