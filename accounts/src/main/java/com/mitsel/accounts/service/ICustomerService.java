package com.mitsel.accounts.service;

import com.mitsel.accounts.dto.CustomerDetailsDto;

public interface ICustomerService {

    /**
     *
     * @param mobileNumber
     * @return Customer Details Dto(Customer, Accounts, Loans & Cards)
     */
    CustomerDetailsDto fetchCustomerDetails(String mobileNumber);

}
