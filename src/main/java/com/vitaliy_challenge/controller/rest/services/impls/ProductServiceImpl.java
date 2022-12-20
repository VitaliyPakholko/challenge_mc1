package com.vitaliy_challenge.controller.rest.services.impls;

import com.vitaliy_challenge.controller.rest.repositories.ProductRepository;
import com.vitaliy_challenge.controller.rest.communication.requests.PagedProductRequest;
import com.vitaliy_challenge.controller.rest.communication.responses.PagedProductResponse;
import com.vitaliy_challenge.controller.rest.services.ProductService;
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
    ProductRepository productRepositoryPanache;

    @Inject
    ProductMapper mapper;

    public ProductDtoFull productById(String id)
    {
        Optional<Product> optionalProduct = productRepositoryPanache.findByIdOptional(id);
        return mapper.toDto(optionalProduct.orElse(null));
    }

    @Override
    public PagedProductResponse filteredProducts(PagedProductRequest request)
    {
        return productRepositoryPanache.getPagedProducts(request);
    }

}
