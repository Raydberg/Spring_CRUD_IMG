package com.practice.service;

import com.practice.model.Product;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ProductService {
    Product saveProduct(Product product);
    Product getProductById(long id);
    void deleteProductById(long id);
    List<Product> getAllProduct(String keyword);
    Page<Product> findPaginated(int pageNo, int pageSize, String sortField, String sortDir);
}
