package org.example.storeapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreOrderResponse {

    private Long id;
    private String customerUsername;
    private String shippingAddress;
    private BigDecimal totalPrice;
    private Long productId;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private LocalDateTime createdDate;

}
