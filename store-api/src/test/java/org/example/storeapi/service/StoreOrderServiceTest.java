package org.example.storeapi.service;

import org.example.storeapi.dto.StoreOrderRequest;
import org.example.storeapi.dto.StoreOrderResponse;
import org.example.storeapi.entity.Product;
import org.example.storeapi.entity.StoreOrder;
import org.example.storeapi.exception.InsufficientStockQuantityException;
import org.example.storeapi.repository.ProductRepository;
import org.example.storeapi.repository.StoreOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreOrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreOrderRepository storeOrderRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private StoreOrderService storeOrderService;

    private Product product;
    private StoreOrderRequest storeOrderRequest;
    private StoreOrder storeOrder;

    @BeforeEach
    public void setup() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));
        product.setStockQuantity(10);

        storeOrderRequest = new StoreOrderRequest();
        storeOrderRequest.setProductId(1L);
        storeOrderRequest.setQuantity(5);
        storeOrderRequest.setShippingAddress("123 Test St");

        storeOrder = new StoreOrder();
        storeOrder.setId(1L);
        storeOrder.setCustomerUsername("testuser");
        storeOrder.setProduct(product);
        storeOrder.setQuantity(5);
        storeOrder.setPriceAtPurchase(product.getPrice());
        storeOrder.setShippingAddress("123 Test St");
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreOrder> page = new PageImpl<>(Collections.singletonList(storeOrder));
        when(storeOrderRepository.findAll(pageable)).thenReturn(page);

        Page<StoreOrderResponse> result = storeOrderService.findAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCustomerUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindOrdersByCustomer() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<StoreOrder> page = new PageImpl<>(Collections.singletonList(storeOrder));
        when(storeOrderRepository.findByCustomerUsername("testuser", pageable)).thenReturn(page);

        Page<StoreOrderResponse> result = storeOrderService.findOrdersByCustomer("testuser", pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCustomerUsername()).isEqualTo("testuser");
    }

    @Test
    public void testPlaceStoreOrder_Success() {
        when(productRepository.findByIdAndStockQuantityGreaterThan(anyLong(), anyInt())).thenReturn(Optional.of(product));
        when(storeOrderRepository.save(any(StoreOrder.class))).thenReturn(storeOrder);

        StoreOrderResponse response = storeOrderService.placeStoreOrder("testuser", storeOrderRequest);

        assertThat(response).isNotNull();
        assertThat(response.getCustomerUsername()).isEqualTo("testuser");
        assertThat(response.getProductId()).isEqualTo(product.getId());
        assertThat(response.getQuantity()).isEqualTo(storeOrderRequest.getQuantity());
    }

    @Test
    public void testPlaceStoreOrder_ProductNotFound() {
        when(productRepository.findByIdAndStockQuantityGreaterThan(anyLong(), anyInt())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            storeOrderService.placeStoreOrder("testuser", storeOrderRequest);
        });
    }

    @Test
    public void testPlaceStoreOrder_InsufficientStock() {
        storeOrderRequest.setQuantity(15);
        when(productRepository.findByIdAndStockQuantityGreaterThan(anyLong(), anyInt())).thenReturn(Optional.of(product));
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("Insufficient stock");

        assertThrows(InsufficientStockQuantityException.class, () -> {
            storeOrderService.placeStoreOrder("testuser", storeOrderRequest);
        });
    }
}
