package nl.aoros.learning.techreads.book.endpoint.dto.mapper;

import nl.aoros.learning.techreads.book.endpoint.dto.NoteDTO;
import nl.aoros.learning.techreads.book.model.Note;
import org.mapstruct.Mapper;

/**
 * @author adrian oros
 */
@Mapper
public interface NoteMapper {
    NoteDTO toDto(Note source);
    Note fromDto(NoteDTO source);
}
