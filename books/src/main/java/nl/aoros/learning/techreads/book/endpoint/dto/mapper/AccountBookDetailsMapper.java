package nl.aoros.learning.techreads.book.endpoint.dto.mapper;

import nl.aoros.learning.techreads.book.endpoint.dto.AccountBookDetailsDTO;
import nl.aoros.learning.techreads.book.model.AccountBookDetails;
import org.mapstruct.Mapper;

/**
 * @author adrian oros
 */
@Mapper(uses = {NoteMapper.class, BookMapper.class, ChapterMapper.class})
public interface AccountBookDetailsMapper {
    AccountBookDetailsDTO toDto(AccountBookDetails source);
    AccountBookDetails fromDto(AccountBookDetailsDTO source);
}
