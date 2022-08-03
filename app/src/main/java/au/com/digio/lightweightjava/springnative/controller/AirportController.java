package au.com.digio.lightweightjava.springnative.controller;

import au.com.digio.lightweightjava.springnative.model.Airport;
import au.com.digio.lightweightjava.springnative.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AirportController {

    @Autowired
    private AirportService airportService;

    @GetMapping("/airports")
    public List<Airport> getAirports() {
        return airportService.getAirports();
    }
}