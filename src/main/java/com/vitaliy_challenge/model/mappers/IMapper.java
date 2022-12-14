package com.vitaliy_challenge.model.mappers;

import com.vitaliy_challenge.model.dtos.GenericDto;
import org.mapstruct.Mapper;

import javax.persistence.Entity;

//TODO ensure type safety in entity
public interface IMapper<Entity, ConcreteDto extends GenericDto>
{
}
