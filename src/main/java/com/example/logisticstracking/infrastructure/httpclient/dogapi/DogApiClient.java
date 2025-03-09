package com.example.logisticstracking.infrastructure.httpclient.dogapi;

import com.example.logisticstracking.infrastructure.httpclient.dogapi.dto.DogFactResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${dog-api-client.name}", url = "${dog-api-client.url}")
public interface DogApiClient {

    @GetMapping("${dog-api-client.path}")
    DogFactResponse getFacts(@RequestParam("number") int number);
}
