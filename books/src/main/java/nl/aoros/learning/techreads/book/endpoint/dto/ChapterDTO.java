package nl.aoros.learning.techreads.book.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class ChapterDTO {
    private int number;
    private String name;
    private List<NoteDTO> notes;
    private List<String> paretoList;
}
