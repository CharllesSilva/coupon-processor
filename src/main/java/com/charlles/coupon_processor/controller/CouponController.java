package com.charlles.coupon_processor.controller;

import com.charlles.coupon_processor.dto.CouponDTO;
import com.charlles.coupon_processor.dto.CouponResponseDTO;
import com.charlles.coupon_processor.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;

@RestController
@RequestMapping("/coupon")
@Tag(name = "coupon", description = "manage discount coupons")
public class CouponController {
    private static final Logger log = LoggerFactory.getLogger(CouponController.class);
    private final CouponService service;

    public CouponController(CouponService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "create new coupon", description = "create a discount coupon with code and expiration date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "coupon created"),
            @ApiResponse(responseCode = "400", description = "invalid data")
    })
    public ResponseEntity<CouponResponseDTO> create(@Valid @RequestBody CouponDTO couponDTO) {
        log.info("Request recebida: {}", couponDTO);

        CouponResponseDTO response = service.create(couponDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);

    }

    @GetMapping("/{id}")
    @Operation(summary = "get coupon", description = "find coupon by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "coupon found"),
            @ApiResponse(responseCode = "404", description = "coupon not found")
    })
    public ResponseEntity<CouponResponseDTO> findById(@PathVariable Long id) {
        CouponResponseDTO response = service.findById(id);
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete coupon", description = "soft delete coupon by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "coupon deleted"),
            @ApiResponse(responseCode = "404", description = "coupon not found"),
            @ApiResponse(responseCode = "409", description = "already deleted")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}