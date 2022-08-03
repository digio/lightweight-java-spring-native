package au.com.digio.lightweightjava.springnative.repository;

import au.com.digio.lightweightjava.springnative.model.Airport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AirportRepository {

    public List<Airport> findAll() {
        Airport sydneyAirport = Airport.builder()
                .code("SYD")
                .name("Sydney Kingsford Smith International Airport")
                .country("AU")
                .gpsCode("YSSY")
                .build();

        Airport melbourneAirport = Airport.builder()
                .code("MEL")
                .name("Melbourne International Airport")
                .country("AU")
                .gpsCode("YMML")
                .build();

        return List.of(sydneyAirport, melbourneAirport);
    }
}