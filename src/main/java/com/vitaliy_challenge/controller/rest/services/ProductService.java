package com.vitaliy_challenge.controller.rest.services;

import com.vitaliy_challenge.controller.rest.communication.requests.PagedProductRequest;
import com.vitaliy_challenge.controller.rest.communication.responses.PagedProductResponse;
import com.vitaliy_challenge.model.dtos.concrete.ProductDtoFull;

public interface ProductService
{
    public ProductDtoFull productById(String id);

    public PagedProductResponse filteredProducts(PagedProductRequest request);
}


