package com.vitaliy_challenge.controller.communication.Requests;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class PagedProductRequest extends PagedRequest
{
    private Boolean withStock;
    private List<String> categoryCodes;
    private String warehouseCode;
}
