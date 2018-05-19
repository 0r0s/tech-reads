package nl.aoros.learning.techreads.book.endpoint.dto.mapper;

import nl.aoros.learning.techreads.book.endpoint.dto.BookDTO;
import nl.aoros.learning.techreads.book.model.Book;
import org.mapstruct.Mapper;

/**
 * @author adrian oros
 */
@Mapper
public interface BookMapper {
    BookDTO toDto(Book source);
    Book fromDto(BookDTO source);
}
