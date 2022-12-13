package com.vitaliy_challenge.model.entities;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "DB_MIGRATION")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class DbMigration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @NonNull
    @Column(name = "MTYPE", nullable = false, length = 1)
    private String mtype;

    @NonNull
    @Column(name = "MSTATUS", nullable = false, length = 10)
    private String mstatus;

    @NonNull
    @Column(name = "MVERSION", nullable = false, length = 150)
    private String mversion;

    @NonNull
    @Column(name = "MCOMMENT", nullable = false, length = 150)
    private String mcomment;

    @NonNull
    @Column(name = "MCHECKSUM", nullable = false)
    private Integer mchecksum;

    @NonNull
    @Column(name = "RUN_ON", nullable = false)
    private Instant runOn;

    @NonNull
    @Column(name = "RUN_BY", nullable = false, length = 30)
    private String runBy;

    @NonNull
    @Column(name = "RUN_TIME", nullable = false)
    private Integer runTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DbMigration that = (DbMigration) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}