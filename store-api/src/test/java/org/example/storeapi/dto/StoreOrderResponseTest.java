package org.example.storeapi.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class StoreOrderResponseTest {

    @Test
    public void testStoreOrderResponse() {
        StoreOrderResponse storeOrderResponse = new StoreOrderResponse();
        assertNotNull(storeOrderResponse);
    }
}
