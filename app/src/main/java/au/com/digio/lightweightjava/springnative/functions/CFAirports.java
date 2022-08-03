package au.com.digio.lightweightjava.springnative.functions;

import au.com.digio.lightweightjava.springnative.model.Airport;
import au.com.digio.lightweightjava.springnative.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class CFAirports implements Function<String, List<Airport>> {

    @Autowired
    private AirportService airportService;

    @Override
    public List<Airport> apply(String s) {
        return airportService.getAirports();
    }
}