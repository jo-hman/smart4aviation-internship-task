package com.jochman.entities.cargo;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CargoEntry {
    @Id
    @GeneratedValue
    private Long cargoEntryId; //extra id which was needed to create CargoEntry table in H2 database
    private Long id;
    private Integer weight;
    private String weightUnit;
    private Integer pieces;
}
