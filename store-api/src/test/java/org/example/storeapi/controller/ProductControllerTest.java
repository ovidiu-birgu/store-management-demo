package org.example.storeapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.storeapi.config.TestSecurityConfig;
import org.example.storeapi.entity.Product;
import org.example.storeapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(TestSecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Sample Product");
    }

    @Test
    void testGetAllProducts() throws Exception {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        Mockito.when(productService.findAll(any(Pageable.class))).thenReturn(productPage);

        mockMvc.perform(get("/products")
                        .param("page", "0")
                        .param("size", "20")
                        .param("sortBy", "name")
                        .param("ascending", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(product.getId()))
                .andExpect(jsonPath("$.content[0].name").value(product.getName()));
    }

    @Test
    void testFindProduct() throws Exception {
        Mockito.when(productService.findProduct(1L)).thenReturn(product);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()));
    }

    @Test
    void testAddProduct() throws Exception {
        Product newProduct = new Product();
        newProduct.setName("New Product");
        newProduct.setDescription("New Description");
        newProduct.setPrice(BigDecimal.valueOf(100));
        newProduct.setStockQuantity(50);

        Mockito.when(productService.addProduct(any(Product.class))).thenReturn(newProduct);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newProduct.getName()));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(BigDecimal.valueOf(123));
        updatedProduct.setStockQuantity(12);

        Mockito.when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedProduct.getName()));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
    }
}
