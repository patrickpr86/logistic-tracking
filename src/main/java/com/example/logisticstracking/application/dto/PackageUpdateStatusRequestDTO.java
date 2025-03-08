package com.example.logisticstracking.application.dto;

import com.example.logisticstracking.domain.enumeration.PackageStatus;

public record PackageUpdateStatusRequestDTO(PackageStatus status) {}

