package com.project.shopApp.controller;


import com.github.javafaker.Faker;
import com.project.shopApp.dtos.ProductDTO;
import com.project.shopApp.dtos.ProductImageDTO;
import com.project.shopApp.exceptions.DataNotFoundException;
import com.project.shopApp.models.Product;
import com.project.shopApp.models.ProductImage;
import com.project.shopApp.reposistories.ProductRepository;
import com.project.shopApp.responses.ProductListResponse;
import com.project.shopApp.responses.ProductResponse;
import com.project.shopApp.services.IProductService;
import com.project.shopApp.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.project.shopApp.models.ProductImage.Maximum_image;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor

public class ProductController {
    private final IProductService productService;
    private final ProductRepository productRepository;

    @GetMapping("")    // http://localhost:8088/${api.prefix}/products
    public ResponseEntity<ProductListResponse> getProduct(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page, limit,
                Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProdcts(pageRequest);
        //Take the total page
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                                .products(products)
                                .totalPages(totalPages)
                                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable("id") Long productId
    ){
        try{
            Product existingProduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("id") Long productId
    ){
        try{
            productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format(
                    "Product with id = % d deleted successfully", productId));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO,
                                           BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> erorrMessage = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(erorrMessage);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok().body(newProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @RequestParam("files") List<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            if(files.size() > Maximum_image){
                return ResponseEntity.badRequest().body("Maximum uploaded image is 5");
            }
            files = files == null ? new ArrayList<MultipartFile>() : files;
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (file.getSize() == 0)
                    continue;
                //Check file size and format
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body("File is too large, please try again with file < 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("File must be an image");
                }
                String filename = storeFile(file);
                //Saving object to database
                //Saving to product_image table
                ProductImage productImage = productService.createProductImage(existingProduct.getId()
                            ,ProductImageDTO.builder()
                                .productId(productId)
                                .imageUrl(filename)
                                .build());
                productImages.add(productImage);
                }
            return ResponseEntity.ok().body(productImages);
            } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image file format");
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Add UUID to make sure the file is unique
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        //Path to saving file
        java.nio.file.Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //Complete path to file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        //Copy file to destination
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    //@PostMapping("/generatedFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i < 5000; i++){
            String productName = faker.commerce().productName();
            if(productService.existByName(productName))
                productName.concat(" " + String.valueOf(ThreadLocalRandom.current().nextInt(1, 11)));
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 15000000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number().numberBetween(1,6))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (DataNotFoundException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake Products created successfully");
    }
}
