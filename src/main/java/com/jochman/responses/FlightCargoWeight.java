package com.jochman.responses;

public record FlightCargoWeight(Double cargoWeight,
                                Double baggageWeight,
                                Double totalWeight,
                                String weightUnit) {}
