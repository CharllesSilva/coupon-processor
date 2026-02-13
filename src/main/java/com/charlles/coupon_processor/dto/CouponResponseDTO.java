package com.charlles.coupon_processor.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record CouponResponseDTO(
        String id,
        String code,
        String description,
        BigDecimal discountValue,
        Timestamp expirationDate,
        CouponStatus status,
        boolean published,
        boolean redeemed
) {}