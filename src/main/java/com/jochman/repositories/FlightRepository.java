package com.jochman.repositories;

import com.jochman.entities.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumberAndDepartureDate(Integer flightNumber, ZonedDateTime departureDate);
    Optional<List<Flight>> findByDepartureDateBetweenAndDepartureAirportIATACode(
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate,
            @Param("departureAirportIATACode") String departureAirportIATACode);
    Optional<List<Flight>> findByDepartureDateBetweenAndArrivalAirportIATACode(
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate,
            @Param("arrivalAirportIATACode") String arrivalAirportIATACode);
}
