package com.demo.kindle.catalog.domain;

import com.demo.kindle.common.models.PagedResult;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private static final int PRODUCT_PAGE_SIZE = 10;
    private final ProductRepository repo;

    ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public PagedResult<ProductEntity> getProducts(int pageNo) {
        Sort sort = Sort.by("name").ascending();
        int page = pageNo <= 1 ? 0 : pageNo - 1;
        Pageable pageable = PageRequest.of(page, PRODUCT_PAGE_SIZE, sort);
        Page<ProductEntity> productsPage = repo.findAll(pageable);
        return new PagedResult<>(productsPage);
    }

    public Optional<ProductEntity> getByCode(String code) {
        return repo.findByCode(code);
    }
}
