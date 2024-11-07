package com.alten.shop.service.impl;

import com.alten.shop.dao.Product;
import com.alten.shop.exception.ProductNotFoundException;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id).map(product -> {
            product.setCode(productDetails.getCode());
            product.setName(productDetails.getName());
            product.setDescription(productDetails.getDescription());
            product.setImage(productDetails.getImage());
            product.setCategory(productDetails.getCategory());
            product.setPrice(productDetails.getPrice());
            product.setQuantity(productDetails.getQuantity());
            product.setInternalReference(productDetails.getInternalReference());
            product.setShellId(productDetails.getShellId());
            product.setInventoryStatus(productDetails.getInventoryStatus());
            product.setRating(productDetails.getRating());
            return productRepository.save(product);
        }).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
    }
}