CREATE TABLE fee_categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    amount      DECIMAL(10,2) NOT NULL,
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE fee_structures (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id        BIGINT NOT NULL,
    fee_category_id BIGINT NOT NULL,
    academic_year   VARCHAR(20) NOT NULL,
    due_date        DATE,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id)        REFERENCES classes(id)         ON DELETE CASCADE,
    FOREIGN KEY (fee_category_id) REFERENCES fee_categories(id)  ON DELETE CASCADE
);

CREATE TABLE fee_payments (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id       BIGINT NOT NULL,
    fee_structure_id BIGINT NOT NULL,
    amount_paid      DECIMAL(10,2) NOT NULL,
    payment_date     DATE NOT NULL,
    payment_mode     ENUM('CASH','ONLINE','CHEQUE','DD') NOT NULL,
    transaction_id   VARCHAR(100),
    status           ENUM('PAID','PENDING','OVERDUE','PARTIAL') DEFAULT 'PAID',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)       REFERENCES students(id)       ON DELETE CASCADE,
    FOREIGN KEY (fee_structure_id) REFERENCES fee_structures(id) ON DELETE CASCADE
);