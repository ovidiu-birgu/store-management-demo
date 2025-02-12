package org.example.storeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.storeapi.dto.ProductRequest;
import org.example.storeapi.dto.ProductResponse;
import org.example.storeapi.entity.Product;
import org.example.storeapi.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponse> findAll(Pageable pageable) {
        log.debug("ProductService findAll: {}", pageable);
        return productRepository.findByStockQuantityGreaterThan(0, pageable).map(this::convertToDto);
    }

    public ProductResponse findProduct(Long id) {
        log.debug("ProductService findProduct: {}", id);
        Product foundProduct = productRepository.findByIdAndStockQuantityGreaterThan(id, 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToDto(foundProduct);
    }

    public ProductResponse addProduct(ProductRequest product) {
        log.debug("ProductService addProduct: {}", product);
        Product savedProduct = productRepository.save(convertToEntity(product));
        return convertToDto(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        log.debug("ProductService updateProduct: {} - {}", id, productRequest);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.debug("ProductService deleteProduct: {}", id);
        Product existingProduct = productRepository.findByIdAndStockQuantityGreaterThan(id, 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // deleting a product sets the stock quantity to 0
        existingProduct.setStockQuantity(0);
        productRepository.save(existingProduct);
    }

    /**
     * Convert ProductRequest DTO to Product entity
     */
    private Product convertToEntity(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setStockQuantity(productRequest.getStockQuantity());
        return product;
    }

    /**
     * Convert Product entity to ProductResponse DTO
     */
    private ProductResponse convertToDto(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setStockQuantity(product.getStockQuantity());
        productResponse.setCreatedDate(product.getCreatedDate());
        productResponse.setLastModifiedDate(product.getLastModifiedDate());
        return productResponse;
    }
}
