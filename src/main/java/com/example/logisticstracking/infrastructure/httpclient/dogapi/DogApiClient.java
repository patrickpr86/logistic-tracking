package com.example.logisticstracking.infrastructure.httpclient.dogapi;

import com.example.logisticstracking.infrastructure.httpclient.dogapi.dto.DogFactResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dogApiClient", url = "https://dogapi.dog")
public interface DogApiClient {

    @GetMapping("/api/v1/facts")
    DogFactResponse getFacts(@RequestParam("number") int number);
}
