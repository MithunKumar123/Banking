package com.mitsel.accounts.service;

import com.mitsel.accounts.dto.CustomerDto;

public interface IAccountsService {


    /**
     *
     *@param customerDto - Object of CustomerDto
     * */
    void createAccount(CustomerDto customerDto);

}
