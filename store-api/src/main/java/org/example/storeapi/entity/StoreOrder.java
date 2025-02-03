package org.example.storeapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "{customer.username.blank}")
    @Size(min = 1, max = 100, message = "{customer.username.size}")
    private String customerUsername;

    @NotBlank(message = "{shipping.address.blank}")
    @Size(min = 1, max = 200, message = "{shipping.address.size}")
    private String shippingAddress;

    @NotNull(message = "{product.stock.blank}")
    @DecimalMin(value = "0", inclusive = false, message = "{product.stock.positive}")
    private Integer quantity;

    @NotNull(message = "{product.price.blank}")
    @DecimalMin(value = "1.0", inclusive = false, message = "{product.price.positive}")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal priceAtPurchase;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
