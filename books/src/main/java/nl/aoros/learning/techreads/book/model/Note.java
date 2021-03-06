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
public class Note {
    private String content;
    private List<String> tags;
}
