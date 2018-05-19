package nl.aoros.learning.techreads.book;

import nl.aoros.learning.techreads.book.endpoint.dto.AccountBookDetailsDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.BookDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.ChapterDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.NoteDTO;
import nl.aoros.learning.techreads.book.model.AccountBookDetails;
import nl.aoros.learning.techreads.book.model.Book;
import nl.aoros.learning.techreads.book.model.Chapter;
import nl.aoros.learning.techreads.book.model.Note;
import nl.aoros.learning.techreads.book.repository.AccountBookDetailsRepository;
import nl.aoros.learning.techreads.book.repository.BookRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author adrian oros
 */
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(classes = BooksApplication.class)
public class BookEndpointSpringBootTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AccountBookDetailsRepository detailsRepository;

    @After
    public void cleanup() {
        bookRepository.deleteAll().block();
        detailsRepository.deleteAll().block();
    }

    @Test
    public void shouldCreateBook() {
        String title = "Microservices for Java Developers";
        BookDTO bookDTO = newBookDtoInstance(title, 1);

        this.client.post()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(bookDTO))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk();

        Book createdBook = bookRepository.findByTitle(title).block();
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getAuthor()).isEqualTo(bookDTO.getAuthor());
        assertThat(createdBook.getTitle()).isEqualTo(bookDTO.getTitle());
        assertThat(createdBook.getCategories()).containsOnly(bookDTO.getCategories().toArray(new String[0]));
        assertThat(createdBook.getTags()).containsOnly(bookDTO.getTags().toArray(new String[0]));
    }

    @Test
    public void shouldDeleteBook() {
        Book savedBook = bookRepository.save(newBookInstance("title", 1)).block();

        this.client.delete()
                .uri("/api/{id}", savedBook.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange().expectStatus().isOk();

        assertThat(bookRepository.findById(savedBook.getId()).block()).isNull();
    }

    @Test
    public void shouldUpdateBook() {
        Book savedBook = bookRepository.save(newBookInstance("to udpate", 1)).block();

        BookDTO changedBook = newBookDtoInstance("changed title", 2);
        changedBook.setAuthor("new author");
        changedBook.setTitle("new title");
        changedBook.setTags(Collections.singletonList("new tag"));
        changedBook.setCategories(Collections.singletonList("software"));
        changedBook.setId(savedBook.getId());

        this.client.put()
                .uri("/api")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(changedBook))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();

        Book updatedBook = bookRepository.findById(savedBook.getId()).block();
        assertThat(updatedBook).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo(changedBook.getTitle());
        assertThat(updatedBook.getAuthor()).isEqualTo(changedBook.getAuthor());
        assertThat(updatedBook.getTags()).containsOnly(changedBook.getTags().toArray(new String[0]));
        assertThat(updatedBook.getCategories()).containsOnly(changedBook.getCategories().toArray(new String[0]));
        assertThat(updatedBook.getUpdateDate()).isAfter(savedBook.getUpdateDate());
    }

    @Test
    public void shouldFetchBook() {
        Book savedBook = bookRepository.save(newBookInstance("Portocala", 1)).block();

        this.client.get()
                .uri("/api/{id}", savedBook.getId())
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(savedBook.getId())
                .jsonPath("$.title").isEqualTo(savedBook.getTitle())
                .jsonPath("$.author").isEqualTo(savedBook.getAuthor())
                .jsonPath("$.categories[0]").isEqualTo(savedBook.getCategories().get(0))
                .jsonPath("$.tags[0]").isEqualTo(savedBook.getTags().get(0))
                .jsonPath("$.createDate").isNotEmpty();
    }

    @Test
    public void shouldCreateBookDetailsForAccount() {
        AccountBookDetailsDTO accountBookDetails = accountBookDetailsDTO("Title", 1);

        this.client.put()
                .uri("/api/details")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(accountBookDetails))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();

        Iterable<AccountBookDetails> detailsIterable = detailsRepository.findAllByAccountId(accountBookDetails.getAccountId()).toIterable();
        AccountBookDetails details = detailsIterable.iterator().next();
        checkRelevantDetailsAttributes(accountBookDetails, details);
    }

    @Test
    public void shouldUpdateBookDetailsForAccount() {
        AccountBookDetails accountBookDetails = accountBookDetails("Title", 1);
        accountBookDetails.setCreateDate(LocalDateTime.now());
        String id = detailsRepository.save(accountBookDetails).block().getId();

        AccountBookDetailsDTO changedDto = accountBookDetailsDTO("new title", 2);
        changedDto.setId(id);

        this.client.put()
                .uri("/api/details")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromObject(changedDto))
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk();

        AccountBookDetails actualDetails = detailsRepository.findById(id).block();
        checkRelevantDetailsAttributes(changedDto, actualDetails);
    }

    @Test
    public void shouldGetBookDetailsForAccount() {
        AccountBookDetails firstDetails = accountBookDetails("Title", 1);
        String accountId = firstDetails.getAccountId();
        AccountBookDetails secondDetails = accountBookDetails("Title", 2);
        secondDetails.setAccountId(accountId);
        String firstId = detailsRepository.save(firstDetails).block().getId();
        String secondId = detailsRepository.save(secondDetails).block().getId();

        this.client.get()
                .uri("/api/details/account/" + accountId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("[0].id").isEqualTo(firstId)
                .jsonPath("[0].accountId").isEqualTo(accountId)
                .jsonPath("[0].review").isEqualTo(firstDetails.getReview())
                .jsonPath("[0].createDate").isNotEmpty()
                .jsonPath("[0].updateDate").isNotEmpty()
                .jsonPath("[1].id").isEqualTo(secondId)
                .jsonPath("[1].accountId").isEqualTo(secondDetails.getAccountId())
                .jsonPath("[1].review").isEqualTo(secondDetails.getReview())
                .jsonPath("[1].createDate").isNotEmpty()
                .jsonPath("[1].updateDate").isNotEmpty();
    }

    private void checkRelevantDetailsAttributes(AccountBookDetailsDTO expectedDetails, AccountBookDetails actualDetails) {
        assertThat(actualDetails).isNotNull();
        assertThat(actualDetails.getId()).isNotNull();
        assertThat(actualDetails.getAccountId()).isEqualTo(expectedDetails.getAccountId());
        assertThat(actualDetails.getReview()).isEqualTo(expectedDetails.getReview());
        assertThat(actualDetails.getCreateDate()).isNotNull();
        assertThat(actualDetails.getUpdateDate()).isNotNull();
        assertThat(actualDetails.getReview()).isEqualTo(expectedDetails.getReview());
        assertThat(actualDetails.getChapters().get(0).getName()).isEqualTo(expectedDetails.getChapters().get(0).getName());
        assertThat(actualDetails.getChapters().get(0).getNumber()).isEqualTo(expectedDetails.getChapters().get(0).getNumber());
        assertThat(actualDetails.getChapters().get(0).getNotes().get(0).getContent()).isEqualTo(expectedDetails.getChapters().get(0).getNotes().get(0).getContent());
        assertThat(actualDetails.getChapters().get(0).getNotes().get(0).getTags()).isEqualTo(expectedDetails.getChapters().get(0).getNotes().get(0).getTags());
        assertThat(actualDetails.getBook().getTitle()).isEqualTo(expectedDetails.getBook().getTitle());
        assertThat(actualDetails.getBook().getId()).isEqualTo(expectedDetails.getBook().getId());
        assertThat(actualDetails.getBook().getAuthor()).isEqualTo(expectedDetails.getBook().getAuthor());
        assertThat(actualDetails.getParetoList()).isEqualTo(expectedDetails.getParetoList());
        assertThat(actualDetails.getNotes().get(0).getTags()).isEqualTo(expectedDetails.getNotes().get(0).getTags());
        assertThat(actualDetails.getNotes().get(0).getContent()).isEqualTo(expectedDetails.getNotes().get(0).getContent());
    }

    private AccountBookDetailsDTO accountBookDetailsDTO(String title, int differentiator) {
        AccountBookDetailsDTO accountBookDetails = new AccountBookDetailsDTO();
        accountBookDetails.setAccountId("accId" + differentiator);
        accountBookDetails.setBook(newBookDtoInstance(title, differentiator));
        ChapterDTO chapter = new ChapterDTO();
        chapter.setName("Chapter 1" + differentiator);
        chapter.setNumber(1 + differentiator);
        NoteDTO chapterNote = new NoteDTO();
        chapterNote.setContent("Something interesting" + differentiator);
        chapterNote.setTags(Collections.singletonList("totry" + differentiator));
        chapter.setNotes(Collections.singletonList(chapterNote));
        accountBookDetails.setChapters(Collections.singletonList(chapter));
        accountBookDetails.setParetoList(Collections.singletonList("this is essential" + differentiator));
        accountBookDetails.setReview("interesting book" + differentiator);
        NoteDTO bookNote = new NoteDTO();
        bookNote.setTags(Collections.singletonList("book note" + differentiator));
        bookNote.setContent("note content" + differentiator);
        accountBookDetails.setNotes(Collections.singletonList(bookNote));
        return accountBookDetails;
    }

    private AccountBookDetails accountBookDetails(String title, int differentiator) {
        AccountBookDetails accountBookDetails = new AccountBookDetails();
        accountBookDetails.setAccountId("accId" + differentiator);
        accountBookDetails.setBook(newBookInstance(title, differentiator));
        LocalDateTime now = LocalDateTime.now();
        accountBookDetails.setUpdateDate(now);
        accountBookDetails.setCreateDate(now);
        Chapter chapter = new Chapter();
        chapter.setName("Chapter 1" + differentiator);
        chapter.setNumber(1 + differentiator);
        Note note = new Note();
        note.setContent("Something interesting" + differentiator);
        note.setTags(Collections.singletonList("totry" + differentiator));
        chapter.setNotes(Collections.singletonList(note));
        accountBookDetails.setChapters(Collections.singletonList(chapter));
        accountBookDetails.setParetoList(Collections.singletonList("this is essential" + differentiator));
        accountBookDetails.setReview("interesting book" + differentiator);
        return accountBookDetails;
    }

    private BookDTO newBookDtoInstance(String title, int differentiator) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("Christian Posta" + differentiator);
        bookDTO.setCategories(Collections.singletonList("Software Engineering" + differentiator));
        bookDTO.setTitle(title);
        bookDTO.setCreateDate(LocalDateTime.now());
        bookDTO.setTags(Collections.singletonList("java" + differentiator));
        return bookDTO;
    }

    private Book newBookInstance(String title, int differentiator) {
        Book bookDTO = new Book();
        bookDTO.setAuthor("Christian Posta" + differentiator);
        bookDTO.setCategories(Collections.singletonList("Software Engineering" + differentiator));
        bookDTO.setTitle(title);
        LocalDateTime now = LocalDateTime.now();
        bookDTO.setCreateDate(now);
        bookDTO.setTags(Collections.singletonList("java" + differentiator));
        bookDTO.setUpdateDate(now);
        return bookDTO;
    }
}
