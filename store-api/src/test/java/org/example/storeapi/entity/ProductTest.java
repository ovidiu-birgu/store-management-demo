package org.example.storeapi.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void testProductCreation() {
        // Arrange
        String name = "Sample Product";
        String description = "This is a sample product.";
        BigDecimal price = new BigDecimal("10.99");
        int stockQuantity = 100;
        LocalDateTime beforeCreation = LocalDateTime.now();

        // Act
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.onCreate();

        // Assert
        assertNull(product.getId(), "Product ID should be null before persistence");
        assertEquals(name, product.getName(), "Product name mismatch");
        assertEquals(description, product.getDescription(), "Product description mismatch");
        assertEquals(price, product.getPrice(), "Product price mismatch");
        assertEquals(stockQuantity, product.getStockQuantity(), "Product stock quantity mismatch");
        assertNotNull(product.getCreatedDate(), "Created date should be set");
        assertNotNull(product.getLastModifiedDate(), "Last modified date should be set");
        assertTrue(product.getCreatedDate().isAfter(beforeCreation) || product.getCreatedDate().isEqual(beforeCreation),
                "Created date should be after or equal to the time before creation");
        assertTrue(product.getLastModifiedDate().isAfter(beforeCreation) || product.getLastModifiedDate().isEqual(beforeCreation),
                "Last modified date should be after or equal to the time before creation");
    }

    @Test
    public void testProductUpdate() {
        // Arrange
        Product product = new Product();
        product.setName("Old Name");
        product.setDescription("Old Description");
        product.setPrice(new BigDecimal("5.99"));
        product.setStockQuantity(50);
        product.onCreate();

        LocalDateTime createdDate = product.getCreatedDate();

        // Act
        product.setName("New Name");
        product.setDescription("New Description");
        product.setPrice(new BigDecimal("15.99"));
        product.setStockQuantity(150);
        product.onUpdate();

        // Assert
        assertEquals("New Name", product.getName(), "Product name should be updated");
        assertEquals("New Description", product.getDescription(), "Product description should be updated");
        assertEquals(new BigDecimal("15.99"), product.getPrice(), "Product price should be updated");
        assertEquals(150, product.getStockQuantity(), "Product stock quantity should be updated");
        assertEquals(createdDate, product.getCreatedDate(), "Created date should remain unchanged");
    }
}
