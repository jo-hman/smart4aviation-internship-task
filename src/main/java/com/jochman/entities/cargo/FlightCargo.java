package com.jochman.entities.cargo;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class FlightCargo {
    @Id
    private Long flightId;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CargoEntry> baggage;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CargoEntry> cargo;

    public double countCargoWeight(){ return countWeight(cargo);}

    public double countBaggageWeight(){
        return countWeight(baggage);
    }

    public int countPiecesOfBaggage(){
        return countPieces(baggage);
    }

    private int countPieces(List<CargoEntry> cargoEntries) {
        int pieces = 0;
        for(CargoEntry entry : cargoEntries){
            pieces += entry.getPieces();
        }
        return pieces;
    }

    private double countWeight(List<CargoEntry> cargoEntries){
        double weight = 0;
        for(CargoEntry entry : cargoEntries){
            if(entry.getWeightUnit().equals("kg")){
                weight += entry.getWeight();
            }
            else {
                weight += entry.getWeight()/2.205; //conversion from lb to kg
            }
        }
        return weight;
    }
}
