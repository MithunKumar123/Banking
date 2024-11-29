package com.mitsel.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Account",
        description = "Schema holding Accounts details"
)
@Data
public class AccountsDto {

    @Schema(
            description = "Account number of the customer",
            example = "9999999999"
    )
    @NotEmpty(message = "Account number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account type of bank",
            example = "savings"
    )
    @NotEmpty(message = "Account type cannot be empty")
    private String accountType;

    @Schema(
            description = "Bank branch address",
            example = "thottiyam"
    )
    @NotEmpty(message = "Branch address cannot be empty")
    private String branchAddress;

}
