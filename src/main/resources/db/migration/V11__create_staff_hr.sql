CREATE TABLE departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE staff (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNIQUE,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    dob           DATE,
    gender        ENUM('MALE','FEMALE','OTHER'),
    address       TEXT,
    phone         VARCHAR(20),
    email         VARCHAR(150),
    department_id BIGINT,
    designation   VARCHAR(100),
    joining_date  DATE,
    salary        DECIMAL(10,2),
    photo         VARCHAR(255),
    status        ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)       REFERENCES users(id)       ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

ALTER TABLE departments
    ADD COLUMN head_id BIGINT,
    ADD CONSTRAINT fk_dept_head FOREIGN KEY (head_id) REFERENCES staff(id) ON DELETE SET NULL;

CREATE TABLE leave_requests (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id     BIGINT NOT NULL,
    leave_type   ENUM('SICK','CASUAL','EARNED','MATERNITY','OTHER') NOT NULL,
    from_date    DATE NOT NULL,
    to_date      DATE NOT NULL,
    reason       TEXT,
    status       ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    approved_by  BIGINT,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (staff_id)    REFERENCES staff(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE payroll (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    staff_id     BIGINT NOT NULL,
    month        TINYINT NOT NULL,
    year         YEAR NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    allowances   DECIMAL(10,2) DEFAULT 0.00,
    deductions   DECIMAL(10,2) DEFAULT 0.00,
    net_salary   DECIMAL(10,2) NOT NULL,
    paid_date    DATE,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_staff_month_year (staff_id, month, year),
    FOREIGN KEY (staff_id) REFERENCES staff(id) ON DELETE CASCADE
);