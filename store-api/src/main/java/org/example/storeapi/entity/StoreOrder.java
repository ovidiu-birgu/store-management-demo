package org.example.storeapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "STORE_ORDER")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerUsername;

    private String shippingAddress;

    private Integer quantity;

    private BigDecimal priceAtPurchase;

    private LocalDateTime createdDate;

    // Each order is linked to a single product.
    @OneToOne
    @JoinColumn(name = "product_id", unique = true, nullable = false)
    private Product product;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
