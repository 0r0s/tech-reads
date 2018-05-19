package nl.aoros.learning.techreads.book.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class BookDTO {
    private String id;
    private String title;
    private String author;
    private List<String> categories;
    private List<String> tags;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
