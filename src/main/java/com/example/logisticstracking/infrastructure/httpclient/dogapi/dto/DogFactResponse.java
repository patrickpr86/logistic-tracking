package com.example.logisticstracking.infrastructure.httpclient.dogapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DogFactResponse implements Serializable {
    private List<String> facts;
}

