package com.example.logisticstracking.infrastructure.httpclient.nagerdate.dto;

import lombok.Data;

@Data
public class NagerDateHolidayResponse {
    private String date;
    private String localName;
    private String name;
    private String countryCode;
    private boolean fixed;
    private boolean global;
}
