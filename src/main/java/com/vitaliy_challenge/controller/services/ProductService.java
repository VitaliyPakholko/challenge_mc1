package com.vitaliy_challenge.controller.services;

import com.vitaliy_challenge.controller.restApis.Requests.PagedProductRequest;
import com.vitaliy_challenge.controller.restApis.Responses.PagedProductResponse;
import com.vitaliy_challenge.model.dtos.concrete.ProductDtoFull;

public interface ProductService
{
    public ProductDtoFull productById(String id);

    public PagedProductResponse filteredProducts(PagedProductRequest request);
}


