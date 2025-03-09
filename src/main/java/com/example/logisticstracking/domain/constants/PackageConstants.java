package com.example.logisticstracking.domain.constants;

public class PackageConstants {

    public static final String PACKAGE_PREFIX_ID = "package-";

    public static final String DEFAULT_COUNTRY = "BR";
    public static final int DEFAULT_ESTIMATED_DAYS = 10;

    public static final String PACKAGE_ORIGIN_LOCATION = "Origin";
    public static final String PACKAGE_CREATED_DESCRIPTION = "Package created and registered in the system";


    // LOG TEMPLATE
    public static final String LOG_START_PACKAGE_CREATION_TEMPLATE = "m=execute Starting package creation. sender={}, recipient={}";
    public static final String LOG_PACKAGE_CREATED_SUCCESS_TEMPLATE = "m=execute Package successfully created. ID={}";
    public static final String LOG_PARSE_DATE_ERROR_TEMPLATE = "m=parseDate Error parsing date={}, using default={}";

    public static final String LOG_ATTEMPT_CANCEL_PACKAGE_TEMPLATE = "m=execute Attempting to cancel package: {}";
    public static final String LOG_PACKAGE_CANCELED_SUCCESS_TEMPLATE = "m=execute Package={} successfully canceled.";
    public static final String LOG_PACKAGE_CANNOT_BE_CANCELED_TEMPLATE = "m=execute Cannot cancel package={}, current status={}";

    public static final String LOG_CREATING_TRACKING_EVENT_TEMPLATE = "m=execute Creating tracking event for package={}";
    public static final String LOG_TRACKING_EVENT_CREATED_SUCCESS_TEMPLATE = "m=execute TrackingEvent={} successfully created for package={}";
    public static final String LOG_RECEIVED_TRACKING_EVENT_REQUEST_TEMPLATE = "m=execute Received request to create TrackingEvent: {}";

    public static final String LOG_UPDATING_PACKAGE_STATUS_TEMPLATE = "m=execute Updating package status for package={} to {}";
    public static final String LOG_PACKAGE_STATUS_UPDATED_TEMPLATE = "m=execute Package status updated for package={} to {}";

    public static final String LOG_GET_PACKAGE_DETAILS_TEMPLATE = "m=execute Fetching package details for package={}, include events={}";
    public static final String LOG_CREATE_PACKAGE_TEMPLATE = "m=execute Creating a new package";
    public static final String LOG_UPDATE_PACKAGE_STATUS_TEMPLATE = "m=execute Updating package status for package={} to {}";
    public static final String LOG_CANCEL_PACKAGE_TEMPLATE = "m=execute Canceling package={}";

    public static final String LOG_HOLIDAY_FOUND_TEMPLATE = "m=execute Date {} is a holiday: {}";
    public static final String LOG_HOLIDAY_NOT_FOUND_TEMPLATE = "m=execute Date {} is NOT a holiday in country={}";
    public static final String LOG_DOG_FACT_OBTAINED_TEMPLATE = "m=execute Dog fact obtained: {}";

    public static final String LOG_TRACKING_EVENT_SENT_TO_KAFKA_TEMPLATE = "m=sendTrackingEventToKafka Tracking event sent to Kafka for package={}: {}";

    // Tracking Event Cancellation Constants
    public static final String TRACKING_EVENT_CANCELLATION_LOCATION = "System";
    public static final String TRACKING_EVENT_CANCELLATION_DESCRIPTION = "Package canceled before shipment";

    // MESSAGES
    public static final String TRACKING_EVENT_MESSAGE = "Event created for package %s: %s";
    public static final String PACKAGE_CANNOT_BE_CANCELED_MESSAGE = "It is not possible to cancel the package because it is not in CREATED.";
    public static final String INVALID_STATUS_TRANSITION_MESSAGE = "Invalid status transition: %s -> %s";
    public static final String DEFAULT_DOG_FACT_MESSAGE = "Dogs are awesome!";
}
