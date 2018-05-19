package nl.aoros.learning.techreads.book.repository;

import nl.aoros.learning.techreads.book.model.AccountBookDetails;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * @author adrian oros
 */
public interface AccountBookDetailsRepository extends ReactiveCrudRepository<AccountBookDetails, String> {
    Flux<AccountBookDetails> findAllByAccountId(String accountId);
}
