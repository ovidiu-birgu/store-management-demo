package org.example.storeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.storeapi.config.TestSecurityConfig;
import org.example.storeapi.dto.StoreOrderRequest;
import org.example.storeapi.dto.StoreOrderResponse;
import org.example.storeapi.service.StoreOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreOrderController.class)
@Import(TestSecurityConfig.class)
class StoreOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreOrderService storeOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "STORE_CUSTOMER")
    void testGetAllOrdersAsCustomer() throws Exception {
        StoreOrderResponse orderResponse = new StoreOrderResponse();
        Page<StoreOrderResponse> page = new PageImpl<>(Collections.singletonList(orderResponse));
        when(storeOrderService.findOrdersByCustomer(anyString(), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "id")
                        .param("ascending", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "STORE_MANAGER")
    void testGetAllOrdersAsManager() throws Exception {
        StoreOrderResponse orderResponse = new StoreOrderResponse();
        Page<StoreOrderResponse> page = new PageImpl<>(Collections.singletonList(orderResponse));
        when(storeOrderService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "id")
                        .param("ascending", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(username = "customer1", roles = "STORE_CUSTOMER")
    void testPlaceOrder() throws Exception {
        StoreOrderRequest orderRequest = new StoreOrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setShippingAddress("address");
        orderRequest.setQuantity(1);
        StoreOrderResponse orderResponse = new StoreOrderResponse();
        when(storeOrderService.placeStoreOrder(anyString(), any(StoreOrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
