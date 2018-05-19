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
public class AccountBookDetailsDTO {
    private String id;
    private String accountId;
    private BookDTO book;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String review;
    private List<NoteDTO> notes;
    private List<String> paretoList;
    private List<ChapterDTO> chapters;
}
