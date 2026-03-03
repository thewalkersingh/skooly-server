CREATE TABLE facilities (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    location    VARCHAR(255),
    status      ENUM('ACTIVE','INACTIVE','UNDER_MAINTENANCE') DEFAULT 'ACTIVE',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE maintenance_logs (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    facility_id   BIGINT NOT NULL,
    reported_by   BIGINT,
    issue         TEXT NOT NULL,
    reported_date DATE NOT NULL,
    resolved_date DATE,
    status        ENUM('OPEN','IN_PROGRESS','RESOLVED') DEFAULT 'OPEN',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (facility_id) REFERENCES facilities(id) ON DELETE CASCADE,
    FOREIGN KEY (reported_by) REFERENCES users(id)      ON DELETE SET NULL
);