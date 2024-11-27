package com.mitsel.accounts.service.impl;

import com.mitsel.accounts.constants.AccountsConstants;
import com.mitsel.accounts.dto.CustomerDto;
import com.mitsel.accounts.entity.Accounts;
import com.mitsel.accounts.entity.Customer;
import com.mitsel.accounts.exception.CustomerAlreadyExistException;
import com.mitsel.accounts.mapper.CustomerMapper;
import com.mitsel.accounts.repository.AccountsRepository;
import com.mitsel.accounts.repository.CustomerRepository;
import com.mitsel.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> existingCustomer = customerRepository.findByMobileNumber(customer.getMobileNumber());
        if(existingCustomer.isPresent()){
            throw new CustomerAlreadyExistException("Mobile number is already exisiting");
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonoymys");
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
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
        newAccount.setCreatedBy("Ananymys");
        newAccount.setCreatedAt(LocalDateTime.now());
        return newAccount;
    }

}
