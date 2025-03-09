package com.example.logisticstracking.application.dto;

import com.example.logisticstracking.domain.enumeration.PackageStatus;
import java.io.Serializable;

public record PackageUpdateStatusRequestDTO(PackageStatus status) implements Serializable {
}

