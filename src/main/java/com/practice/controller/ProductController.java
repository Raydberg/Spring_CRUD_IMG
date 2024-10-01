package com.practice.controller;
import com.practice.utils.FileUploadUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import com.practice.model.Product;
import com.practice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public List<Product> viewHomePage() {
        return productService.findPaginated(1, 3, "id", "asc").getContent();
    }

    @GetMapping("/index")
    public List<Product> getProducts(@RequestParam(required = false) String keyword) {
        return productService.getAllProduct(keyword);
    }

    @GetMapping("/showNewProductForm")
    public Product createNewProduct() {
        return new Product();
    }

    @PostMapping("/saveProduct")
    public Product saveProduct(@ModelAttribute("product") Product product,
                               @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setPhotos(fileName);
        Product savedProduct = productService.saveProduct(product);
        String uploadDir = "product-photos/" + savedProduct.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return savedProduct;
    }

    @GetMapping("/showFormForUpdate/{id}")
    public Product updateImage(@PathVariable(value = "id") long id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(value = "id") long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page/{pageNo}")
    public Map<String, Object> findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                             @RequestParam("sortField") String sortField,
                                             @RequestParam("sortDir") String sortDir) {
        int pageSize = 3;
        Page<Product> page = productService.findPaginated(pageNo, pageSize, sortField, sortDir);
        List<Product> listProduct = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("currentPage", pageNo);
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("sortField", sortField);
        response.put("sortDir", sortDir);
        response.put("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        response.put("listProduct", listProduct);

        return response;
    }


}
