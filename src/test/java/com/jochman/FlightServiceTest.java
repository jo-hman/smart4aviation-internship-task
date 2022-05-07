package com.jochman;

import com.jochman.entities.Flight;
import com.jochman.entities.cargo.CargoEntry;
import com.jochman.entities.cargo.FlightCargo;
import com.jochman.exception.NotFoundException;
import com.jochman.repositories.CargoRepository;
import com.jochman.repositories.FlightRepository;
import com.jochman.responses.AirportFlightsInfo;
import com.jochman.responses.FlightCargoWeight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void shouldReturnCorrectFlightCargoWeightObjectForGivenFlight(){
        //Given
        int flightNumber = 3;
        ZonedDateTime departureDate = ZonedDateTime.of(
                LocalDate.of(2018, 1, 3),
                LocalTime.of(8,55),
                ZoneId.of("UTC+1"));

        Flight flight = new Flight(1L,
                1,
                "AAA",
                "BBB",
                departureDate);

        List<CargoEntry> baggage = Arrays.asList(
                new CargoEntry(13L, 1L, 12, "lb", 234),
                new CargoEntry(14L, 2L, 124, "lb", 3),
                new CargoEntry(15L, 3L, 53, "kg", 45));
        List<CargoEntry> cargo = Arrays.asList(
                new CargoEntry(16L, 1L, 443, "kg", 23),
                new CargoEntry(17L, 2L, 65, "lb", 12),
                new CargoEntry(18L, 3L, 322, "lb", 2));
        FlightCargo flightCargo = new FlightCargo(1L, baggage, cargo);

        given(flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate)).
                willReturn(Optional.of(flight));

        given(cargoRepository.findById(flight.getFlightId())).
                willReturn(Optional.of(flightCargo));

        double baggageWeight = flightCargo.countBaggageWeight();
        double cargoWeight = flightCargo.countCargoWeight();
        FlightCargoWeight expectedResult = new FlightCargoWeight(
                cargoWeight,
                baggageWeight,
                baggageWeight + cargoWeight,
                "kg");

        //When
        FlightCargoWeight flightCargoWeight = flightService.
                getFlightCargoWeight(flightNumber, departureDate);

        //Then
        assertEquals(expectedResult, flightCargoWeight);
    }

    @Test
    void shouldThrowNotFoundExceptionForFlightNotPresentInDatabase(){
        //Given
        int flightNumber = 3;
        ZonedDateTime departureDate = ZonedDateTime.of(
                LocalDate.of(2018, 1, 3),
                LocalTime.of(8,55),
                ZoneId.of("UTC+1"));

        given(flightRepository.findByFlightNumberAndDepartureDate(flightNumber, departureDate)).
                willReturn(Optional.empty());

        //When Then
        assertThatThrownBy(() -> flightService.getFlightCargoWeight(flightNumber, departureDate)).
                isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldReturnCorrectAirportFlightsInfoObjectForGivenFlightsAndFlightCargos(){
        //Given
        ZonedDateTime date = ZonedDateTime.of(
                LocalDate.of(2019, 3, 12),
                LocalTime.of(12,33),
                ZoneId.of("UTC+1"));

        String airportIATACode = "AAA";

        Flight flight1 =
                new Flight(1L, 1, airportIATACode, "BBB", date);
        Flight flight2 =
                new Flight(2L, 2, "BBB", airportIATACode, date);
        Flight flight3 =
                new Flight(3L, 3, airportIATACode, "CCC", date);

        List<CargoEntry> cargoEntries1 = Arrays.asList(
                new CargoEntry(1L, 1L, 231, "kg", 21),
                new CargoEntry(2L, 2L, 321, "lb", 32),
                new CargoEntry(3L, 3L, 432, "kg", 42));
        List<CargoEntry> cargoEntries2 = Arrays.asList(
                new CargoEntry(4L, 1L, 312, "lb", 221),
                new CargoEntry(5L, 2L, 12, "lb", 31),
                new CargoEntry(6L, 3L, 1203, "kg", 4));
        List<CargoEntry> cargoEntries3 = Arrays.asList(
                new CargoEntry(7L, 1L, 54, "kg", 213),
                new CargoEntry(8L, 2L, 342, "lb", 31),
                new CargoEntry(9L, 3L, 764, "kg", 23));
        FlightCargo flightCargo1 = new FlightCargo(1L, cargoEntries1, cargoEntries2);
        FlightCargo flightCargo2 = new FlightCargo(2L, cargoEntries3, cargoEntries1);
        FlightCargo flightCargo3 = new FlightCargo(3L, cargoEntries3, cargoEntries2);

        int numberOfFlightsDeparting = 2;
        int numberOfFlightsArriving = 1;

        int piecesOfBaggageDeparting = 362;
        int piecesOfBaggageArriving = 267;

        AirportFlightsInfo expectedResult = new AirportFlightsInfo(
                numberOfFlightsDeparting,
                numberOfFlightsArriving,
                piecesOfBaggageDeparting,
                piecesOfBaggageArriving);

        given(flightRepository.
                findByDepartureDateBetweenAndDepartureAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1), airportIATACode)).
                willReturn(Optional.of(Arrays.asList(flight1, flight3)));
        given(flightRepository.
                findByDepartureDateBetweenAndArrivalAirportIATACode(
                        date.toLocalDate().atStartOfDay(date.getZone()),
                        date.toLocalDate().atStartOfDay(date.getZone()).plusDays(1), airportIATACode)).
                willReturn(Optional.of(Arrays.asList(flight2)));
        given(cargoRepository.findById(anyLong())).
                willReturn(Optional.of(flightCargo1)).
                willReturn(Optional.of(flightCargo3)).
                willReturn(Optional.of(flightCargo2));

        //When
        AirportFlightsInfo airportFlightsInfo = flightService.getAirportFlightsInfo(airportIATACode, date);

        //Then
        assertEquals(expectedResult, airportFlightsInfo);
    }
}
