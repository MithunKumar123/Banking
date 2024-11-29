package com.mitsel.cards.service.impl;

import com.mitsel.cards.constants.CardsConstants;
import com.mitsel.cards.dto.CardsDto;
import com.mitsel.cards.entity.Cards;
import com.mitsel.cards.exception.CardAlreadyExistException;
import com.mitsel.cards.exception.ResourceNotFoundException;
import com.mitsel.cards.mapper.CardsMapper;
import com.mitsel.cards.repository.CardsRepository;
import com.mitsel.cards.service.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;

    /**
     * @param mobileNumber
     */
    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistException("Card already registered with this mobile number: " + mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    private Cards createNewCard(String mobileNumber) {
        Cards creditCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        creditCard.setCardNumber(Long.toString(randomCardNumber));
        creditCard.setMobileNumber(mobileNumber);
        creditCard.setCardType(CardsConstants.CREDIT_CARD);
        creditCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        creditCard.setAmountUsed(0);
        creditCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return creditCard;
    }

    /**
     * @param mobileNumber
     * @return Cards details in a DTO fashion by using the mobile number
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Cards", "mobile number", mobileNumber));

        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    /**
     * @param cardsDto
     * @return informing the user if the card details have been updated or not
     */
    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository
                .findByCardNumber(cardsDto.getCardNumber())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Cards", "Card Number", cardsDto.getCardNumber())
                );

        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return true;
    }

    /**
     * @param mobileNumber
     * @return informing the user if the card details have been deleted or not
     */
    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository
                .findByMobileNumber(mobileNumber)
                .orElseThrow(
                    () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
                );
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }
}
