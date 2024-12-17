package com.mitsel.accounts.controller;

import com.mitsel.accounts.constants.AccountsConstants;
import com.mitsel.accounts.dto.AccountsContactInfoDto;
import com.mitsel.accounts.dto.CustomerDto;
import com.mitsel.accounts.dto.ErrorResponseDto;
import com.mitsel.accounts.dto.ResponseDto;
import com.mitsel.accounts.service.IAccountsService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST Api's for accounts in Banking application",
        description = "CRUD REST Api's for accounts in banking application to CREATE, UPDATE, READ and DELETE account details"
)
@RestController
@RequestMapping(value = "/api/accounts/v1", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class AccountsControllerV1 {

    private static final Logger logger = LoggerFactory.getLogger(AccountsControllerV1.class);

    private final IAccountsService iAccountsService;

    @Autowired
    private Environment environment;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private AccountsContactInfoDto accountsContactInfoDto;

    @Autowired
    public AccountsControllerV1(IAccountsService iAccountsService) {
        this.iAccountsService = iAccountsService;
    }

    @Operation(
            summary = "Create account rest api",
            description = "REST API to create a new customer and account inside our application"
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
                    responseCode = "201",
                    description = "Http Status created"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto){
        iAccountsService.createAccount(customerDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch account details rest api",
            description = "REST API to fetch customer and account details based on mobile number"
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
    @GetMapping("/findByMobile")
    public ResponseEntity<CustomerDto> fetchAccountDetails(
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber){
        CustomerDto customerDto = iAccountsService.fetchAccount(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(
            summary = "Update account details rest api",
            description = "REST API to Update customer and account details based on account number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Http Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/updateAccount")
    public ResponseEntity<ResponseDto> updateAccountDetails(@Valid @RequestBody CustomerDto customerDto){
        boolean isUpdated = iAccountsService.updateAccount(customerDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_UPDATE));
    }

    @Operation(
            summary = "Delete account details rest api",
            description = "REST API to Delete customer and account details based on mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Http Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/deleteByMobile")
    public ResponseEntity<ResponseDto> deleteAccountDetails(
            @RequestParam
            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
            String mobileNumber
    ){
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
        }
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ResponseDto(AccountsConstants.STATUS_417, AccountsConstants.MESSAGE_417_DELETE));
    }

    @Operation(
            summary = "Fetch account details Build information",
            description = "Get Build information that is deployed into the accounts MS"
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
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/buildVersion")
    public ResponseEntity<String> getBuildInfo(){
        logger.debug("getBuildInfo method called");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable){
        logger.debug("getBuildInfoFallback method called");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("0.9");
    }

    @Operation(
            summary = "Java version the current service is using",
            description = "Get Java version information that is used into the accounts MS"
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
    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/javaVersion")
    public ResponseEntity<String> getJavaVersion(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Java 17");
    }

    @Operation(
            summary = "Get Contact Info details",
            description = "Get the contact info and use it when ever there is an issue"
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
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDto> getContactInfo(){
        logger.debug("Invoked accounts contact-info API");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDto);
    }
}
