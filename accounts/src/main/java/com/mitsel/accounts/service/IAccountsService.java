package com.mitsel.accounts.service;

import com.mitsel.accounts.dto.CustomerDto;

public interface IAccountsService {


    /**
     *
     *@param customerDto - Object of CustomerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber - Accepts the mobile number as an input to the query
     * @return Account details based on the mobile number
     */
    CustomerDto fetchAccount(String mobileNumber);

    /**
     * @param customerDto
     * @return boolean indicating if the update of the account details is successful or not
     */
    boolean updateAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber
     * @return Boolean value to notify the that the account is deleted or not
     */
    boolean deleteAccount(String mobileNumber);

}
