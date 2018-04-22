package nl.aoros.learning.techreads.book.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
class Chapter {
    private int number;
    private String name;
    private List<Note> notes;
    private List<String> paretoList;
}
