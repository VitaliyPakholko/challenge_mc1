package com.vitaliy_challenge.model.mappers;

import com.vitaliy_challenge.model.dtos.GenericDto;
import org.mapstruct.Mapper;

import javax.persistence.Entity;

public interface IMapper<Entity, ConcreteDto extends GenericDto>
{
}
