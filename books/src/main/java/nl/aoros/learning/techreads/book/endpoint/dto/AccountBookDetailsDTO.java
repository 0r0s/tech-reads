package nl.aoros.learning.techreads.book.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class AccountBookDetailsDTO {
    private String id;
    @NotBlank(message = "The account id is required", groups = BookDetailsCrud.class)
    private String accountId;

    @Valid
    @NotNull(message = "The book is required", groups = BookDetailsCrud.class)
    private BookDTO book;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String review;
    private List<NoteDTO> notes;
    private List<String> paretoList;
    private List<ChapterDTO> chapters;

    public interface BookDetailsCrud {}
}
