package nl.aoros.learning.techreads.account.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class AccountDTO {
    private String id;
    private String name;
    private boolean enabled;
    private String email;
    private String location;
    private LocalDateTime createDate;
    private LocalDateTime lastPasswordChange;
}
