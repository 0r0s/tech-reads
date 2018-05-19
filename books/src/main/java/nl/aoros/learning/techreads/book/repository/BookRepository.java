package nl.aoros.learning.techreads.book.repository;

import nl.aoros.learning.techreads.book.model.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author adrian oros
 */
public interface BookRepository extends ReactiveCrudRepository<Book, String> {
    Mono<Book> findByTitle(String title);
}
