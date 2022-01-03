package com.tripplanner.routefinder.controller;

import com.tripplanner.routefinder.services.RouteFinderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class RouteFinderController {

    @Autowired
    private RouteFinderService routeFinderService;

    @GetMapping("/routing/{origin}/{destination}")
    public ResponseEntity<Object> routing(@PathVariable String origin,
                                          @PathVariable String destination) throws IOException {

        List<String> route = routeFinderService.routing(origin, destination);
        Map<String, List<String>> result = new HashMap<>();
        result.put("route", route);

        if (route.isEmpty()) {
            log.error("Route does not exist between countries: {} and {}", origin, destination);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
