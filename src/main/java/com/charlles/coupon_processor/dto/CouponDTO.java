package com.charlles.coupon_processor.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CouponDTO(
        Long id,
        @NotNull(message = "O code nao pode ser nulo") String code,
        String description,
        @NotNull(message = "O desconto nao pode ser nulo") BigDecimal discountValue,
        @NotNull(message = "A data de validacao nao pode ser nula") Timestamp expirationDate,
        Boolean published) {
    public boolean isPublished() {
        return published != null && published;
    }
}

