package org.example.storeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductRequestTest {

    @Test
    public void testProductRequestTest() {
        ProductRequest productRequest = new ProductRequest();
        assertNotNull(productRequest);
    }
}
