package nl.aoros.learning.techreads.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.aoros.learning.techreads.book.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@SpringBootTest(classes = BooksApplication.class)
public class BooksApplicationTests {

	@Test
	public void contextLoads() {
	}
}
