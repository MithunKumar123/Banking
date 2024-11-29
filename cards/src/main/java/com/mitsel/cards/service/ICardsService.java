package com.mitsel.cards.service;

import com.mitsel.cards.dto.CardsDto;

public interface ICardsService {


    /**
     *
     * @param mobileNumber
     */
    void createCard(String mobileNumber);

    /**
     *
     * @param mobileNumber
     * @return Cards details in a DTO fashion by using the mobile number
     */
    CardsDto fetchCard(String mobileNumber);

    /**
     *
     * @param cardsDto
     * @return informing the user if the card details have been updated or not
     */
    boolean updateCard(CardsDto cardsDto);

    /**
     *
     * @param mobileNumber
     * @return informing the user if the card details have been deleted or not
     */
    boolean deleteCard(String mobileNumber);

}
