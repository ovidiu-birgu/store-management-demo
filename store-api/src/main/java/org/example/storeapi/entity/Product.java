package org.example.storeapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "STORE_PRODUCT")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "{product.name.blank}")
    @Size(min = 1, max = 100, message = "{product.name.size}")
    private String name;

    @NotBlank(message = "{product.description.blank}")
    @Size(min =1, max = 500, message = "{product.description.size}")
    private String description;

    @NotNull(message = "{product.price.blank}")
    @DecimalMin(value = "1.0", inclusive = false, message = "{product.price.positive}")
    private BigDecimal price;

    @NotNull(message = "{product.stock.blank}")
    @DecimalMin(value = "0", inclusive = true, message = "{product.stock.positive}")
    private Integer stockQuantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastModifiedDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        lastModifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastModifiedDate = LocalDateTime.now();
    }
}
