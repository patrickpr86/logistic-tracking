CREATE DATABASE IF NOT EXISTS logistics_db;
USE logistics_db;

CREATE TABLE packages (
    id                      VARCHAR(50) NOT NULL,
    created_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, created_at),
    description             VARCHAR(255) NOT NULL,
    sender                  VARCHAR(255) NOT NULL,
    recipient               VARCHAR(255) NOT NULL,
    status                  ENUM('CREATED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED', 'UNKNOWN') NOT NULL,
    updated_at              DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    delivered_at            DATETIME NULL,
    is_holiday              BOOLEAN NOT NULL DEFAULT FALSE,
    fun_fact                VARCHAR(1024) NULL,
    estimated_delivery_date DATETIME NULL,
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB
PARTITION BY RANGE (YEAR(created_at)) (
    PARTITION p_before_2023 VALUES LESS THAN (2023),
    PARTITION p_2023 VALUES LESS THAN (2024),
    PARTITION p_2024 VALUES LESS THAN (2025),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

CREATE TABLE tracking_events (
    id          BINARY(16) NOT NULL,
    date        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, date),
    location    VARCHAR(255) NOT NULL,
    description VARCHAR(500) NOT NULL,
    package_id  VARCHAR(50) NOT NULL,
    INDEX idx_package_date (package_id, date)
) ENGINE=InnoDB
PARTITION BY RANGE (YEAR(date)) (
    PARTITION p_before_2023 VALUES LESS THAN (2023),
    PARTITION p_2023 VALUES LESS THAN (2024),
    PARTITION p_2024 VALUES LESS THAN (2025),
    PARTITION p_future VALUES LESS THAN MAXVALUE
);

-- Criar tabelas de arquivamento
CREATE TABLE packages_archive LIKE packages;
CREATE TABLE tracking_events_archive LIKE tracking_events;

-- Criar trigger para arquivar registros antes de deletar
DELIMITER //
CREATE TRIGGER before_package_delete
BEFORE DELETE ON packages
FOR EACH ROW
BEGIN
    INSERT INTO packages_archive SELECT * FROM packages WHERE id = OLD.id;
    INSERT INTO tracking_events_archive SELECT * FROM tracking_events WHERE package_id = OLD.package_id;
    DELETE FROM tracking_events WHERE package_id = OLD.package_id;
END;
//
DELIMITER ;

-- Habilitar o event scheduler
SET GLOBAL event_scheduler = ON;

-- Criar evento para arquivar e expurgar registros antigos
DELIMITER //
CREATE EVENT IF NOT EXISTS archive_and_purge
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_DATE + INTERVAL 1 DAY
DO
BEGIN
    -- Arquivar pacotes antigos (30 dias)
    INSERT INTO packages_archive
    SELECT * FROM packages WHERE created_at < NOW() - INTERVAL 30 DAY
    LIMIT 1000;

    -- Excluir pacotes antigos (por lotes)
    DELETE FROM packages WHERE created_at < NOW() - INTERVAL 30 DAY
    LIMIT 1000;

    -- Arquivar eventos antigos (30 dias)
    INSERT INTO tracking_events_archive
    SELECT * FROM tracking_events WHERE date < NOW() - INTERVAL 30 DAY
    LIMIT 1000;

    -- Excluir eventos antigos (por lotes)
    DELETE FROM tracking_events WHERE date < NOW() - INTERVAL 30 DAY
    LIMIT 1000;
END;
//
DELIMITER ;
