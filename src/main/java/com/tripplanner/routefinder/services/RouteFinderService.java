package com.tripplanner.routefinder.services;

import java.io.IOException;
import java.util.List;

public interface RouteFinderService {

    List<String> routing(String origin, String destination) throws IOException;
}
