package org.example.storeapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.example.storeapi.entity.Product;
import org.example.storeapi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final int MIN_PAGE_NUMBER = 0;
    public static final String DEFAULT_PAGE_SIZE = "20";
    public static final int MIN_PAGE_SIZE = 1;
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "name";
    public static final String DEFAULT_SORT_TYPE = "true";

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) @Min(MIN_PAGE_NUMBER) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) @Min(MIN_PAGE_SIZE) @Max(MAX_PAGE_SIZE) int size,
            @RequestParam(defaultValue = DEFAULT_SORT_BY) String sortBy,
            @RequestParam(defaultValue = DEFAULT_SORT_TYPE) boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return productService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        Product product = productService.findProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product createdProduct = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
