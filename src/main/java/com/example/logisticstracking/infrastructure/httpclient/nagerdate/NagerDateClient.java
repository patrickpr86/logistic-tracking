package com.example.logisticstracking.infrastructure.httpclient.nagerdate;

import com.example.logisticstracking.infrastructure.httpclient.nagerdate.dto.NagerDateHolidayResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "nagerDateClient", url = "https://date.nager.at")
public interface NagerDateClient {

    @GetMapping("/api/v3/PublicHolidays/{year}/{countryCode}")
    List<NagerDateHolidayResponse> getPublicHolidays(
            @PathVariable("year") int year,
            @PathVariable("countryCode") String countryCode
    );
}
