package com.tripplanner.routefinder.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripplanner.routefinder.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class RouteFinderServiceImpl implements RouteFinderService {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public List<String> routing(String origin, String destination) throws IOException {
        List<String> route;
        List<Country> countries = retrieveCountriesWithNeighbours();

        //populate countries with borders graph
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        countries.stream().forEach(
                c -> {
                    String currentCountryVertex = c.getCca3();
                    graph.addVertex(currentCountryVertex);
                    List<String> borderingCountries = c.getBorders();
                    for (String borderCountry : borderingCountries) {
                        graph.addVertex(borderCountry);
                        graph.addEdge(currentCountryVertex, borderCountry);
                    }
                }
        );

        String originCountry;
        Optional<Country> originCountryOptional =
                countries.stream().filter(c -> c.getCca3().equalsIgnoreCase(origin)).findFirst();
        if (originCountryOptional.isPresent()) {
            originCountry = originCountryOptional.get().getCca3();
        } else {
            log.error("Origin country {} not found.", origin);
            return Collections.emptyList();
        }

        String destinationCountry;
        Optional<Country> destinationCountryOptional =
                countries.stream().filter(c -> c.getCca3().equalsIgnoreCase(destination)).findFirst();
        if (destinationCountryOptional.isPresent()) {
            destinationCountry = destinationCountryOptional.get().getCca3();
        } else {
            log.error("Destination country {} not found.", destination);
            return Collections.emptyList();
        }

        BellmanFordShortestPath bellmanFordShortestPath = new BellmanFordShortestPath(graph);
        try {
            route = bellmanFordShortestPath.getPath(originCountry, destinationCountry).getVertexList();
        } catch (Exception e) {
            log.error("Running shortest path algorithm threw {}", e.toString());
            return Collections.emptyList();
        }

        return route;
    }

    private List<Country> retrieveCountriesWithNeighbours() throws IOException {
        List<Country> countriesWithNeighbours = new ArrayList<>();

        JsonNode treeNodes = MAPPER.readTree(new ClassPathResource("countries.json").getFile());
        treeNodes.forEach(
                n -> {
                    Country country = new Country();
                    country.setCca3(n.findValue("cca3").textValue());
                    String bordersString = n.findValue("borders").toString()
                            .replace("\"", "")
                            .replace("[", "")
                            .replace("]", "");
                    country.setBorders(Arrays.asList(bordersString.split(",")));

                    if (!country.getBorders().get(0).isEmpty()) {
                        countriesWithNeighbours.add(country);
                    }
                }
        );

        return countriesWithNeighbours;
    }
}
