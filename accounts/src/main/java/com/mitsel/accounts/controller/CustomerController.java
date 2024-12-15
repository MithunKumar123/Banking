package com.mitsel.accounts.controller;

import com.mitsel.accounts.dto.CustomerDetailsDto;
import com.mitsel.accounts.dto.ErrorResponseDto;
import com.mitsel.accounts.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(
        name = "CRUD REST Api's for Customers in Banking application",
        description = "CRUD REST Api's for accounts in banking application to READ customer details"
)
@RequestMapping(value = "/api/customer/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);


    private ICustomerService iCustomerService;

    public CustomerController(ICustomerService iCustomerService) {
        this.iCustomerService = iCustomerService;
    }

    @Operation(
            summary = "Fetch customer details rest api",
            description = "REST API to fetch customer, account, loans & cards details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "500",
                    description = "Http Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK"
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@RequestParam
                                                                   @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                   String mobileNumber,
                                                                   @RequestHeader("banking-correlation-id") String correleationId){
        logger.debug("banking-correlation-id found: {}", correleationId);
        CustomerDetailsDto customerDetailsDto = iCustomerService.fetchCustomerDetails(mobileNumber, correleationId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDetailsDto);
    }

}
