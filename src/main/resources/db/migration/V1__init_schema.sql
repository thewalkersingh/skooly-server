-- ============================================================
-- V1__init_schema.sql
-- Skooly - Full Initial Schema
-- ============================================================

-- ============================================================
-- SCHOOLS (Root tenant table)
-- ============================================================
/*CREATE TABLE schools (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    code        VARCHAR(50)  NOT NULL UNIQUE,
    address     TEXT,
    phone       VARCHAR(20),
    email       VARCHAR(100),
    logo        VARCHAR(500),
    studentStatus      ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);*/

-- ============================================================
-- USERS & AUTH
-- ============================================================
/*CREATE TABLE roles (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE   -- ADMIN, TEACHER, STUDENT, PARENT, STAFF
);

CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    username    VARCHAR(100) NOT NULL,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(50)  NOT NULL,
    is_active   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_users_school_username (school_id, username),
    CONSTRAINT fk_users_school FOREIGN KEY (school_id) REFERENCES schools(id)
);*/

-- ============================================================
-- ACADEMIC STRUCTURE
-- ============================================================
/*CREATE TABLE classes (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT      NOT NULL,
    name        VARCHAR(100) NOT NULL,
    grade_level INT,
    CONSTRAINT fk_classes_school FOREIGN KEY (school_id) REFERENCES schools(id)
);

CREATE TABLE subjects (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    code        VARCHAR(50),
    description TEXT,
    CONSTRAINT fk_subjects_school FOREIGN KEY (school_id) REFERENCES schools(id)
);*/

-- ============================================================
-- TEACHERS (needed before sections for class teacher FK)
-- ============================================================
/*CREATE TABLE teachers (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id     BIGINT       NOT NULL,
    user_id       BIGINT       NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    dob           DATE,
    gender        ENUM('MALE','FEMALE','OTHER'),
    address       TEXT,
    phone         VARCHAR(20),
    email         VARCHAR(100),
    joining_date  DATE,
    subject_id    BIGINT,
    qualification VARCHAR(200),
    experience    INT,
    photo         VARCHAR(500),
    studentStatus        ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_teachers_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_teachers_user     FOREIGN KEY (user_id)    REFERENCES users(id),
    CONSTRAINT fk_teachers_subject  FOREIGN KEY (subject_id) REFERENCES subjects(id)
);*/

/*CREATE TABLE sections (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    class_id    BIGINT       NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    teacher_id  BIGINT,                   -- class teacher
    capacity    INT DEFAULT 40,
    CONSTRAINT fk_sections_school  FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_sections_class   FOREIGN KEY (class_id)   REFERENCES classes(id),
    CONSTRAINT fk_sections_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(id)
);*/

-- ============================================================
-- FACILITIES / ROOMS
-- ============================================================
/*CREATE TABLE facilities (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    location    VARCHAR(200),
    studentStatus      ENUM('ACTIVE','INACTIVE','UNDER_MAINTENANCE') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_facilities_school FOREIGN KEY (school_id) REFERENCES schools(id)
);*/

/*CREATE TABLE rooms (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    type        ENUM('CLASSROOM','LAB','LIBRARY','OFFICE','SPORTS') NOT NULL DEFAULT 'CLASSROOM',
    capacity    INT,
    floor       VARCHAR(50),
    building    VARCHAR(100),
    studentStatus      ENUM('AVAILABLE','OCCUPIED','MAINTENANCE') NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_rooms_school FOREIGN KEY (school_id) REFERENCES schools(id)
);
*/
/*CREATE TABLE maintenance_logs (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id     BIGINT NOT NULL,
    facility_id   BIGINT NOT NULL,
    reported_by   BIGINT,
    issue         TEXT   NOT NULL,
    reported_date DATE   NOT NULL,
    resolved_date DATE,
    studentStatus        ENUM('OPEN','IN_PROGRESS','RESOLVED') NOT NULL DEFAULT 'OPEN',
    CONSTRAINT fk_maint_school    FOREIGN KEY (school_id)   REFERENCES schools(id),
    CONSTRAINT fk_maint_facility  FOREIGN KEY (facility_id) REFERENCES facilities(id),
    CONSTRAINT fk_maint_reporter  FOREIGN KEY (reported_by) REFERENCES users(id)
);
*/
-- ============================================================
-- TIMETABLE
-- ============================================================
/*CREATE TABLE timetable (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT      NOT NULL,
    class_id    BIGINT      NOT NULL,
    section_id  BIGINT      NOT NULL,
    subject_id  BIGINT      NOT NULL,
    teacher_id  BIGINT      NOT NULL,
    day_of_week ENUM('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL,
    room_id     BIGINT,
    CONSTRAINT fk_tt_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_tt_class    FOREIGN KEY (class_id)   REFERENCES classes(id),
    CONSTRAINT fk_tt_section  FOREIGN KEY (section_id) REFERENCES sections(id),
    CONSTRAINT fk_tt_subject  FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_tt_teacher  FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    CONSTRAINT fk_tt_room     FOREIGN KEY (room_id)    REFERENCES rooms(id)
);*/

-- ============================================================
-- PARENTS
-- ============================================================
/*CREATE TABLE parents (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    first_name  VARCHAR(100) NOT NULL,
    last_name   VARCHAR(100) NOT NULL,
    phone       VARCHAR(20),
    email       VARCHAR(100),
    address     TEXT,
    occupation  VARCHAR(200),
    relation    VARCHAR(50),
    CONSTRAINT fk_parents_school FOREIGN KEY (school_id) REFERENCES schools(id),
    CONSTRAINT fk_parents_user   FOREIGN KEY (user_id)   REFERENCES users(id)
);
*/
-- ============================================================
-- STUDENTS
-- ============================================================
/*CREATE TABLE students (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id      BIGINT       NOT NULL,
    user_id        BIGINT       NOT NULL,
    first_name     VARCHAR(100) NOT NULL,
    last_name      VARCHAR(100) NOT NULL,
    dob            DATE,
    gender         ENUM('MALE','FEMALE','OTHER'),
    address        TEXT,
    phone          VARCHAR(20),
    email          VARCHAR(100),
    admission_date DATE,
    class_id       BIGINT,
    section_id     BIGINT,
    parent_id      BIGINT,
    photo          VARCHAR(500),
    studentStatus         ENUM('ACTIVE','INACTIVE','GRADUATED','TRANSFERRED') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_students_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_students_user     FOREIGN KEY (user_id)    REFERENCES users(id),
    CONSTRAINT fk_students_class    FOREIGN KEY (class_id)   REFERENCES classes(id),
    CONSTRAINT fk_students_section  FOREIGN KEY (section_id) REFERENCES sections(id),
    CONSTRAINT fk_students_parent   FOREIGN KEY (parent_id)  REFERENCES parents(id)
);*/

-- ============================================================
-- ATTENDANCE
-- ============================================================
/*CREATE TABLE attendance (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT NOT NULL,
    student_id  BIGINT NOT NULL,
    class_id    BIGINT NOT NULL,
    date        DATE   NOT NULL,
    studentStatus      ENUM('PRESENT','ABSENT','LATE') NOT NULL,
    marked_by   BIGINT,
    remarks     VARCHAR(500),
    CONSTRAINT fk_att_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_att_student  FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_att_class    FOREIGN KEY (class_id)   REFERENCES classes(id),
    CONSTRAINT fk_att_marker   FOREIGN KEY (marked_by)  REFERENCES users(id),
    UNIQUE KEY uq_attendance (school_id, student_id, date)
);*/

/*CREATE TABLE teacher_attendance (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT NOT NULL,
    teacher_id  BIGINT NOT NULL,
    date        DATE   NOT NULL,
    studentStatus      ENUM('PRESENT','ABSENT','LATE') NOT NULL,
    remarks     VARCHAR(500),
    CONSTRAINT fk_tatt_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_tatt_teacher  FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    UNIQUE KEY uq_teacher_attendance (school_id, teacher_id, date)
);
*/
-- ============================================================
-- FEES & FINANCE
-- ============================================================
/*CREATE TABLE fee_categories (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    amount      DECIMAL(10,2) NOT NULL,
    description TEXT,
    CONSTRAINT fk_fee_cat_school FOREIGN KEY (school_id) REFERENCES schools(id)
);
*/
/*CREATE TABLE fee_structures (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id        BIGINT       NOT NULL,
    class_id         BIGINT       NOT NULL,
    fee_category_id  BIGINT       NOT NULL,
    academic_year    VARCHAR(20)  NOT NULL,
    due_date         DATE,
    CONSTRAINT fk_fee_str_school    FOREIGN KEY (school_id)       REFERENCES schools(id),
    CONSTRAINT fk_fee_str_class     FOREIGN KEY (class_id)        REFERENCES classes(id),
    CONSTRAINT fk_fee_str_category  FOREIGN KEY (fee_category_id) REFERENCES fee_categories(id)
);
*/
/*CREATE TABLE fee_payments (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id         BIGINT        NOT NULL,
    student_id        BIGINT        NOT NULL,
    fee_structure_id  BIGINT        NOT NULL,
    amount_paid       DECIMAL(10,2) NOT NULL,
    payment_date      DATE          NOT NULL,
    payment_mode      ENUM('CASH','CARD','ONLINE','CHEQUE') NOT NULL DEFAULT 'CASH',
    transaction_id    VARCHAR(200),
    studentStatus            ENUM('PAID','PARTIAL','PENDING','OVERDUE') NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_fee_pay_school      FOREIGN KEY (school_id)        REFERENCES schools(id),
    CONSTRAINT fk_fee_pay_student     FOREIGN KEY (student_id)       REFERENCES students(id),
    CONSTRAINT fk_fee_pay_structure   FOREIGN KEY (fee_structure_id) REFERENCES fee_structures(id)
);*/

-- ============================================================
-- LIBRARY
-- ============================================================
/*CREATE TABLE books (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id        BIGINT       NOT NULL,
    title            VARCHAR(300) NOT NULL,
    author           VARCHAR(200),
    isbn             VARCHAR(50),
    category         VARCHAR(100),
    total_copies     INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    publisher        VARCHAR(200),
    published_year   INT,
    CONSTRAINT fk_books_school FOREIGN KEY (school_id) REFERENCES schools(id)
);
*/
/*CREATE TABLE book_issues (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id    BIGINT NOT NULL,
    book_id      BIGINT NOT NULL,
    student_id   BIGINT NOT NULL,
    issue_date   DATE   NOT NULL,
    due_date     DATE   NOT NULL,
    return_date  DATE,
    fine         DECIMAL(8,2) DEFAULT 0.00,
    studentStatus       ENUM('ISSUED','RETURNED','OVERDUE') NOT NULL DEFAULT 'ISSUED',
    CONSTRAINT fk_bi_school  FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_bi_book    FOREIGN KEY (book_id)    REFERENCES books(id),
    CONSTRAINT fk_bi_student FOREIGN KEY (student_id) REFERENCES students(id)
);
*/
-- ============================================================
-- EXAMS & RESULTS
-- ============================================================
/*CREATE TABLE grade_scale (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id  BIGINT       NOT NULL,
    grade      VARCHAR(10)  NOT NULL,
    min_marks  DECIMAL(5,2) NOT NULL,
    max_marks  DECIMAL(5,2) NOT NULL,
    gpa        DECIMAL(4,2),
    CONSTRAINT fk_grade_school FOREIGN KEY (school_id) REFERENCES schools(id)
);
*/
/*CREATE TABLE exams (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id      BIGINT       NOT NULL,
    name           VARCHAR(200) NOT NULL,
    class_id       BIGINT       NOT NULL,
    subject_id     BIGINT       NOT NULL,
    exam_date      DATE,
    total_marks    DECIMAL(6,2) NOT NULL,
    passing_marks  DECIMAL(6,2) NOT NULL,
    academic_year  VARCHAR(20),
    CONSTRAINT fk_exams_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_exams_class    FOREIGN KEY (class_id)   REFERENCES classes(id),
    CONSTRAINT fk_exams_subject  FOREIGN KEY (subject_id) REFERENCES subjects(id)
);*/

/*CREATE TABLE results (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id       BIGINT       NOT NULL,
    exam_id         BIGINT       NOT NULL,
    student_id      BIGINT       NOT NULL,
    marks_obtained  DECIMAL(6,2) NOT NULL,
    grade           VARCHAR(10),
    remarks         VARCHAR(500),
    studentStatus          ENUM('PASS','FAIL') NOT NULL,
    CONSTRAINT fk_results_school   FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_results_exam     FOREIGN KEY (exam_id)    REFERENCES exams(id),
    CONSTRAINT fk_results_student  FOREIGN KEY (student_id) REFERENCES students(id),
    UNIQUE KEY uq_result (school_id, exam_id, student_id)
);*/

-- ============================================================
-- STAFF / HR
-- ============================================================
/*CREATE TABLE departments (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    CONSTRAINT fk_dept_school FOREIGN KEY (school_id) REFERENCES schools(id)
);
*/
/*CREATE TABLE staff (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id     BIGINT       NOT NULL,
    user_id       BIGINT       NOT NULL,
    first_name    VARCHAR(100) NOT NULL,
    last_name     VARCHAR(100) NOT NULL,
    dob           DATE,
    gender        ENUM('MALE','FEMALE','OTHER'),
    address       TEXT,
    phone         VARCHAR(20),
    email         VARCHAR(100),
    department_id BIGINT,
    designation   VARCHAR(200),
    joining_date  DATE,
    salary        DECIMAL(10,2),
    photo         VARCHAR(500),
    studentStatus        ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_staff_school  FOREIGN KEY (school_id)     REFERENCES schools(id),
    CONSTRAINT fk_staff_user    FOREIGN KEY (user_id)       REFERENCES users(id),
    CONSTRAINT fk_staff_dept    FOREIGN KEY (department_id) REFERENCES departments(id)
);*/

-- Add head_id to departments after staff is created
/*ALTER TABLE departments ADD COLUMN head_id BIGINT;
ALTER TABLE departments ADD CONSTRAINT fk_dept_head FOREIGN KEY (head_id) REFERENCES staff(id);*/

/*CREATE TABLE leave_requests (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id    BIGINT      NOT NULL,
    staff_id     BIGINT      NOT NULL,
    leave_type   VARCHAR(50) NOT NULL,
    from_date    DATE        NOT NULL,
    to_date      DATE        NOT NULL,
    reason       TEXT,
    studentStatus       ENUM('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING',
    approved_by  BIGINT,
    CONSTRAINT fk_leave_school      FOREIGN KEY (school_id)  REFERENCES schools(id),
    CONSTRAINT fk_leave_staff       FOREIGN KEY (staff_id)   REFERENCES staff(id),
    CONSTRAINT fk_leave_approver    FOREIGN KEY (approved_by) REFERENCES users(id)
);
*/
/*CREATE TABLE payroll (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id     BIGINT        NOT NULL,
    staff_id      BIGINT        NOT NULL,
    month         INT           NOT NULL,
    year          INT           NOT NULL,
    basic_salary  DECIMAL(10,2) NOT NULL,
    allowances    DECIMAL(10,2) DEFAULT 0.00,
    deductions    DECIMAL(10,2) DEFAULT 0.00,
    net_salary    DECIMAL(10,2) NOT NULL,
    paid_date     DATE,
    CONSTRAINT fk_payroll_school FOREIGN KEY (school_id) REFERENCES schools(id),
    CONSTRAINT fk_payroll_staff  FOREIGN KEY (staff_id)  REFERENCES staff(id),
    UNIQUE KEY uq_payroll (school_id, staff_id, month, year)
);
*/
-- ============================================================
-- NOTIFICATIONS
-- ============================================================
/*CREATE TABLE notifications (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    title       VARCHAR(200) NOT NULL,
    message     TEXT         NOT NULL,
    is_read     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_school FOREIGN KEY (school_id) REFERENCES schools(id),
    CONSTRAINT fk_notif_user   FOREIGN KEY (user_id)   REFERENCES users(id)
);*/

-- ============================================================
-- ACTIVITY LOGS
-- ============================================================
/*CREATE TABLE activity_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    school_id   BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    action      VARCHAR(50)  NOT NULL,   -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT
    module      VARCHAR(50)  NOT NULL,   -- STUDENT, TEACHER, FEES, EXAM, etc.
    description TEXT,
    ip_address  VARCHAR(50),
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_log_school FOREIGN KEY (school_id) REFERENCES schools(id),
    CONSTRAINT fk_log_user   FOREIGN KEY (user_id)   REFERENCES users(id)
);
*/
-- ============================================================
-- SEED DATA — default roles
-- ============================================================
/*INSERT INTO roles (name) VALUES ('ADMIN'), ('TEACHER'), ('STUDENT'), ('PARENT'), ('STAFF');
*/
/*-- Seed school
INSERT INTO schools (name, code, address, phone, email, studentStatus)
VALUES ('Demo School', 'DEMO001', '123 Main Street, City', '+1-555-0100', 'admin@demoschool.com', 'ACTIVE');

-- Seed admin user (password stored as plain text — JWT auth added later)
INSERT INTO users (school_id, username, password, role, is_active)
VALUES (1, 'admin', 'admin123', 'ADMIN', TRUE);*/