package com.charlles.coupon_processor.service;

import com.charlles.coupon_processor.dto.CouponDTO;
import com.charlles.coupon_processor.dto.CouponResponseDTO;
import com.charlles.coupon_processor.dto.CouponStatus;
import com.charlles.coupon_processor.entity.Coupon;
import com.charlles.coupon_processor.exception.CouponAlreadyDeletedException;
import com.charlles.coupon_processor.exception.CouponNotFoundException;
import com.charlles.coupon_processor.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    void shouldCreateCoupon() {
        CouponDTO dto = new CouponDTO(
                null,
                "ABC123",
                "test coupon",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                true
        );

        Coupon savedCoupon = new Coupon(
                1L,
                "ABC123",
                "test coupon",
                new BigDecimal("10.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

        CouponResponseDTO response = couponService.create(dto);

        assertNotNull(response);
        assertEquals("ABC123", response.code());
        assertEquals(new BigDecimal("10.00"), response.discountValue());
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void shouldFindCouponById() {
        Long id = 1L;
        Coupon coupon = new Coupon(
                id,
                "XYZ789",
                "find test",
                new BigDecimal("20.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        CouponResponseDTO response = couponService.findById(id);

        assertNotNull(response);
        assertEquals("XYZ789", response.code());
        assertEquals(new BigDecimal("20.00"), response.discountValue());
        verify(couponRepository, times(1)).findById(id);
    }

    @Test
    void shouldThrowExceptionWhenCouponNotFound() {
        Long id = 999L;

        when(couponRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> couponService.findById(id));
        verify(couponRepository, times(1)).findById(id);
    }

    @Test
    void shouldDeleteCoupon() {
        Long id = 1L;
        Coupon coupon = new Coupon(
                id,
                "DEL123",
                "delete test",
                new BigDecimal("15.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.ACTIVE,
                true
        );

        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        couponService.delete(id);

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(couponRepository, times(1)).findById(id);
        verify(couponRepository, times(1)).save(coupon);
    }

    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedCoupon() {
        Long id = 1L;
        Coupon coupon = new Coupon(
                id,
                "DUP123",
                "already deleted",
                new BigDecimal("25.00"),
                Timestamp.valueOf(LocalDateTime.now().plusDays(30)),
                CouponStatus.DELETED,
                true
        );

        when(couponRepository.findById(id)).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> couponService.delete(id));
        verify(couponRepository, times(1)).findById(id);
        verify(couponRepository, never()).save(any(Coupon.class));
    }
}