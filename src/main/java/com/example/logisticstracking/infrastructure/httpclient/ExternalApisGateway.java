package com.example.logisticstracking.infrastructure.httpclient;

import com.example.logisticstracking.infrastructure.httpclient.dogapi.DogApiClient;
import com.example.logisticstracking.infrastructure.httpclient.dogapi.dto.DogFactResponse;
import com.example.logisticstracking.infrastructure.httpclient.nagerdate.NagerDateClient;
import com.example.logisticstracking.infrastructure.httpclient.nagerdate.dto.NagerDateHolidayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        List<NagerDateHolidayResponse> holidays = nagerDateClient.getPublicHolidays(date.getYear(), countryCode);
        String dateStr = date.toString();

        boolean isHoliday = holidays.stream()
                .anyMatch(holiday -> holiday.getDate().equals(dateStr));

        if (isHoliday) {
            holidays.stream()
                    .filter(holiday -> holiday.getDate().equals(dateStr))
                    .findFirst()
                    .ifPresent(holiday -> log.info(LOG_HOLIDAY_FOUND_TEMPLATE, dateStr, holiday.getLocalName()));
        } else {
            log.info(LOG_HOLIDAY_NOT_FOUND_TEMPLATE, dateStr, countryCode);
        }

        return isHoliday;
    }

    public String getRandomDogFunFact() {
        return Optional.ofNullable(dogApiClient.getFacts(1))
                .map(DogFactResponse::getFacts)
                .flatMap(facts -> facts.stream().findFirst())
                .map(fact -> {
                    log.info(LOG_DOG_FACT_OBTAINED_TEMPLATE, fact);
                    return fact;
                })
                .orElse(DEFAULT_DOG_FACT_MESSAGE);
    }

}
