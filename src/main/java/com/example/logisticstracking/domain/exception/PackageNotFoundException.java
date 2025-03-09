package com.example.logisticstracking.domain.exception;

public class PackageNotFoundException extends RuntimeException {

    private static final String PACKAGE_NOT_FOUND_EXCEPTION_MESSAGE = "Package not found: ";
    public PackageNotFoundException(String packageId) {
        super(PACKAGE_NOT_FOUND_EXCEPTION_MESSAGE + packageId);
    }
}
