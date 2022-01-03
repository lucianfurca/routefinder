package com.tripplanner.routefinder.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Country {

    private String cca3;

    private List<String> borders;
}
