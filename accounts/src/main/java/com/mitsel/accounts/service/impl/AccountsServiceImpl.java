package com.mitsel.accounts.service.impl;

import com.mitsel.accounts.constants.AccountsConstants;
import com.mitsel.accounts.dto.AccountsDto;
import com.mitsel.accounts.dto.AccountsMessageDto;
import com.mitsel.accounts.dto.CustomerDto;
import com.mitsel.accounts.entity.Accounts;
import com.mitsel.accounts.entity.Customer;
import com.mitsel.accounts.exception.CustomerAlreadyExistException;
import com.mitsel.accounts.exception.ResourceNotFoundException;
import com.mitsel.accounts.mapper.AccountsMapper;
import com.mitsel.accounts.mapper.CustomerMapper;
import com.mitsel.accounts.repository.AccountsRepository;
import com.mitsel.accounts.repository.CustomerRepository;
import com.mitsel.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private final StreamBridge streamBridge;

    private void sendCommunication(Accounts accounts, Customer customer){
        var accountsMessageDto = new AccountsMessageDto(accounts.getAccountNumber()
                , customer.getName()
                , customer.getEmail()
                , customer.getMobileNumber());
        log.info("sending Communication request for the details: {}", accountsMessageDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMessageDto);
        log.info("Is the communication sent successfully: {}", result);
    }

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(existingCustomer.isPresent()){
            throw new CustomerAlreadyExistException("Mobile number is already exisiting");
        }

        Customer savedCustomer = customerRepository.save(customer);

        Accounts accounts = accountsRepository.save(createNewAccount(savedCustomer));
        sendCommunication(accounts, savedCustomer);
    }

    /**
     *
     * @param customer - Customer Object
     * @return the new Account details
     */
    private Accounts createNewAccount(Customer customer){
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccountNumber = 10000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccountNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        return newAccount;
    }

    /**
     *
     * @param mobileNumber - Accepts the mobile number as an input to the query
     * @return Accounts details based on the given mobile number
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts accounts = accountsRepository
                .findByCustomerId(customer.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "CustomerId", String.valueOf(customer.getCustomerId())));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    /**
     * @param customerDto
     * @return boolean indicating if the update of the account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();
        if(accountsDto != null){
            Accounts accounts = accountsRepository
                    .findById(accountsDto.getAccountNumber())
                    .orElseThrow(() -> new ResourceNotFoundException("Account", "Account Number", String.valueOf(accountsDto.getAccountNumber())));

            AccountsMapper.mapToAccounts(accountsDto, accounts);
            accounts = accountsRepository.save(accounts);
            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepository
                    .findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer ID", customerId.toString()));
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param mobileNumber
     * @return Boolean value to notify the that the account is deleted or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    /**
     * @param accountNumber
     * @return
     */
    @Override
    public boolean updateCommuincationStatus(Long accountNumber) {
        boolean isUpdated = false;
        if(accountNumber != null){
            Accounts accounts = accountsRepository
                    .findById(accountNumber)
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Account", "AccountNumber", accountNumber.toString())
                    );
            accounts.setCommunication_switch(true);
            accountsRepository.save(accounts);
            isUpdated = true;
        }
        return isUpdated;
    }
}
