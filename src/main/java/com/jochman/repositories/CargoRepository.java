package com.jochman.repositories;

import com.jochman.entities.cargo.FlightCargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<FlightCargo, Long> {

}
