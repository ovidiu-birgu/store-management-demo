package org.example.storeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductResponseTest {

    @Test
    public void testProductResponseTest() {
        ProductResponse productRequest = new ProductResponse();
        assertNotNull(productRequest);
    }
}
