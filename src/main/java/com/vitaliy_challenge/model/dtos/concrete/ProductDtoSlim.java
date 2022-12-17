package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProductDtoSlim extends GenericDto
{
    private final String id;
    private final String mpn;
    private final Double streetPriceVat;
    private final Double vatValue;
    private final String categoryCodeString;
}
