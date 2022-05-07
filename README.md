Task was solved using Maven, Java 17, Spring Boot, Spring Data JPA with H2 Database and Postman.

Application gives you following endpoints to hit:
localhost:8080/api/v1/...
- POST post-test-flights (allows to save list of flights for purpose of testing)
- POST post-test-cargos (allows to save list of flight cargos for purpose of testing)
- GET get-flight-cargo?flightNumber={flightNumber}&departureDate={zonedDateTime} (returns cargo weight, baggage weight and total weight for given flight)
- GET get-airport-flights-info?AirportIATACode={IATACode}&date={zonedDateTime} (returns number of departing flights, number of arriving flights, number of departing pieces of baggage and number of arriving pieces of baggage for given airport and date)

I have also written unit tests I considered necessary using JUNIT and Mockito.