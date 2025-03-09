package com.example.logisticstracking.infrastructure.httpclient.nagerdate;

import com.example.logisticstracking.infrastructure.httpclient.nagerdate.dto.NagerDateHolidayResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${nager-date-client.name}", url = "${nager-date-client.url}")
public interface NagerDateClient {

    @GetMapping("${nager-date-client.path}")
    List<NagerDateHolidayResponse> getPublicHolidays(
            @PathVariable("year") int year,
            @PathVariable("countryCode") String countryCode
    );
}
