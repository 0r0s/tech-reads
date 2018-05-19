package nl.aoros.learning.techreads.book.endpoint.dto.mapper;

import nl.aoros.learning.techreads.book.endpoint.dto.ChapterDTO;
import nl.aoros.learning.techreads.book.model.Chapter;
import org.mapstruct.Mapper;

/**
 * @author adrian oros
 */
@Mapper(uses = NoteMapper.class)
public interface ChapterMapper {
    ChapterDTO toDto(Chapter source);
    Chapter fromDto(ChapterDTO source);
}
