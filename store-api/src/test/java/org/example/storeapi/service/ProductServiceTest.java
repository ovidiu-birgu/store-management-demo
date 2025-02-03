package org.example.storeapi.service;

import org.example.storeapi.entity.Product;
import org.example.storeapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void testFindAll() {
        Pageable pageable = Pageable.unpaged();
        Product product1 = new Product();
        product1.setName("Test Product 1");
        product1.setStockQuantity(10);
        Product product2 = new Product();
        product2.setName("Test Product 2");
        product2.setStockQuantity(5);
        Page<Product> page = new PageImpl<>(Arrays.asList(product1, product2));

        when(productRepository.findByStockQuantityGreaterThan(0, pageable)).thenReturn(page);

        Page<Product> result = productService.findAll(pageable);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(productRepository, times(1)).findByStockQuantityGreaterThan(0, pageable);
    }

    @Test
    public void testFindProductFound() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("Test Product");
        product.setStockQuantity(10);

        when(productRepository.findByIdAndStockQuantityGreaterThan(id, 0))
                .thenReturn(Optional.of(product));

        Product result = productService.findProduct(id);
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(productRepository, times(1)).findByIdAndStockQuantityGreaterThan(id, 0);
    }

    @Test
    public void testFindProductNotFound() {
        Long id = 1L;
        when(productRepository.findByIdAndStockQuantityGreaterThan(id, 0))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.findProduct(id));
        verify(productRepository, times(1)).findByIdAndStockQuantityGreaterThan(id, 0);
    }

    @Test
    public void testAddProduct() {
        Product product = new Product();
        product.setName("New Product");
        product.setStockQuantity(15);

        when(productRepository.save(product)).thenReturn(product);

        Product result = productService.addProduct(product);
        assertNotNull(result);
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProductFound() {
        Long id = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(id);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(10.0));
        existingProduct.setStockQuantity(5);

        Product updatedProduct = new Product();
        updatedProduct.setName("New Name");
        updatedProduct.setDescription("New Description");
        updatedProduct.setPrice(BigDecimal.valueOf(15.0));
        updatedProduct.setStockQuantity(10);

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(id, updatedProduct);
        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(15.0), result.getPrice());
        assertEquals(10, result.getStockQuantity());

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    public void testUpdateProductNotFound() {
        Long id = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("New Name");

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.updateProduct(id, updatedProduct));
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    public void testDeleteProductFound() {
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setStockQuantity(5);

        when(productRepository.findByIdAndStockQuantityGreaterThan(id, 0))
                .thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        productService.deleteProduct(id);

        // After deletion, the stock quantity should be set to 0
        assertEquals(0, product.getStockQuantity());
        verify(productRepository, times(1)).findByIdAndStockQuantityGreaterThan(id, 0);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testDeleteProductNotFound() {
        Long id = 1L;
        when(productRepository.findByIdAndStockQuantityGreaterThan(id, 0))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> productService.deleteProduct(id));
        verify(productRepository, times(1)).findByIdAndStockQuantityGreaterThan(id, 0);
    }
}
