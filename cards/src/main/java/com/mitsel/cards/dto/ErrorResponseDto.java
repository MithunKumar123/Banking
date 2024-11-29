package com.mitsel.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema to hold the error response"
)
@Data
@AllArgsConstructor
public class ErrorResponseDto {


    @Schema(
            description = "Api path invoked by the client"
    )
    private String apiPath;

    @Schema(
            description = "Error code in the response"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message in the response"
    )
    private String errorMessage;

    @Schema(
            description = "Time respresenting when the error happened"
    )
    private LocalDateTime errorTime;

}
