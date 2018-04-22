package nl.aoros.learning.techreads.book.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
class Note {
    private String content;
    private LocalDateTime createDate;
    private List<String> tags;
}
