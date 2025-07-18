package com.devjoliveira.jocommerce.dto;

import java.time.Instant;

import com.devjoliveira.jocommerce.entities.Payment;

public record PaymentDto(
    Long id,
    Instant moment) {

  public PaymentDto(Payment payment) {
    this(
        payment.getId(),
        payment.getMoment());
  }

}
