package org.example.storeapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StoreOrderTest {

    @Test
    public void testStoreOrderCreation() {
        // Arrange
        String customerUsername = "john_doe";
        String shippingAddress = "123 Main St, Anytown, USA";
        int quantity = 2;
        BigDecimal priceAtPurchase = new BigDecimal("19.99");
        Product product = new Product();
        product.setName("Sample Product");
        product.setDescription("This is a sample product.");
        product.setPrice(priceAtPurchase);
        product.setStockQuantity(50);

        // Act
        StoreOrder order = new StoreOrder();
        order.setCustomerUsername(customerUsername);
        order.setShippingAddress(shippingAddress);
        order.setQuantity(quantity);
        order.setPriceAtPurchase(priceAtPurchase);
        order.setProduct(product);
        order.onCreate();

        // Assert
        assertNull(order.getId(), "Order ID should be null before persistence");
        assertEquals(customerUsername, order.getCustomerUsername(), "Customer username mismatch");
        assertEquals(shippingAddress, order.getShippingAddress(), "Shipping address mismatch");
        assertEquals(quantity, order.getQuantity(), "Order quantity mismatch");
        assertEquals(priceAtPurchase, order.getPriceAtPurchase(), "Price at purchase mismatch");
        assertEquals(product, order.getProduct(), "Associated product mismatch");
    }
}
