package com.springworkshop.dealership.domain;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cars", schema = "inventory")
public class CarEntity {
    @Id
    public int id;
    @Column(length = 100)
    private String name;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CarType carType;
}
