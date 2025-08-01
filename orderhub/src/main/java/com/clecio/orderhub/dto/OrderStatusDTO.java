package com.clecio.orderhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.clecio.orderhub.entity.OrderStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {

    private Long id;
    private OrderStatus status;
    private String statusDescription;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime paidAt;
    private String customerEmail;
}