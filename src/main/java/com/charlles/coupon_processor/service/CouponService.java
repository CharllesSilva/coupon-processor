package com.charlles.coupon_processor.service;

import com.charlles.coupon_processor.dto.CouponDTO;
import com.charlles.coupon_processor.dto.CouponResponseDTO;
import com.charlles.coupon_processor.dto.CouponStatus;

import com.charlles.coupon_processor.entity.Coupon;
import com.charlles.coupon_processor.exception.CouponNotFoundException;
import com.charlles.coupon_processor.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponResponseDTO create(CouponDTO couponDTO) {
        Coupon coupon = ToEntity(couponDTO);
        Coupon savedCoupon = couponRepository.save(coupon);

        return toResponse(savedCoupon);
    }

    public CouponResponseDTO findById(Long id) {
        return toResponse(getCoupon(id));
    }

    public void delete(Long id) {
        Coupon coupon = getCoupon(id);
        coupon.delete();
        couponRepository.save(coupon);
}

    private Coupon getCoupon(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException(
                        "coupon not found ssssswith id: " + id
                ));
    }

    public CouponResponseDTO toResponse(Coupon coupon) {
        return new CouponResponseDTO(
                coupon.getId().toString(),
                coupon.getCode(),
                coupon.getDescription(),
                coupon.getDiscountValue(),
                coupon.getExpirationDate(),
                coupon.getStatus(),
                coupon.isPublished(),
                coupon.isRedeemed()
        );
    }

    private Coupon ToEntity(CouponDTO dto) {
        return new Coupon(
                dto.id(),
                dto.code(),
                dto.description(),
                dto.discountValue(),
                dto.expirationDate(),
                CouponStatus.ACTIVE,
                dto.isPublished());
    }
}
