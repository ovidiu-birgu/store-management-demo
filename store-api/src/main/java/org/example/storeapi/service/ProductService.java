package org.example.storeapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Page<Product> findAll(Pageable pageable) {
        log.debug("ProductService findAll: {}", pageable);
        return productRepository.findByStockQuantityGreaterThan(0, pageable);
    }

    public Product findProduct(Long id) {
        log.debug("ProductService findProduct: {}", id);
        return productRepository.findByIdAndStockQuantityGreaterThan(id, 0)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Product addProduct(Product product) {
        log.debug("ProductService addProduct: {}", product);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        log.debug("ProductService updateProduct: {} - {}", id, updatedProduct);
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
        return productRepository.save(existingProduct);
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
}
