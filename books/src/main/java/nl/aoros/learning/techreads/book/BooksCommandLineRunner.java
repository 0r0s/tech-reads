package nl.aoros.learning.techreads.book;

import nl.aoros.learning.techreads.book.model.AccountBookDetails;
import nl.aoros.learning.techreads.book.model.Book;
import nl.aoros.learning.techreads.book.repository.AccountBookDetailsRepository;
import nl.aoros.learning.techreads.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

/**
 * @author adrian oros
 */
@Profile("local")
@Component
public class BooksCommandLineRunner implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final AccountBookDetailsRepository detailsRepository;

    @Autowired
    BooksCommandLineRunner(BookRepository bookRepository, AccountBookDetailsRepository detailsRepository) {
        this.bookRepository = bookRepository;
        this.detailsRepository = detailsRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Stream.of(
                new String[]{"Sapiens", "Yuval Hoah Harari", "1", "nice read"},
                new String[]{"Meditations", "Marcus Aurelius", "1", "excellent read"}
        ).forEach(detail -> {
                    Book s = new Book();
                    s.setTitle(detail[0]);
                    s.setAuthor(detail[1]);
                    Book book = bookRepository.save(s).block();
                    AccountBookDetails bookDetails = new AccountBookDetails();
                    bookDetails.setBook(book);
                    bookDetails.setAccountId(detail[2]);
                    bookDetails.setReview(detail[3]);
                    detailsRepository.save(bookDetails).block();
                }
        );
    }
}
