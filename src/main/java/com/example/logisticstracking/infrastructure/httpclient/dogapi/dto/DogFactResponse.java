package com.example.logisticstracking.infrastructure.httpclient.dogapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class DogFactResponse {
    private List<String> facts;
}

