package com.vitaliy_challenge.controller.services;

import com.vitaliy_challenge.controller.restApis.Requests.PagedProductRequest;
import com.vitaliy_challenge.controller.restApis.Responses.PagedProductResponse;
import com.vitaliy_challenge.model.dtos.concrete.ProductDto;

public interface ProductService
{
    public ProductDto productById(String id);

    public PagedProductResponse filteredProducts(PagedProductRequest request);
}


