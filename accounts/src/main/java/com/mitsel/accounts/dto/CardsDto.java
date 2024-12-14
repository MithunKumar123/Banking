package com.mitsel.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;


@Schema(
        name = "Cards",
        description = "Schema to hold the Card information"
)
@Data
public class CardsDto {

    @NotEmpty(message = "Mobile number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    @Schema(
            description = "Mobile number of the customer",
            example = "9999999999"
    )
    private String mobileNumber;

    @NotEmpty(message = "Card number should not be empty")
    @Pattern(regexp = "(^$|[0-9]{12})", message = "Card number must be 12 digits")
    @Schema(
            description = "Card number of the customer",
            example = "999999999999"
    )
    private String cardNumber;

    @NotEmpty(message = "Card type should not be empty or null")
    @Schema(
            description = "Type of card",
            example = "Credit card"
    )
    private String cardType;

    @Positive(message = "Total card limit should be greater than 0")
    @Schema(
            description = "Total limit on the card",
            example = "100000"
    )
    private int totalLimit;

    @PositiveOrZero(message = "Total amount used should be greater than or equals zero")
    @Schema(
            description = "Total amount used on the card",
            example = "10000"
    )
    private int amountused;

    @PositiveOrZero(message = "Total amount available should be greater than or equals zero")
    @Schema(
            description = "Available amount on the card after spent till now",
            example = "90000"
    )
    private int availableAmount;

}
