package au.com.digio.lightweightjava.springnative.service;

import au.com.digio.lightweightjava.springnative.model.Airport;
import au.com.digio.lightweightjava.springnative.repository.AirportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    @Autowired
    private AirportRepository airportRepository;

    public List<Airport> getAirports() {
        return airportRepository.findAll();
    }
}
