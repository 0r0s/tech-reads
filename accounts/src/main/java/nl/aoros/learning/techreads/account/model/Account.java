package nl.aoros.learning.techreads.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author adrian oros
 */
@Document
@Data
@NoArgsConstructor
public class Account {
    @Id
    private String id;
    private String name;
    private String email;
    private String location;
    private LocalDateTime createDate;
    private LocalDateTime lastPasswordChange;
}
