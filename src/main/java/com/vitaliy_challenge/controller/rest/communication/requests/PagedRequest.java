package com.vitaliy_challenge.controller.rest.communication.requests;

import lombok.Data;

import javax.validation.constraints.Min;

@Data
public abstract class PagedRequest
{
    @Min(1) private Integer pageSize = 20;
    @Min(0) private Integer pageNumber = 0;
}
