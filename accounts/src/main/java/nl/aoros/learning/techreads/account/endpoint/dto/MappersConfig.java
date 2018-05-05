package nl.aoros.learning.techreads.account.endpoint.dto;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author adrian oros
 */
@Configuration
public class MappersConfig {

    @Bean
    AccountMapper accountMapper() {
        return Mappers.getMapper(AccountMapper.class);
    }
}
