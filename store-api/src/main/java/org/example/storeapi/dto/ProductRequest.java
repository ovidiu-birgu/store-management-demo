package org.example.storeapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequest {

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
}
