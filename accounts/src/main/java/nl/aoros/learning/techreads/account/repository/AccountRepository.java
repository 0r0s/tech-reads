package nl.aoros.learning.techreads.account.repository;

import nl.aoros.learning.techreads.account.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author adrian oros
 */
public interface AccountRepository extends ReactiveCrudRepository<Account, String> {
    Mono<Account> findByName(String name);
    Flux<Account> findAllByOrderByCreateDateAsc();
}
