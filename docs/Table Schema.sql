-- 🗄️ V1__create_users_roles.sql
CREATE TABLE roles (
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(100) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role_id     BIGINT NOT NULL,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

INSERT INTO roles (name) VALUES
    ('ADMIN'), ('TEACHER'), ('STUDENT'), ('PARENT'), ('STAFF');

-- 🗄️ V2__create_students.sql
CREATE TABLE students (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNIQUE,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    dob             DATE,
    gender          ENUM('MALE', 'FEMALE', 'OTHER'),
    address         TEXT,
    phone           VARCHAR(20),
    email           VARCHAR(150),
    admission_date  DATE,
    class_id        BIGINT,
    section_id      BIGINT,
    parent_id       BIGINT,
    photo           VARCHAR(255),
    studentStatus          ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


-- 🗄️ V3__create_teachers.sql
CREATE TABLE teachers (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNIQUE,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    dob             DATE,
    gender          ENUM('MALE', 'FEMALE', 'OTHER'),
    address         TEXT,
    phone           VARCHAR(20),
    email           VARCHAR(150),
    joining_date    DATE,
    subject_id      BIGINT,
    qualification   VARCHAR(255),
    experience      INT DEFAULT 0,
    photo           VARCHAR(255),
    studentStatus          ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at      DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


-- 🗄️ V4__create_classes_sections_subjects.sql
CREATE TABLE classes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    grade_level INT NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE sections (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id    BIGINT NOT NULL,
    name        VARCHAR(50) NOT NULL,
    teacher_id  BIGINT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL
);

CREATE TABLE subjects (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    subjectCode        VARCHAR(20) UNIQUE,
    description TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add foreign keys to students and teachers now that classes/subjects exist
ALTER TABLE students
    ADD CONSTRAINT fk_student_class FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE SET NULL,
    ADD CONSTRAINT fk_student_section FOREIGN KEY (section_id) REFERENCES sections(id) ON DELETE SET NULL;

ALTER TABLE teachers
    ADD CONSTRAINT fk_teacher_subject FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE SET NULL;

-- 🗄️ V5__create_timetable.sql
CREATE TABLE rooms (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    type        ENUM('CLASSROOM', 'LAB', 'LIBRARY', 'OFFICE', 'SPORTS', 'OTHER'),
    capacity    INT,
    floor       VARCHAR(50),
    building    VARCHAR(100),
    studentStatus      ENUM('AVAILABLE', 'OCCUPIED', 'UNDER_MAINTENANCE') DEFAULT 'AVAILABLE',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE timetable (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    class_id    BIGINT NOT NULL,
    section_id  BIGINT NOT NULL,
    subject_id  BIGINT NOT NULL,
    teacher_id  BIGINT NOT NULL,
    room_id     BIGINT,
    day_of_week ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY'),
    start_time  TIME NOT NULL,
    end_time    TIME NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (class_id)   REFERENCES classes(id)   ON DELETE CASCADE,
    FOREIGN KEY (section_id) REFERENCES sections(id)  ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id)  ON DELETE CASCADE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(id)  ON DELETE CASCADE,
    FOREIGN KEY (room_id)    REFERENCES rooms(id)     ON DELETE SET NULL
);

-- 🗄️ V6__create_attendance.sql
CREATE TABLE attendance (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id 	BIGINT NOT NULL,
    student_id  BIGINT NOT NULL,
    class_id    BIGINT NOT NULL,
    date        DATE NOT NULL,
    studentStatus      ENUM('PRESENT','ABSENT','LATE','HALF_DAY','HOLIDAY') NOT NULL,
    marked_by   BIGINT,
    remarks     VARCHAR(255),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_student_date (student_id, date),
    FOREIGN KEY (school_id) REFERENCES schools(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (class_id)   REFERENCES classes(id)  ON DELETE CASCADE,
    FOREIGN KEY (marked_by)  REFERENCES users(id)    ON DELETE SET NULL
);

CREATE TABLE teacher_attendance (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    teacher_id  BIGINT NOT NULL,
    date        DATE NOT NULL,
    studentStatus      ENUM('PRESENT','ABSENT','LATE','HALF_DAY','HOLIDAY') NOT NULL,
    remarks     VARCHAR(255),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_teacher_date (teacher_id, date),
    FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE CASCADE
);

-- 🗄️ V7__create_fees.sql
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
    studentStatus           ENUM('PAID','PENDING','OVERDUE','PARTIAL') DEFAULT 'PAID',
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id)       REFERENCES students(id)       ON DELETE CASCADE,
    FOREIGN KEY (fee_structure_id) REFERENCES fee_structures(id) ON DELETE CASCADE
);

-- 🗄️ V8__create_library.sql
CREATE TABLE books (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id 		 BIGINT NOT NULL,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255),
    isbn             VARCHAR(50) UNIQUE,
    category         VARCHAR(100),
    total_copies     INT DEFAULT 1,
    available_copies INT DEFAULT 1,
    publisher        VARCHAR(255),
    published_year   INT NOT NULL,
    created_at       DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	FOREIGN KEY (school_id)   REFERENCES schools(id)   ON DELETE CASCADE
);

CREATE TABLE book_issues (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id     BIGINT NOT NULL,
    student_id  BIGINT NOT NULL,
    school_id 	BIGINT NOT NULL,
    issue_date  DATE NOT NULL,
    due_date    DATE NOT NULL,
    return_date DATE,
    fine        DECIMAL(8,2) DEFAULT 0.00,
    studentStatus      ENUM('ISSUED','RETURNED','OVERDUE') DEFAULT 'ISSUED',
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id)    REFERENCES books(id)    ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id)   REFERENCES schools(id)   ON DELETE CASCADE
);

-- 🗄️ V9__create_facilities.sql
CREATE TABLE facilities (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    location    VARCHAR(255),
    studentStatus      ENUM('ACTIVE','INACTIVE','UNDER_MAINTENANCE') DEFAULT 'ACTIVE',
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
    studentStatus        ENUM('OPEN','IN_PROGRESS','RESOLVED') DEFAULT 'OPEN',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (facility_id) REFERENCES facilities(id) ON DELETE CASCADE,
    FOREIGN KEY (reported_by) REFERENCES users(id)      ON DELETE SET NULL
);

-- 🗄️ V10__create_exams_results.sql
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
    studentStatus          ENUM('PASS','FAIL') NOT NULL,
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

-- 🗄️ V11__create_staff_hr.sql
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
    studentStatus        ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
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
    studentStatus       ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
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
    studentStatus        ENUM('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
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
    studentStatus       ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
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

-- 🗄️ V12__create_parents.sql
CREATE TABLE parents (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT UNIQUE,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(150),
    address     TEXT,
    occupation  VARCHAR(150),
    relation    ENUM('FATHER','MOTHER','GUARDIAN') NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);

ALTER TABLE students
    ADD CONSTRAINT fk_student_parent FOREIGN KEY (parent_id) REFERENCES parents(id) ON DELETE SET NULL;

-- 🗄️ V13__create_notifications.sql
CREATE TABLE notifications (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    title       VARCHAR(255) NOT NULL,
    message     TEXT NOT NULL,
    is_read     BOOLEAN DEFAULT FALSE,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 🗄️ V14__create_activity_logs.sql
CREATE TABLE activity_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT,
    action      ENUM('CREATE','UPDATE','DELETE','LOGIN','LOGOUT') NOT NULL,
    module      VARCHAR(50) NOT NULL,
    description TEXT,
    ip_address  VARCHAR(50),
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);