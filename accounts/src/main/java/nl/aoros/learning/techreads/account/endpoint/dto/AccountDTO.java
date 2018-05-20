package nl.aoros.learning.techreads.account.endpoint.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author adrian oros
 */
@Data
@NoArgsConstructor
public class AccountDTO {
    @NotBlank(message = "The id is required", groups = Update.class)
    private String id;

    @NotBlank(message = "The name is required", groups = {Update.class, Create.class})
    private String name;

    @NotNull(message = "The enabled flag is required", groups = {Update.class, Create.class})
    private Boolean enabled;

    @NotBlank(message = "The email is required", groups = {Update.class, Create.class})
    private String email;

    @NotBlank(message = "The location is required", groups = {Update.class, Create.class})
    private String location;

    private LocalDateTime createDate;

    private LocalDateTime lastPasswordChange;

    public interface Create {
    }

    public interface Update {
    }
}
