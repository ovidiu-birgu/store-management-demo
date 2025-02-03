package org.example.storeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StoreOrderRequestTest {

    @Test
    public void testStoreOrderRequest() {
        StoreOrderRequest storeOrderRequest = new StoreOrderRequest();
        assertNotNull(storeOrderRequest);
    }
}
