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

    /**
     * Saves all given Flight objects in database
     * @param flights - list of flights to be saved
     */
    public void addFlights(List<Flight> flights) {
        flightRepository.saveAll(flights);
    }

    /**
     * Saves all given FlightCargo objects in database
     * @param flightCargos - list of flight cargos to be saved
     */
    public void addCargos(List<FlightCargo> flightCargos) {
        cargoRepository.saveAll(flightCargos);
    }


    /**
     * Returns FlightCargoWeight object for given flight, if given flight is not present in DB throws NotFoundException
     * @param flightNumber - represents flight number
     * @param departureDate - flight departure date
     * @return FlightCargoWeight object which represents baggage weight, cargos weight and total weight
     */
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

    /**
     * Returns AirportFlightsInfo object for given airport and day
     * @param AirportIATACode - represents an airport
     * @param date - for which method finds all flights departing and arriving to an airport on this day
     * @return AirportFlightsInfo which represents number of departing and arriving flights along with number of pieces
     * departing and arriving baggage
     */
    public AirportFlightsInfo getAirportFlightsInfo(String AirportIATACode, ZonedDateTime date) {
        List<Flight> departingFlights = flightRepository.
                findByDepartureDateBetweenAndDepartureAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1),
                        AirportIATACode).orElseThrow(NotFoundException::new);

        List<Flight> arrivingFlights = flightRepository.
                findByDepartureDateBetweenAndArrivalAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1),
                        AirportIATACode).
                orElseThrow(NotFoundException::new);

        int numberOfFlightsDeparting = departingFlights.size();
        int numberOfFlightsArriving = arrivingFlights.size();

        int piecesOfBaggageDeparting = countPiecesOfBaggage(departingFlights);
        int piecesOfBaggageArriving = countPiecesOfBaggage(arrivingFlights);

        return new AirportFlightsInfo(
                numberOfFlightsDeparting,
                numberOfFlightsArriving,
                piecesOfBaggageDeparting,
                piecesOfBaggageArriving);
    }

    /**
     * Counts all flights' pieces of baggage
     * @param flights - list of flights for which pieces of baggage are counted
     * @return number of pieces of baggage
     */
    private int countPiecesOfBaggage(List<Flight> flights) {
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
