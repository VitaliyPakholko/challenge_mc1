package com.vitaliy_challenge.model.mappers.impls;

import com.vitaliy_challenge.model.dtos.concrete.DbMigrationDto;
import com.vitaliy_challenge.model.entities.DbMigration;
import com.vitaliy_challenge.model.mappers.IMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "cdi")
public interface DbMigrationMapper extends IMapper<DbMigration, DbMigrationDto>
{
    DbMigration toEntity(DbMigrationDto dbMigrationDto);

    DbMigrationDto toDto(DbMigration dbMigration);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    DbMigration partialUpdate(DbMigrationDto dbMigrationDto, @MappingTarget DbMigration dbMigration);
}
