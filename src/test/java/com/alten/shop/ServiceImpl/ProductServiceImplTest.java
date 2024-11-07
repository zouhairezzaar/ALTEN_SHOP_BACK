package com.alten.shop.ServiceImpl;

import com.alten.shop.dao.Product;
import com.alten.shop.exception.ProductNotFoundException;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setCode("P001");
        product.setName("Watch Product");
        product.setDescription("Product Description");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setInventoryStatus("INSTOCK");
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productServiceImpl.getAllProducts();
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
    }

    @Test
    void testGetProductById_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productServiceImpl.getProductById(1L);
        assertNotNull(result);
        assertTrue(result.isPresent(), "product should be present");
        assertEquals("Watch Product", result.get().getName());
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productServiceImpl.createProduct(product);
        assertNotNull(createdProduct);
        assertEquals("Watch Product", createdProduct.getName());
    }

    @Test
    void testUpdateProduct_ProductExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Product");
        updatedProduct.setCode("P001");
        updatedProduct.setPrice(120.0);

        Product result = productServiceImpl.updateProduct(1L, updatedProduct);
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals(120.0, result.getPrice());
    }

    @Test
    void testUpdateProduct_ProductDoesNotExist() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Product updatedProduct = new Product();
        assertThrows(ProductNotFoundException.class, () -> productServiceImpl.updateProduct(1L, updatedProduct));
    }

    @Test
    void testDeleteProduct_ProductExists() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        assertDoesNotThrow(() -> productServiceImpl.deleteProduct(1L));
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_ProductDoesNotExist() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productServiceImpl.deleteProduct(1L));
    }
}