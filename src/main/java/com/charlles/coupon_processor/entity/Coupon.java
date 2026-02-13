package com.charlles.coupon_processor.entity;

import com.charlles.coupon_processor.dto.CouponStatus;
import com.charlles.coupon_processor.exception.CouponAlreadyDeletedException;
import com.charlles.coupon_processor.exception.InvalidCouponException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    @Column(length = 1000)
    private String description;
    private BigDecimal discountValue;
    private Timestamp expirationDate;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private boolean published;
    private boolean redeemed;

    public Long getId() {
        return id;
    }

    protected Coupon() {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRedeemed() {
        return redeemed;
    }

    public void setRedeemed(boolean redeemed) {
        this.redeemed = redeemed;
    }

    public Coupon(Long id, String code, String description, BigDecimal discountValue, Timestamp expirationDate, CouponStatus status, boolean published) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = false;
        validate();
    }

    public void delete() {
        if (this.status == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException("counpon is already deleted");
        }
        this.status = CouponStatus.DELETED;
    }

    private void validate(){
        validateAndNormalizeCode();
        validateDiscountValue();
        validateExpirationDate();
    }

    // Coupon entity
    public void validateAndNormalizeCode() {
        String normalizedCode = this.code.replaceAll("[^a-zA-Z0-9]", "");

        if (normalizedCode.length() != 6) {
            throw new InvalidCouponException(
                    "code must have exactly 6 characters"
            );
        }
        this.code = normalizedCode;
    }

    public void validateExpirationDate() {
        Timestamp now = Timestamp.from(Instant.now());

        if (this.expirationDate.before(now)) {
            throw new InvalidCouponException("expiration date cannot be in the past");
        }
    }

    public void validateDiscountValue() {
        if (this.discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new InvalidCouponException("minimum discount value is 0.5");
        }
        if (this.discountValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCouponException("discount value must be greater than zero");
        }
    }
}
