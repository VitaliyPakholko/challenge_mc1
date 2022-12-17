package com.vitaliy_challenge.model.dtos.concrete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vitaliy_challenge.model.dtos.GenericDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * A DTO for the {@link com.vitaliy_challenge.model.entities.Category} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends GenericDto implements Serializable
{
    private final String id;
    private final String description;
    @JsonIgnore
    private final Set<ProductDtoFull> products;
}