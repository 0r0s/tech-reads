package nl.aoros.learning.techreads.account.endpoint.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "The account id")
    @NotBlank(message = "The id is required", groups = Update.class)
    private String id;

    @ApiModelProperty(value = "The account name")
    @NotBlank(message = "The name is required", groups = {Update.class, Create.class})
    private String name;

    @ApiModelProperty(value = "Whether the account is enabled or not")
    @NotNull(message = "The enabled flag is required", groups = {Update.class, Create.class})
    private Boolean enabled;

    @ApiModelProperty(value = "The account email address")
    @NotBlank(message = "The email is required", groups = {Update.class, Create.class})
    private String email;

    @ApiModelProperty(value = "The account location")
    @NotBlank(message = "The location is required", groups = {Update.class, Create.class})
    private String location;

    @ApiModelProperty(value = "The account create date")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "The password change date")
    private LocalDateTime lastPasswordChange;

    public interface Create {
    }

    public interface Update {
    }
}
