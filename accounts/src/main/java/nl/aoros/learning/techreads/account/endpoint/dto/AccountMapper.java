package nl.aoros.learning.techreads.account.endpoint.dto;

import nl.aoros.learning.techreads.account.model.Account;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author adrian oros
 */
@Mapper
public interface AccountMapper {
    AccountDTO toDto(Account source);
    Account fromDto(AccountDTO destination);
    List<AccountDTO> toDtos(List<Account> accounts);
    List<Account> fromDtos(List<AccountDTO> accounts);
}
