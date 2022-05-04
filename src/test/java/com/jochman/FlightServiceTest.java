package com.jochman;


import com.jochman.repositories.CargoRepository;
import com.jochman.repositories.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;
    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private FlightService flightService;


    @Test
    void shouldReturnCorrectFlightCargoWeightObjectForGivenFlightsAndFlightCargos(){
        //Given

        //When
        
        //Then
    }

    @Test
    void shouldThrowNotFoundExceptionForFlightNotPresentInDatabase(){
        //Given
        
        //When
        
        //Then
    }

    @Test
    void shouldReturnCorrectAirportFlightsInfoObjectForGivenFlightsAndFlightCargos(){
        //Given
        
        //When
        
        //Then
    }
}
