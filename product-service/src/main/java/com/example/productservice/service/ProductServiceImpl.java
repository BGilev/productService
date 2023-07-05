package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductServiceCustomException;
import com.example.productservice.model.ProductRequest;
import com.example.productservice.model.ProductResponse;
import com.example.productservice.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding product...");

        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get product for productId: {}", productId);
        Product product =
                productRepository.findById(productId)
                        .orElseThrow(
                                () -> new ProductServiceCustomException(
                                        "Product with given id not found",
                                        "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse
                = new ProductResponse();

        copyProperties(product, productResponse);

        return productResponse;
    }


    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for id {}", quantity, productId);

        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() -> new ProductServiceCustomException(
                                "Product with given id not found",
                                "PRODUCT_NOT_FOUND"));

        if (product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product doesnt have sufficient quantity",
                    "INSUFFICIENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated successfully");
    }
}

// root
// root
