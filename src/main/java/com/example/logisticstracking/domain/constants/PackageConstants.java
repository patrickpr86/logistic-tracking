package com.example.logisticstracking.domain.constants;

public class PackageConstants {

    //COMMON
    public static final String PACKAGE_PREFIX_ID = "package-";
    public static final String ASYNC_EXECUTOR_THREAD_PREFIX = "Async-Executor-";
    public static final int ASYNC_CORE_POOL_SIZE = 10;
    public static final int ASYNC_MAX_POOL_SIZE = 50;
    public static final int ASYNC_QUEUE_CAPACITY = 200;
    public static final int ASYNC_KEEP_ALIVE_SECONDS = 120;

    // KAFKA METRICS
    public static final String ACTIVE_THREADS = "activeThreads";
    public static final String MAX_THREADS = "maxThreads";
    public static final String QUEUE_SIZE = "queueSize";

    //KAFKA PROPS
    public static final String DEFAULT_TRACKING_TOPIC = "tracking-events";
    public static final int DEFAULT_TRACKING_PARTITIONS = 3;
    public static final short DEFAULT_TRACKING_REPLICATION = 1;

    // HikariCP METRICS
    public static final String ACTIVE_CONNECTIONS = "activeConnections";
    public static final String IDLE_CONNECTIONS = "idleConnections";
    public static final String TOTAL_CONNECTIONS = "totalConnections";
    public static final String WAITING_THREADS = "waitingThreads";

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
    public static final String LOG_GET_ALL_PACKAGES_TEMPLATE = "m=execute Fetching all package details for sender={}, recipient={}";
    public static final String LOG_CREATE_PACKAGE_TEMPLATE = "m=execute Creating a new package";
    public static final String LOG_UPDATE_PACKAGE_STATUS_TEMPLATE = "m=execute Updating package status for package={} to {}";
    public static final String LOG_CANCEL_PACKAGE_TEMPLATE = "m=execute Canceling package={}";
    public static final String LOG_FETCH_PACKAGE_DETAILS_TEMPLATE = "m=execute Fetching package details for package={}, includeEvents={}";
    public static final String LOG_PACKAGE_DETAILS_FETCHED_TEMPLATE = "m=execute Package details fetched successfully for package={}";


    public static final String LOG_HOLIDAY_FOUND_TEMPLATE = "m=execute Date {} is a holiday: {}";
    public static final String LOG_HOLIDAY_NOT_FOUND_TEMPLATE = "m=execute Date {} is NOT a holiday in country={}";
    public static final String LOG_DOG_FACT_OBTAINED_TEMPLATE = "m=execute Dog fact obtained: {}";

    public static final String LOG_TRACKING_EVENT_SENT_TO_KAFKA_TEMPLATE = "m=sendTrackingEventToKafka Tracking event sent to Kafka for package={}: {}";

    // CANCELLATION
    public static final String TRACKING_EVENT_CANCELLATION_LOCATION = "System";
    public static final String TRACKING_EVENT_CANCELLATION_DESCRIPTION = "Package canceled before shipment";

    // MESSAGES
    public static final String TRACKING_EVENT_MESSAGE = "Event created for package %s: %s";
    public static final String PACKAGE_CANNOT_BE_CANCELED_MESSAGE = "It is not possible to cancel the package because it is not in CREATED.";
    public static final String INVALID_STATUS_TRANSITION_MESSAGE = "Invalid status transition: %s -> %s";
    public static final String DEFAULT_DOG_FACT_MESSAGE = "Dogs are awesome!";
    public static final String PACKAGE_CANCELED_KAFKA_MESSAGE = "Package %s has been canceled";
    public static final String PACKAGE_CREATED_KAFKA_MESSAGE = "Package %s created successfully";
    public static final String PACKAGE_STATUS_UPDATED_KAFKA_MESSAGE = "Package %s status updated to %s";



}
