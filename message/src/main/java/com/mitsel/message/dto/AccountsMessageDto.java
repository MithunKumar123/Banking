package com.mitsel.message.dto;

/**
 *
 * @param accouontNumber
 * @param name
 * @param email
 * @param mobileNumber
 */
public record AccountsMessageDto(Long accouontNumber, String name, String email, String mobileNumber) {
}
