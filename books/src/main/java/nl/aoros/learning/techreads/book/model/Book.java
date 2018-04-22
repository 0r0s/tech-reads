package nl.aoros.learning.techreads.book.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Document
@Data
@NoArgsConstructor
public class Book {
    private String id;
    private String accountId;
    private String title;
    private String author;
    private List<String> categories;
    private List<String> tags;
    private String review;
    private List<Note> notes;
    private List<String> paretoList;
    private List<Chapter> chapters;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
