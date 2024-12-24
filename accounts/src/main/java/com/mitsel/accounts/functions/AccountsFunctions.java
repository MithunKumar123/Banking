package com.mitsel.accounts.functions;

import com.mitsel.accounts.service.IAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {

    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Bean
    public Consumer<Long> updateCommunication(IAccountsService iAccountsService){
        return accountNumber -> {
            log.info("Updating the communication status for the {}", accountNumber);
            iAccountsService.updateCommuincationStatus(accountNumber);
            log.info("Updated the communication status for the {}", accountNumber);
        };
    }

}
