package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.CategoryDto;
import com.vitaliy_challenge.model.entities.Category;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.*;

@Mapper(componentModel = "cdi",
        uses = {ProductMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CategoryMapper extends IMapper<Category, CategoryDto>
{
    Category toEntity(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Category partialUpdate(CategoryDto categoryDto, @MappingTarget Category category);

    CategoryDto toDto(Category category);

    @AfterMapping
    default void linkProducts(@MappingTarget Category category)
    {
        category.getProducts().forEach(product -> product.setCategoryCode(category));
    }
}
