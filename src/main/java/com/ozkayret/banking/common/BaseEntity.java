package com.ozkayret.banking.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    @JsonIgnore
    private Boolean status = true;

    @CreatedDate
    @Column(nullable = false)
    @JsonIgnore
    private Date createDate;

    @LastModifiedDate
    @Column(nullable = false)
    @JsonIgnore
    private Date updateDate;

    @CreatedBy
    @JsonIgnore
    private UUID createdBy;

    @LastModifiedBy
    @JsonIgnore
    private UUID lastModifiedBy;

    @Version
    @JsonIgnore
    private Integer version;
}
