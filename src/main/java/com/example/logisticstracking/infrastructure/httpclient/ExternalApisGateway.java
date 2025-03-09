package com.example.logisticstracking.infrastructure.httpclient;

import com.example.logisticstracking.infrastructure.httpclient.dogapi.DogApiClient;
import com.example.logisticstracking.infrastructure.httpclient.dogapi.dto.DogFactResponse;
import com.example.logisticstracking.infrastructure.httpclient.nagerdate.NagerDateClient;
import com.example.logisticstracking.infrastructure.httpclient.nagerdate.dto.NagerDateHolidayResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.example.logisticstracking.domain.constants.PackageConstants.*;

@Slf4j
@Component
public class ExternalApisGateway {
    private final NagerDateClient nagerDateClient;
    private final DogApiClient dogApiClient;

    public ExternalApisGateway(NagerDateClient nagerDateClient, DogApiClient dogApiClient) {
        this.nagerDateClient = nagerDateClient;
        this.dogApiClient = dogApiClient;
    }

    public boolean isHoliday(LocalDate date, String countryCode) {
        int year = date.getYear();
        List<NagerDateHolidayResponse> holidays = nagerDateClient.getPublicHolidays(year, countryCode);
        String dateStr = date.toString();

        for (NagerDateHolidayResponse holiday : holidays) {
            if (holiday.getDate().equals(dateStr)) {
                log.info(LOG_HOLIDAY_FOUND_TEMPLATE, dateStr, holiday.getLocalName());
                return true;
            }
        }
        log.info(LOG_HOLIDAY_NOT_FOUND_TEMPLATE, dateStr, countryCode);
        return false;
    }

    public String getRandomDogFunFact() {
        DogFactResponse response = dogApiClient.getFacts(1);
        if (response.getFacts() != null && !response.getFacts().isEmpty()) {
            String fact = response.getFacts().get(0);
            log.info(LOG_DOG_FACT_OBTAINED_TEMPLATE, fact);
            return fact;
        }
        return DEFAULT_DOG_FACT_MESSAGE;
    }
}
