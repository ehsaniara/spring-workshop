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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_jpa_sequence_generator")
    @SequenceGenerator(name = "car_jpa_sequence_generator", schema = "inventory", sequenceName = "car_id_seq", allocationSize = 1)
    public Integer id;
    @Column(length = 100)
    private String name;
    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private CarType carType;
}
