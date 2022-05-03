package com.jochman;

import com.jochman.entities.cargo.FlightCargo;
import com.jochman.entities.Flight;
import com.jochman.exception.NotFoundException;
import com.jochman.repositories.CargoRepository;
import com.jochman.repositories.FlightRepository;
import com.jochman.responses.AirportFlightsInfo;
import com.jochman.responses.FlightCargoWeight;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class FlightService {
    private final FlightRepository flightRepository;
    private final CargoRepository cargoRepository;

    public void addFlights(List<Flight> flights) {
        flightRepository.saveAll(flights);
    }

    public void addCargos(List<FlightCargo> flightCargos) {
        cargoRepository.saveAll(flightCargos);
    }

    public FlightCargoWeight getFlightCargoWeight(Integer flightNumber, ZonedDateTime departureDate) {
        Flight flight = flightRepository.
                findByFlightNumberAndDepartureDate(flightNumber, departureDate).
                orElseThrow(NotFoundException::new);

        FlightCargo flightCargo = cargoRepository.
                findById(flight.getFlightId()).
                orElseThrow(NotFoundException::new);

        double cargoWeight = flightCargo.countCargoWeight();
        double baggageWeight = flightCargo.countBaggageWeight();

        return new FlightCargoWeight(cargoWeight,
                baggageWeight,
                cargoWeight + baggageWeight,
                "kg");
    }


    public AirportFlightsInfo getAirportFlightsInfo(String IATAAirportCode, ZonedDateTime date) {
        List<Flight> departingFlights = flightRepository.
                findByDepartureDateBetweenAndDepartureAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1),
                        IATAAirportCode).orElseThrow(NotFoundException::new);

        List<Flight> arrivingFlights = flightRepository.
                findByDepartureDateBetweenAndArrivalAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1),
                        IATAAirportCode).
                orElseThrow(NotFoundException::new);

        int numberOfDepartingFlights = departingFlights.size();
        int numberOfArrivingFlights = arrivingFlights.size();

        int piecesOfBaggageDeparting = countPieces(departingFlights);
        int piecesOfBaggageArriving = countPieces(arrivingFlights);


        return new AirportFlightsInfo(
                numberOfDepartingFlights,
                numberOfArrivingFlights,
                piecesOfBaggageDeparting,
                piecesOfBaggageArriving);
    }

    private int countPieces(List<Flight> flights) {
        int totalPieces = 0;
        for(Flight flight : flights){
            FlightCargo flightCargo = cargoRepository.
                    findById(flight.getFlightId()).
                    orElseThrow(NotFoundException::new);
            totalPieces += flightCargo.countPiecesOfBaggage();
        }
        return totalPieces;
    }
}
