package com.csv.javavibe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object representing an order event, using Lombok.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    private Long orderId;
    private String productName;
    private int quantity;
    private String customerEmail;
}