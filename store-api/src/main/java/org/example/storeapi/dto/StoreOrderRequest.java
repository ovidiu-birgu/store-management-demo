package org.example.storeapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreOrderRequest {

    @NotNull(message = "{order.item.invalid}")
    private Long productId;

    @NotBlank(message = "{shipping.address.blank}")
    @Size(min = 1, max = 200, message = "{shipping.address.size}")
    private String shippingAddress;

    @NotNull(message = "{product.stock.blank}")
    @DecimalMin(value = "0", inclusive = false, message = "{product.stock.positive}")
    private Integer quantity;

}
