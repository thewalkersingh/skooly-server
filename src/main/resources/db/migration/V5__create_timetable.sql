CREATE TABLE rooms (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    type        ENUM('CLASSROOM', 'LAB', 'LIBRARY', 'OFFICE', 'SPORTS', 'OTHER'),
    capacity    INT,
    floor       VARCHAR(50),
    building    VARCHAR(100),
    status      ENUM('AVAILABLE', 'OCCUPIED', 'UNDER_MAINTENANCE') DEFAULT 'AVAILABLE',
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
);CREATE TABLE rooms (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    type        ENUM('CLASSROOM', 'LAB', 'LIBRARY', 'OFFICE', 'SPORTS', 'OTHER'),
    capacity    INT,
    floor       VARCHAR(50),
    building    VARCHAR(100),
    status      ENUM('AVAILABLE', 'OCCUPIED', 'UNDER_MAINTENANCE') DEFAULT 'AVAILABLE',
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