package com.jochman;

import com.jochman.entities.cargo.FlightCargo;
import com.jochman.entities.Flight;
import com.jochman.responses.AirportFlightsInfo;
import com.jochman.responses.FlightCargoWeight;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class FlightController {

    private final FlightService flightService;

    @PostMapping("post-test-flights")
    public ResponseEntity postTestFlights(@RequestBody List<Flight> flights){
        flightService.saveFlights(flights);
        log.info("Test flights successfully posted");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("post-test-cargos")
    public ResponseEntity postTestCargos(@RequestBody List<FlightCargo> flightCargos){
        flightService.saveFlightCargos(flightCargos);
        log.info("Test cargos successfully posted");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("get-flight-cargo")
    public ResponseEntity<FlightCargoWeight> getFlightCargo(
            @RequestParam("flightNumber") Integer flightNumber,
            @RequestParam("departureDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime departureDate
    ){
        log.info("Get request for FlightCargoWeight for flight number {} and date {}", flightNumber, departureDate);
        return new ResponseEntity<>(
                flightService.getFlightCargoWeight(flightNumber, departureDate),
                HttpStatus.OK);
    }

    @GetMapping("get-airport-flights-info")
    public ResponseEntity<AirportFlightsInfo> getAirportFlightsInfo(
            @RequestParam("AirportIATACode") String airportIATACode,
            @RequestParam("date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime date
    ){
        log.info("Get request for AirportFlightsInfo for airport {} and date {}", airportIATACode, date);
        return new ResponseEntity<>(
                flightService.getAirportFlightsInfo(airportIATACode, date),
                HttpStatus.OK);
    }
}
