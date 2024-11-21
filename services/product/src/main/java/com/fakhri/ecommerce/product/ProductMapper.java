package com.fakhri.ecommerce.product;

import com.fakhri.ecommerce.category.Category;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {

    public Product toProduct(ProductRequest productRequest){
        return Product.builder().
                id(productRequest.id()).
                name(productRequest.name()).
                description(productRequest.description()).
                price(productRequest.price()).
                availableQuantity(productRequest.availableQuantity()).
                category(Category.builder().id(productRequest.id()).build()).
                build();
    }




    public ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getAvailableQuantity(),
                product.getPrice(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCategory().getDescription()
                );
    }

    public ProductPurchaseResponse toproductPurchaseResponse(Product product,  double quantity) {
        return new ProductPurchaseResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity
        );
    }
}