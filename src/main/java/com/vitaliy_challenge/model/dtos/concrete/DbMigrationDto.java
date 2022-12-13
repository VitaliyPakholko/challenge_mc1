package com.vitaliy_challenge.model.dtos.concrete;

import com.vitaliy_challenge.model.dtos.GenericDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

/**
 * A DTO for the {@link com.vitaliy_challenge.model.entities.DbMigration} entity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class DbMigrationDto extends GenericDto implements Serializable
{
    private final Integer id;
    private final String mtype;
    private final String mstatus;
    private final String mversion;
    private final String mcomment;
    private final Integer mchecksum;
    private final Instant runOn;
    private final String runBy;
    private final Integer runTime;
}