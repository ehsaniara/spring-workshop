package com.springworkshop.dealership.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public abstract class AuditModel {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createDate;
    @LastModifiedDate
    private Instant updateDate;
}
