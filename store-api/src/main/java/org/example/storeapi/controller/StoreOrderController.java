package org.example.storeapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.storeapi.dto.StoreOrderRequest;
import org.example.storeapi.dto.StoreOrderResponse;
import org.example.storeapi.service.StoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
public class StoreOrderController {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int MIN_PAGE_NUMBER = 0;
    public static final String DEFAULT_PAGE_SIZE = "5";
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 10;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_TYPE = "true";
    public static final String ROLE_STORE_CUSTOMER = "ROLE_STORE_CUSTOMER";

    private final StoreOrderService storeOrderService;

    @Autowired
    public StoreOrderController(StoreOrderService storeOrderService) {
        this.storeOrderService = storeOrderService;
    }

    @GetMapping
    public Page<StoreOrderResponse> getAllOrders(Authentication authentication,
                                                 @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) @Min(MIN_PAGE_NUMBER) int page,
                                                 @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(MIN_PAGE_SIZE) @Max(MAX_PAGE_SIZE) int size,
                                                 @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
                                                 @RequestParam(defaultValue = DEFAULT_SORT_TYPE) boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // customers can only see their orders
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(ROLE_STORE_CUSTOMER)))
            return storeOrderService.findOrdersByCustomer(userDetails.getUsername(), pageable);

        // managers or employees can see all orders
        return storeOrderService.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<StoreOrderResponse> placeOrder(Principal principal, @Valid @RequestBody StoreOrderRequest storeOrderRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storeOrderService.placeStoreOrder(principal.getName(), storeOrderRequest));
    }

}
