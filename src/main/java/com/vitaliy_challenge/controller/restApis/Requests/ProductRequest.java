package com.vitaliy_challenge.controller.restApis.Requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class ProductRequest extends PagedRequest
{
    private Boolean withStock;
    private List<String> categoryCodes;
    private List<String> warehouseCodes;
}
