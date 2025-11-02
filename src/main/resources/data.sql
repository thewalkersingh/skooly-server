-- ====== TEACHERS ======
INSERT INTO teachers (id, employee_code, name, email, phone)
VALUES (1, 'T001', 'Amit Sharma', 'amit@school.com', '9876543210'),
       (2, 'T002', 'Neha Verma', 'neha@school.com', '9876543211'),
       (3, 'T003', 'Ravi Kumar', 'ravi@school.com', '9876543212');

-- ====== PARENTS ======
INSERT INTO parents (id, name, email, phone)
VALUES (1, 'Suresh Singh', 'suresh.parent@gmail.com', '9000011111'),
       (2, 'Anita Gupta', 'anita.parent@gmail.com', '9000011112'),
       (3, 'Rajesh Mehta', 'rajesh.parent@gmail.com', '9000011113');

-- ====== CLASSES ======
INSERT INTO school_class (id, grade, section, teacher_id)
VALUES (1, '10', 'A', 1),
       (2, '9', 'B', 2),
       (3, '8', 'C', 3);

-- ====== STUDENTS ======
INSERT INTO students (id, admission_number, first_name, last_name, age, email, phone, address, city, state, zip,
                      country, class_id, parent_id)
VALUES (1, 'S001', 'Rohan', 'Singh', 12, 'rohan.singh@gmail.com', '9011110001', '12 MG Road', 'Delhi', 'Delhi',
        '110001', 'India', 1, 1),
       (2, 'S002', 'Priya', 'Sharma', 14, 'priya.sharma@gmail.com', '9011110002', '45 Park Street', 'Kolkata',
        'WB', '700016', 'India', 1, 1),
       (3, 'S003', 'Aman', 'Verma', 12, 'aman.verma@gmail.com', '9011110003', '22 Lajpat Nagar', 'Delhi', 'Delhi',
        '110024', 'India', 2, 2),
       (4, 'S004', 'Sneha', 'Gupta', 13, 'sneha.gupta@gmail.com', '9011110004', '33 Lake Road', 'Kolkata', 'WB',
        '700029', 'India', 2, 2),
       (5, 'S005', 'Karan', 'Mehta', 12, 'karan.mehta@gmail.com', '9011110005', '5 Civil Lines', 'Mumbai', 'MH',
        '400001', 'India', 3, 3),
       (6, 'S006', 'Anjali', 'Patel', 15, 'anjali.patel@gmail.com', '9011110006', '8 MG Road', 'Pune', 'MH',
        '411001', 'India', 3, 3);

-- ====== ATTENDANCE ======
INSERT INTO attendances (id, date, status, student_id)
VALUES (1, '2025-10-20', 'PRESENT', 1),
       (2, '2025-10-20', 'PRESENT', 2),
       (3, '2025-10-20', 'ABSENT', 3),
       (4, '2025-10-20', 'LEAVE', 4),
       (5, '2025-10-20', 'PRESENT', 5),
       (6, '2025-10-20', 'ABSENT', 6),
       (7, '2025-10-21', 'PRESENT', 1),
       (8, '2025-10-21', 'LEAVE', 2),
       (9, '2025-10-21', 'PRESENT', 3),
       (10, '2025-10-21', 'ABSENT', 4);
