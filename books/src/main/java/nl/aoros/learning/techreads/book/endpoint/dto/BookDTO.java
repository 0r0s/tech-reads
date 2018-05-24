package nl.aoros.learning.techreads.book.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import nl.aoros.learning.techreads.book.endpoint.dto.AccountBookDetailsDTO.BookDetailsCrud;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "The book id is required", groups = {BookDetailsCrud.class})
    @NotBlank(message = "The id is required", groups = {Update.class})
    private String id;
    @NotNull(message = "The book title is required", groups = {BookDetailsCrud.class})
    @NotNull(message = "The title is required", groups = {Create.class, Update.class})
    private String title;
    @NotBlank(message = "The book author is required", groups = {BookDetailsCrud.class})
    @NotBlank(message = "The author is required", groups = {Create.class, Update.class})
    private String author;
    private List<String> categories;
    private List<String> tags;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public interface Create{}
    public interface Update{}
}
