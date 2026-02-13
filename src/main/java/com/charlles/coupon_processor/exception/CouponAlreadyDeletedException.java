package com.charlles.coupon_processor.exception;

public class CouponAlreadyDeletedException extends RuntimeException {

    public CouponAlreadyDeletedException(String message) {
        super(message);
    }
}
