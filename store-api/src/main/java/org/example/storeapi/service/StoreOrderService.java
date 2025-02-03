package org.example.storeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storeapi.dto.StoreOrderRequest;
import org.example.storeapi.dto.StoreOrderResponse;
import org.example.storeapi.entity.Product;
import org.example.storeapi.entity.StoreOrder;
import org.example.storeapi.exception.InsufficientStockQuantityException;
import org.example.storeapi.repository.ProductRepository;
import org.example.storeapi.repository.StoreOrderRepository;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreOrderService {

    private final ProductRepository productRepository;
    private final StoreOrderRepository storeOrderRepository;
    private final MessageSource messageSource;

    public Page<StoreOrderResponse> findAll(Pageable pageable) {
        log.debug("StoreOrderService findAll: {}", pageable);
        return storeOrderRepository.findAll(pageable).map(this::convertToDto);
    }

    public Page<StoreOrderResponse> findOrdersByCustomer(String customer, Pageable pageable) {
        log.debug("StoreOrderService findOrdersByCustomer: {} - {}", customer, pageable);
        return storeOrderRepository.findByCustomerUsername(customer, pageable).map(this::convertToDto);
    }

    @Transactional
    public StoreOrderResponse placeStoreOrder(String customer, StoreOrderRequest storeOrderRequest) {
        log.debug("StoreOrderService placeStoreOrder: {} - {}", customer, storeOrderRequest);
        Product orderProduct = productRepository.findByIdAndStockQuantityGreaterThan(storeOrderRequest.getProductId(), 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // update stock quantity of product
        int stockRemaining = orderProduct.getStockQuantity() - storeOrderRequest.getQuantity();

        if(stockRemaining < 0)
            throw new InsufficientStockQuantityException(messageSource.getMessage("product.stock.quantity.insufficient", null, Locale.getDefault()));

        orderProduct.setStockQuantity(stockRemaining);
        productRepository.save(orderProduct);

        StoreOrder savedStoreOrder = convertToEntity(customer, storeOrderRequest, orderProduct);
        storeOrderRepository.save(savedStoreOrder);
        return convertToDto(savedStoreOrder);
    }

    /**
     * Convert StoreOrderRequest DTO to StoreOrder entity
     */
    private StoreOrder convertToEntity(String customer, StoreOrderRequest storeOrderRequest, Product orderProduct) {
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setCustomerUsername(customer);
        storeOrder.setShippingAddress(storeOrderRequest.getShippingAddress());
        storeOrder.setQuantity(storeOrderRequest.getQuantity());
        storeOrder.setPriceAtPurchase(orderProduct.getPrice());
        storeOrder.setProduct(orderProduct);
        return storeOrder;
    }

    /**
     * Convert StoreOrder entity to StoreOrderResponse DTO
     */
    private StoreOrderResponse convertToDto(StoreOrder storeOrder) {
        StoreOrderResponse storeOrderResponse = new StoreOrderResponse();
        storeOrderResponse.setId(storeOrder.getId());
        storeOrderResponse.setCustomerUsername(storeOrder.getCustomerUsername());
        storeOrderResponse.setShippingAddress(storeOrder.getShippingAddress());
        storeOrderResponse.setQuantity(storeOrder.getQuantity());
        storeOrderResponse.setPriceAtPurchase(storeOrder.getPriceAtPurchase());
        storeOrderResponse.setProductId(storeOrder.getProduct().getId());
        storeOrderResponse.setTotalPrice(storeOrder.getPriceAtPurchase().multiply(BigDecimal.valueOf(storeOrder.getQuantity())));
        storeOrderResponse.setCreatedDate(storeOrder.getCreatedDate());
        return storeOrderResponse;
    }
}
