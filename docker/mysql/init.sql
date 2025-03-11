CREATE DATABASE IF NOT EXISTS logistics_db;
USE logistics_db;

CREATE TABLE packages
(
    id                    VARCHAR(50) NOT NULL,
    createdAt             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (createdAt, id),
    description           VARCHAR(255) NOT NULL,
    sender                VARCHAR(255) NOT NULL,
    recipient             VARCHAR(255) NOT NULL,
    status                ENUM('CREATED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED', 'UNKNOWN') NOT NULL,
    updatedAt             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deliveredAt           DATETIME NULL,
    isHoliday             BOOLEAN NOT NULL DEFAULT FALSE,
    funFact               VARCHAR(1024) NULL,
    estimatedDeliveryDate DATETIME NULL,
    INDEX                 idx_status (status),
    INDEX                 idx_createdAt (createdAt)
) ENGINE=InnoDB
PARTITION BY RANGE (YEAR(createdAt)) (
    PARTITION p_before_2023 VALUES LESS THAN (2023),
    PARTITION p_2023 VALUES LESS THAN (2024),
    PARTITION p_2024 VALUES LESS THAN (2025),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

CREATE TABLE tracking_events
(
    id          BINARY(16) NOT NULL,
    date        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (date, id),
    location    VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    package_id  VARCHAR(50) NOT NULL,
    INDEX       idx_package_date (package_id, date)
) ENGINE=InnoDB
PARTITION BY RANGE (TO_DAYS(date)) (
    PARTITION p_old VALUES LESS THAN (TO_DAYS('2023-01-01')),
    PARTITION p_2023 VALUES LESS THAN (TO_DAYS('2024-01-01')),
    PARTITION p_2024 VALUES LESS THAN (TO_DAYS('2025-01-01')),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

-- Tabelas para arquivamento
CREATE TABLE packages_archive LIKE packages;
CREATE TABLE tracking_events_archive LIKE tracking_events;

-- Trigger para arquivar packages e tracking_events antes da exclusão
DELIMITER //
CREATE TRIGGER before_package_delete
    BEFORE DELETE ON packages
    FOR EACH ROW
BEGIN
    INSERT INTO packages_archive SELECT * FROM packages WHERE id = OLD.id;
    INSERT INTO tracking_events_archive SELECT * FROM tracking_events WHERE package_id = OLD.id;
    DELETE FROM tracking_events WHERE package_id = OLD.id;
END;
//
DELIMITER ;

-- Habilita o event scheduler
SET GLOBAL event_scheduler = ON;

-- Evento periódico de arquivamento e expurgo (dados com mais de 30 dias)
DELIMITER //
CREATE EVENT IF NOT EXISTS archive_and_purge
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY
DO
BEGIN
    -- Arquivar e excluir pacotes antigos (30 dias)
    INSERT INTO packages_archive
    SELECT * FROM packages WHERE createdAt < NOW() - INTERVAL 30 DAY;

    DELETE FROM packages WHERE createdAt < NOW() - INTERVAL 30 DAY;

    -- Arquivar eventos de rastreamento antigos (30 dias)
    INSERT INTO tracking_events_archive
    SELECT * FROM tracking_events WHERE date < NOW() - INTERVAL 30 DAY;

    -- Excluir eventos de rastreamento antigos (30 dias)
    DELETE FROM tracking_events WHERE date < NOW() - INTERVAL 30 DAY;
END;
//
DELIMITER ;
