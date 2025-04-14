package com.project.shopApp.services;

import com.project.shopApp.controller.ProductController;
import com.project.shopApp.dtos.ProductDTO;
import com.project.shopApp.dtos.ProductImageDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.exceptions.InvalidParamException;
import com.project.shopApp.models.Category;
import com.project.shopApp.models.Product;
import com.project.shopApp.models.ProductImage;
import com.project.shopApp.reposistories.CategoryRepository;
import com.project.shopApp.reposistories.ProductImageRepository;
import com.project.shopApp.reposistories.ProductRepository;
import com.project.shopApp.responses.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.project.shopApp.models.ProductImage.Maximum_image;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategorty = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find Category with id: " + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategorty)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException(
                        "Cannot find prodcut with id: " + productId
                ));
    }

    @Override
    public Page<ProductResponse> getAllProdcts(PageRequest pageRequest) {
        //get the list of product by page and limit
        return productRepository
                .findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO)
            throws Exception{
        Product existingProduct = getProductById(id);
        if(existingProduct != null){
            //Copy the attribute from DTO to Product
            //Using Object Mapper or Model Mapper
            Category existingCategorty = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Cannot find Category with id: " + productDTO.getCategoryId()));

            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setCategory(existingCategorty);
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());

            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> optionalProduct =  productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(
            Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Cannot find Category with id: " + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //Insert more than 5 image for 1 product is forbidden
        int size = productImageRepository.findByProductId(productId).size();
        if(size >= Maximum_image){
            throw new InvalidParamException(
                    "Number of image for 1 product cannnot be higher than: "  + Maximum_image);
        }
        return productImageRepository.save(newProductImage);
    }
}
