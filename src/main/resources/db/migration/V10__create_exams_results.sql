CREATE TABLE grade_scale (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade       VARCHAR(5) NOT NULL,
    min_marks   DECIMAL(5,2) NOT NULL,
    max_marks   DECIMAL(5,2) NOT NULL,
    gpa         DECIMAL(3,2),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE exams (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(150) NOT NULL,
    class_id        BIGINT NOT NULL,
    subject_id      BIGINT NOT NULL,
    exam_date       DATE NOT NULL,
    total_marks     DECIMAL(6,2) NOT NULL,
    passing_marks   DECIMAL(6,2) NOT NULL,
    academic_year   VARCHAR(20) NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id)   REFERENCES classes(id)   ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id)  ON DELETE CASCADE
);

CREATE TABLE results (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    exam_id         BIGINT NOT NULL,
    student_id      BIGINT NOT NULL,
    marks_obtained  DECIMAL(6,2) NOT NULL,
    grade           VARCHAR(5),
    remarks         VARCHAR(255),
    status          ENUM('PASS','FAIL') NOT NULL,
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_exam_student (exam_id, student_id),
    FOREIGN KEY (exam_id)    REFERENCES exams(id)    ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

INSERT INTO grade_scale (grade, min_marks, max_marks, gpa) VALUES
    ('A+', 90.00, 100.00, 4.00),
    ('A',  80.00,  89.99, 3.75),
    ('B+', 70.00,  79.99, 3.50),
    ('B',  60.00,  69.99, 3.00),
    ('C+', 50.00,  59.99, 2.50),
    ('C',  40.00,  49.99, 2.00),
    ('D',  35.00,  39.99, 1.00),
    ('F',   0.00,  34.99, 0.00);