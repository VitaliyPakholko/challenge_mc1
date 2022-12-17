package com.vitaliy_challenge.controller.services.impls;

import com.vitaliy_challenge.controller.repositories.ProductRepository;
import com.vitaliy_challenge.controller.communication.Requests.PagedProductRequest;
import com.vitaliy_challenge.controller.communication.Responses.PagedProductResponse;
import com.vitaliy_challenge.controller.services.ProductService;
import com.vitaliy_challenge.model.dtos.concrete.ProductDtoFull;
import com.vitaliy_challenge.model.entities.Product;
import com.vitaliy_challenge.model.mappers.impls.ProductMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class ProductServiceImpl implements ProductService
{
    @Inject
    ProductRepository productRepository;

    @Inject
    ProductMapper mapper;

    public ProductDtoFull productById(String id)
    {
        Optional<Product> optionalProduct = productRepository.findByIdOptional(id);
        return mapper.toDto(optionalProduct.orElse(null));
    }

    @Override
    public PagedProductResponse filteredProducts(PagedProductRequest request)
    {
        return productRepository.getPagedProducts(request);
    }

}
