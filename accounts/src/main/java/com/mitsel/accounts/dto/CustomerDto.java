package com.mitsel.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(
        name = "Customer",
        description = "Schema holding Customer and Accounts details"
)
@Data
public class CustomerDto {

    @Schema(
            description = "Name of the Customer",
            example = "John doe"
    )
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 5, max = 30, message = "The name length should be in 5 to 30")
    private String name;

    @Schema(
            description = "Email of the Customer",
            example = "John.doe@gmail.com"
    )
    @NotEmpty(message = "Email address cannot be empty")
    @Email(message = "Email should be valid value")
    private String email;

    @Schema(
            description = "Mobile number of the Customer",
            example = "9999999999"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account details of the customer"
    )
    private AccountsDto accountsDto;

}
