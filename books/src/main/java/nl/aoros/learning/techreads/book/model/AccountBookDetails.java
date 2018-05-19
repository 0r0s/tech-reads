package nl.aoros.learning.techreads.book.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nl.aoros.learning.techreads.book.endpoint.dto.BookDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.ChapterDTO;
import nl.aoros.learning.techreads.book.endpoint.dto.NoteDTO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Document
@Data
@NoArgsConstructor
public class AccountBookDetails {
    private String id;
    private String accountId;
    private Book book;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String review;
    private List<Note> notes;
    private List<String> paretoList;
    private List<Chapter> chapters;
}
