package nl.aoros.learning.techreads.book.service;

import nl.aoros.learning.techreads.book.model.AccountBookDetails;
import nl.aoros.learning.techreads.book.model.Book;
import nl.aoros.learning.techreads.book.repository.AccountBookDetailsRepository;
import nl.aoros.learning.techreads.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author adrian oros
 */
@Service
public class BookService {

    private BookRepository bookRepository;

    private AccountBookDetailsRepository detailsRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AccountBookDetailsRepository detailsRepository) {
        this.bookRepository = bookRepository;
        this.detailsRepository = detailsRepository;
    }

    public Mono<String> addBook(Book book) {
        return validateBook(book)
                .flatMap(validatedBook -> {
                    LocalDateTime now = LocalDateTime.now();
                    validatedBook.setCreateDate(now);
                    validatedBook.setUpdateDate(now);
                    return bookRepository.save(validatedBook);
                })
                .map(Book::getId);
    }

    public Mono<Void> updateBook(Book book) {
        return validateBook(book)
                .flatMap(validatedBook -> bookRepository.findById(validatedBook.getId()))
                .flatMap(existingBook -> {
                    if (existingBook == null) {
                        throw new RuntimeException("Could not find book with id: " + book.getId());
                    }
                    existingBook.setAuthor(book.getAuthor());
                    existingBook.setTitle(book.getTitle());
                    existingBook.setCategories(book.getCategories());
                    existingBook.setTags(book.getTags());
                    existingBook.setUpdateDate(LocalDateTime.now());
                    return bookRepository.save(existingBook);
                })
                .then();
    }

    public Mono<Void> deleteBook(String id) {
        return bookRepository.deleteById(id);
    }

    public Mono<Book> getBook(String id) {
        return bookRepository.findById(id);
    }

    public Mono<AccountBookDetails> getAccountBookDetails(String id) {
        return detailsRepository.findById(id);
    }

    public Mono<String> addBookDetails(AccountBookDetails accountBookDetails) {
        return validateBookDetails(accountBookDetails)
                .flatMap(validatedDetails -> {
                    String id = validatedDetails.getId();
                    if (id != null) {
                        Optional<AccountBookDetails> optionalDetails = detailsRepository.findById(id).blockOptional();
                        AccountBookDetails existingDetails = optionalDetails.orElseThrow(() -> new RuntimeException("Could not find details with id " + id));
                        validatedDetails.setUpdateDate(LocalDateTime.now());
                        validatedDetails.setCreateDate(existingDetails.getCreateDate());
                        return detailsRepository.save(validatedDetails);
                    } else {
                        LocalDateTime now = LocalDateTime.now();
                        accountBookDetails.setCreateDate(now);
                        accountBookDetails.setUpdateDate(now);
                        return detailsRepository.save(accountBookDetails);
                    }
                })
                .map(AccountBookDetails::getId);
    }

    public Flux<AccountBookDetails> getAllBookDetailsForAccount(String accountId) {
        return detailsRepository.findAllByAccountId(accountId);
    }

    private Mono<Book> validateBook(Book book) {
        return Mono.just(book);
    }

    private Mono<AccountBookDetails> validateBookDetails(AccountBookDetails bookDetails) {
        return Mono.just(bookDetails);
    }
}
