package com.vitaliy_challenge.controller.rest.communication.responses;

import com.vitaliy_challenge.model.dtos.concrete.ProductDtoSlim;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedProductResponse
{
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private List<ProductDtoSlim> results;
}
