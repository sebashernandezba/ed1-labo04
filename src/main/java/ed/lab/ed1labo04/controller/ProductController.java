package ed.lab.ed1labo04.controller;

import ed.lab.ed1labo04.entity.ProductEntity;
import ed.lab.ed1labo04.model.CreateProductRequest;
import ed.lab.ed1labo04.model.UpdateProductRequest;
import ed.lab.ed1labo04.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductEntity> createProduct(@RequestBody CreateProductRequest createProductRequest) {
        try {
            return new ResponseEntity<>(productService.createProduct(createProductRequest), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> updateProduct(@PathVariable long id, @RequestBody UpdateProductRequest updateProductRequest) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, updateProductRequest));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProduct(@PathVariable long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
}
