package nl.aoros.learning.techreads.book.endpoint;

import nl.aoros.learning.techreads.book.endpoint.dto.AccountBookDetailsDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.AccountBookDetailsDTO.BookDetailsCrud;
import nl.aoros.learning.techreads.book.endpoint.dto.BookDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.mapper.AccountBookDetailsMapper;
import nl.aoros.learning.techreads.book.endpoint.dto.mapper.BookMapper;
import nl.aoros.learning.techreads.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author adrian oros
 */
@RestController
@RequestMapping("/api")
public class BookEndpoint {
    private BookService bookService;
    private BookMapper mapper;
    private AccountBookDetailsMapper detailsMapper;

    @Autowired
    public BookEndpoint(BookService bookService,
                        BookMapper mapper,
                        AccountBookDetailsMapper detailsMapper) {
        this.bookService = bookService;
        this.mapper = mapper;
        this.detailsMapper = detailsMapper;
    }

    @PostMapping
    public Mono<String> create(@RequestBody @Validated(BookDTO.Create.class) BookDTO book) {
        return Mono.just(book).map(mapper::fromDto).flatMap(bookService::addBook);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable String id) {
        return bookService.deleteBook(id);
    }

    @PutMapping
    public Mono<Void> update(@RequestBody @Validated(BookDTO.Update.class) BookDTO bookDTO) {
        return Mono.just(bookDTO)
                .map(mapper::fromDto)
                .flatMap(bookService::updateBook);
    }

    @GetMapping("/{id}")
    public Mono<BookDTO> get(@PathVariable String id) {
        return bookService.getBook(id).map(mapper::toDto);
    }

    @GetMapping("/details/{id}")
    public Mono<AccountBookDetailsDTO> getAccountBookDetails(@PathVariable String id) {
        return bookService.getAccountBookDetails(id).map(detailsMapper::toDto);
    }

    @PutMapping("/details")
    public Mono<String> addBookDetails(@RequestBody @Validated({BookDetailsCrud.class}) AccountBookDetailsDTO accountBookDetailsDTO) {
        return Mono.just(accountBookDetailsDTO)
                .map(detailsMapper::fromDto)
                .flatMap(details -> bookService.addBookDetails(details));
    }

    @GetMapping("/details/account/{accountId}")
    public Flux<AccountBookDetailsDTO> getAllDetailsForAccount(@PathVariable String accountId) {
        return bookService.getAllBookDetailsForAccount(accountId).map(detailsMapper::toDto);
    }
}
