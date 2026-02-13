package com.charlles.coupon_processor.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CouponDTO(
        Long id,
        @NotNull(message = "code cannot be null") String code,
        String description,
        @NotNull(message = "discount cannot be null") BigDecimal discountValue,
        @NotNull(message = "expiration date cannot be null") Timestamp expirationDate,
        Boolean published) {
    public boolean isPublished() {
        return published != null && published;
    }
}
