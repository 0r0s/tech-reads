package nl.aoros.learning.techreads.book.endpoint.dto.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author adrian oros
 */
@Configuration
public class MappersConfig {

    @Bean
    public BookMapper bookMapper() {
        return Mappers.getMapper(BookMapper.class);
    }

    @Bean
    public NoteMapper noteMapper() {
        return Mappers.getMapper(NoteMapper.class);
    }

    @Bean
    public ChapterMapper chapterMapper() {
        return Mappers.getMapper(ChapterMapper.class);
    }

    @Bean
    public AccountBookDetailsMapper detailsMapper() { return Mappers.getMapper(AccountBookDetailsMapper.class); }

}



