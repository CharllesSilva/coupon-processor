package com.charlles.coupon_processor.entity;

import com.charlles.coupon_processor.dto.CouponStatus;
import com.charlles.coupon_processor.exception.CouponAlreadyDeletedException;
import com.charlles.coupon_processor.exception.InvalidCouponException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void shouldCreateValidCoupon() {
        Coupon coupon = new Coupon(
                null,
                "ABC123",
                "valid coupon",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        assertNotNull(coupon);
        assertEquals("ABC123", coupon.getCode());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isRedeemed());
    }

    @Test
    void shouldNormalizeCodeRemovingSpecialCharacters() {
        Coupon coupon = new Coupon(
                null,
                "AB-C12@3",
                "test",
                new BigDecimal("5.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        assertEquals("ABC123", coupon.getCode());
    }

    @Test
    void shouldThrowExceptionWhenCodeIsInvalid() {
        assertThrows(InvalidCouponException.class, () -> {
            new Coupon(
                    null,
                    "ABC",
                    "invalid code",
                    new BigDecimal("10.00"),
                    Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                    CouponStatus.ACTIVE,
                    true
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsTooLow() {
        assertThrows(InvalidCouponException.class, () -> {
            new Coupon(
                    null,
                    "ABC123",
                    "low discount",
                    new BigDecimal("0.3"),
                    Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                    CouponStatus.ACTIVE,
                    true
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenDiscountValueIsZero() {
        assertThrows(InvalidCouponException.class, () -> {
            new Coupon(
                    null,
                    "ABC123",
                    "zero discount",
                    BigDecimal.ZERO,
                    Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                    CouponStatus.ACTIVE,
                    true
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenExpirationDateIsInPast() {
        assertThrows(InvalidCouponException.class, () -> {
            new Coupon(
                    null,
                    "ABC123",
                    "expired",
                    new BigDecimal("10.00"),
                    Timestamp.valueOf(LocalDateTime.now().minusDays(1)),
                    CouponStatus.ACTIVE,
                    true
            );
        });
    }

    @Test
    void shouldDeleteCoupon() {
        Coupon coupon = new Coupon(
                1L,
                "DEL123",
                "delete test",
                new BigDecimal("15.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        coupon.delete();

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Coupon coupon = new Coupon(
                1L,
                "DUP123",
                "already deleted",
                new BigDecimal("20.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.DELETED,
                true
        );

        assertThrows(CouponAlreadyDeletedException.class, () -> {
            coupon.delete();
        });
    }

    @Test
    void shouldSetRedeemedAsFalseByDefault() {
        Coupon coupon = new Coupon(
                null,
                "TEST12",
                "redeemed test",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        assertFalse(coupon.isRedeemed());
    }

    @Test
    void shouldAcceptMinimumDiscountValue() {
        Coupon coupon = new Coupon(
                null,
                "MIN123",
                "minimum discount",
                new BigDecimal("0.5"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        assertEquals(new BigDecimal("0.5"), coupon.getDiscountValue());
    }

    @Test
    void shouldAcceptCodeWithExactlySixCharacters() {
        Coupon coupon = new Coupon(
                null,
                "A1B2C3",
                "six chars",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        assertEquals("A1B2C3", coupon.getCode());
    }
}