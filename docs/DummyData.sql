USE skooly_db;

INSERT INTO schools (name, code, address, phone, email, logo, studentStatus, created_at) VALUES
('Sunrise Public School', 'SCH001', 'Hyderabad, Telangana', '0401234567', 'contact@sunrise.edu', 'sunrise_logo.png', 'ACTIVE', NOW()),
('Green Valley High', 'SCH002', 'Delhi, India', '0112345678', 'info@greenvalley.edu', 'greenvalley_logo.png', 'ACTIVE', NOW()),
('Bluebell International', 'SCH003', 'Mumbai, Maharashtra', '0229876543', 'admin@bluebell.edu', 'bluebell_logo.png', 'ACTIVE', NOW()),
('Silver Oak Academy', 'SCH004', 'Ahmedabad, Gujarat', '0791112233', 'office@silveroak.edu', 'silveroak_logo.png', 'ACTIVE', NOW()),
('Lotus Convent School', 'SCH005', 'Chennai, Tamil Nadu', '0447654321', 'lotus@convent.edu', 'lotus_logo.png', 'INACTIVE', NOW()),
('Bright Future School', 'SCH006', 'Pune, Maharashtra', '0209988776', 'brightfuture@school.edu', 'brightfuture_logo.png', 'ACTIVE', NOW()),
('Harmony Kids Academy', 'SCH007', 'Kolkata, West Bengal', '0335566778', 'harmony@kids.edu', 'harmony_logo.png', 'ACTIVE', NOW()),
('Evergreen High School', 'SCH008', 'Bengaluru, Karnataka', '0802233445', 'evergreen@school.edu', 'evergreen_logo.png', 'ACTIVE', NOW()),
('Starlight Public School', 'SCH009', 'Lucknow, Uttar Pradesh', '0522123456', 'starlight@school.edu', 'starlight_logo.png', 'INACTIVE', NOW()),
('Riverdale Academy', 'SCH010', 'Jaipur, Rajasthan', '0141122334', 'riverdale@academy.edu', 'riverdale_logo.png', 'ACTIVE', NOW());

-- INSERT INTO roles (id, name) VALUES
-- (1, 'ADMIN'),
-- (2, 'TEACHER'),
-- (3, 'STUDENT'),
-- (4, 'PARENT'),
-- (5, 'STAFF'),
-- (6, 'LIBRARIAN'),
-- (7, 'ACCOUNTANT'),
-- (8, 'PRINCIPAL'),
-- (9, 'VICE_PRINCIPAL'),
-- (10, 'COUNSELOR');


INSERT INTO users (school_id, username, password, role, is_active, created_at) VALUES
(1, 'admin1', 'bcrypt_hash_here', 'ADMIN', TRUE, NOW()),
(1, 'teacher1', 'bcrypt_hash_here', 'TEACHER', TRUE, NOW()),
(1, 'student1', 'bcrypt_hash_here', 'STUDENT', TRUE, NOW()),
(1, 'parent1', 'bcrypt_hash_here', 'PARENT', TRUE, NOW()),
(1, 'staff1', 'bcrypt_hash_here', 'STAFF', TRUE, NOW()),
(1, 'teacher2', 'bcrypt_hash_here', 'TEACHER', TRUE, NOW()),
(1, 'student2', 'bcrypt_hash_here', 'STUDENT', TRUE, NOW()),
(1, 'admin2', 'bcrypt_hash_here', 'ADMIN', TRUE, NOW()),
(1, 'teacher3', 'bcrypt_hash_here', 'TEACHER', TRUE, NOW()),
(1, 'student3', 'bcrypt_hash_here', 'STUDENT', TRUE, NOW());


INSERT INTO classes (school_id, name, grade_level) VALUES
(1, 'Class 1A', 1),
(1, 'Class 2A', 2),
(1, 'Class 3A', 3),
(1, 'Class 4A', 4),
(1, 'Class 5A', 5),
(1, 'Class 6A', 6),
(1, 'Class 7A', 7),
(1, 'Class 8A', 8),
(1, 'Class 9A', 9),
(1, 'Class 10A', 10);


INSERT INTO subjects (school_id, name, code, description) VALUES
(1, 'Mathematics', 'MATH101', 'Basic Mathematics'),
(1, 'English', 'ENG101', 'English Language'),
(1, 'Science', 'SCI101', 'General Science'),
(1, 'History', 'HIS101', 'World History'),
(1, 'Geography', 'GEO101', 'Physical Geography'),
(1, 'Physics', 'PHY101', 'Fundamentals of Physics'),
(1, 'Chemistry', 'CHEM101', 'Intro to Chemistry'),
(1, 'Biology', 'BIO101', 'Life Sciences'),
(1, 'Computer Science', 'CS101', 'Programming Basics'),
(1, 'Economics', 'ECO101', 'Principles of Economics');


INSERT INTO teachers (school_id, user_id, first_name, last_name, dob, gender, address, phone, email, joining_date, subject_id, qualification, experience, photo, studentStatus) VALUES
(1, 2, 'Ravi', 'Kumar', '1980-05-12', 'MALE', 'Hyderabad', '9000000001', 'ravi.kumar@school1.edu', '2015-06-01', 1, 'M.Sc Mathematics', 10, 'ravi.jpg', 'ACTIVE'),
(1, 6, 'Anita', 'Sharma', '1985-08-20', 'FEMALE', 'Delhi', '9000000002', 'anita.sharma@school2.edu', '2016-07-15', 2, 'M.A English', 8, 'anita.jpg', 'ACTIVE'),
(1, 9, 'Rajesh', 'Patel', '1979-03-10', 'MALE', 'Ahmedabad', '9000000003', 'rajesh.patel@school3.edu', '2014-05-20', 3, 'M.Sc Science', 12, 'rajesh.jpg', 'ACTIVE'),
(1, 2, 'Meena', 'Verma', '1982-11-25', 'FEMALE', 'Mumbai', '9000000004', 'meena.verma@school4.edu', '2017-09-01', 4, 'M.A History', 9, 'meena.jpg', 'ACTIVE'),
(1, 6, 'Suresh', 'Rao', '1981-07-15', 'MALE', 'Chennai', '9000000005', 'suresh.rao@school5.edu', '2018-01-10', 5, 'M.A Geography', 7, 'suresh.jpg', 'ACTIVE'),
(1, 9, 'Priya', 'Nair', '1983-02-20', 'FEMALE', 'Pune', '9000000006', 'priya.nair@school6.edu', '2019-03-05', 6, 'M.Sc Physics', 6, 'priya.jpg', 'ACTIVE'),
(1, 2, 'Arjun', 'Singh', '1984-09-12', 'MALE', 'Kolkata', '9000000007', 'arjun.singh@school7.edu', '2020-04-01', 7, 'M.Sc Chemistry', 5, 'arjun.jpg', 'ACTIVE'),
(1, 6, 'Neha', 'Gupta', '1986-12-01', 'FEMALE', 'Bengaluru', '9000000008', 'neha.gupta@school8.edu', '2021-05-15', 8, 'M.Sc Biology', 4, 'neha.jpg', 'ACTIVE'),
(1, 9, 'Vikram', 'Joshi', '1987-08-25', 'MALE', 'Lucknow', '9000000009', 'vikram.joshi@school9.edu', '2022-06-20', 9, 'MCA Computer Science', 3, 'vikram.jpg', 'ACTIVE'),
(1, 2, 'Kavita', 'Desai', '1988-11-10', 'FEMALE', 'Jaipur', '9000000010', 'kavita.desai@school10.edu', '2023-07-30', 10, 'M.A Economics', 2, 'kavita.jpg', 'ACTIVE');


INSERT INTO sections (school_id, class_id, name, teacher_id, capacity) VALUES
(1, 1, 'Section A', 1, 40),
(1, 2, 'Section A', 2, 40),
(1, 3, 'Section A', 3, 40),
(1, 4, 'Section A', 4, 40),
(1, 5, 'Section A', 5, 40),
(1, 6, 'Section A', 6, 40),
(1, 7, 'Section A', 7, 40),
(1, 8, 'Section A', 8, 40),
(1, 9, 'Section A', 9, 40),
(1, 10, 'Section A', 10, 40);


INSERT INTO parents (school_id, user_id, first_name, last_name, phone, email, address, occupation, relation)VALUES
(1, 4, 'Ramesh', 'Sharma', '9010000001', 'ramesh.sharma@parent1.edu', 'Hyderabad', 'Engineer', 'Father'),
(1, 5, 'Sunita', 'Patel', '9010000002', 'sunita.patel@parent2.edu', 'Delhi', 'Teacher', 'Mother'),
(1, 4, 'Mahesh', 'Verma', '9010000003', 'mahesh.verma@parent3.edu', 'Ahmedabad', 'Doctor', 'Father'),
(1, 5, 'Anjali', 'Rao', '9010000004', 'anjali.rao@parent4.edu', 'Mumbai', 'Lawyer', 'Mother'),
(1, 4, 'Suresh', 'Nair', '9010000005', 'suresh.nair@parent5.edu', 'Chennai', 'Businessman', 'Father'),
(1, 5, 'Meena', 'Singh', '9010000006', 'meena.singh@parent6.edu', 'Pune', 'Nurse', 'Mother'),
(1, 4, 'Arvind', 'Joshi', '9010000007', 'arvind.joshi@parent7.edu', 'Kolkata', 'Accountant', 'Father'),
(1, 5, 'Neelam', 'Gupta', '9010000008', 'neelam.gupta@parent8.edu', 'Bengaluru', 'Professor', 'Mother'),
(1, 4, 'Vijay', 'Desai', '9010000009', 'vijay.desai@parent9.edu', 'Lucknow', 'Banker', 'Father'),
(1, 5, 'Kavita', 'Mehta', '9010000010', 'kavita.mehta@parent10.edu', 'Jaipur', 'Architect', 'Mother');


INSERT INTO students (school_id, user_id, first_name, last_name, dob, gender, address, phone, email,
    admission_date, class_id, section_id, parent_id, photo, studentStatus) VALUES
(1, 1, 'Amit', 'Sharma', '2010-05-12', 'MALE', 'Hyderabad', '9001000001', 'amit.sharma@student1.edu', '2020-06-01', 1, 1, 1, 'amit.jpg', 'ACTIVE'),
(1, 2, 'Sneha', 'Patel', '2011-08-20', 'FEMALE', 'Delhi', '9001000002', 'sneha.patel@student2.edu', '2020-07-15', 2, 2, 2, 'sneha.jpg', 'ACTIVE'),
(1, 3, 'Rahul', 'Verma', '2009-03-10', 'MALE', 'Ahmedabad', '9001000003', 'rahul.verma@student3.edu', '2019-05-20', 3, 3, 3, 'rahul.jpg', 'ACTIVE'),
(1, 4, 'Priya', 'Rao', '2012-11-25', 'FEMALE', 'Mumbai', '9001000004', 'priya.rao@student4.edu', '2021-09-01', 4, 4, 4, 'priya.jpg', 'ACTIVE'),
(1, 5, 'Karan', 'Nair', '2010-07-15', 'MALE', 'Chennai', '9001000005', 'karan.nair@student5.edu', '2020-01-10', 5, 5, 5, 'karan.jpg', 'ACTIVE'),
(1, 6, 'Meera', 'Singh', '2011-02-20', 'FEMALE', 'Pune', '9001000006', 'meera.singh@student6.edu', '2021-03-05', 6, 6, 6, 'meera.jpg', 'ACTIVE'),
(1, 7, 'Arjun', 'Joshi', '2009-09-12', 'MALE', 'Kolkata', '9001000007', 'arjun.joshi@student7.edu', '2019-04-01', 7, 7, 7, 'arjun.jpg', 'ACTIVE'),
(1, 8, 'Neha', 'Gupta', '2012-12-01', 'FEMALE', 'Bengaluru', '9001000008', 'neha.gupta@student8.edu', '2021-05-15', 8, 8, 8, 'neha.jpg', 'ACTIVE'),
(1, 9, 'Vikram', 'Desai', '2010-08-25', 'MALE', 'Lucknow', '9001000009', 'vikram.desai@student9.edu', '2020-06-20', 9, 9, 9, 'vikram.jpg', 'ACTIVE'),
(1, 10, 'Kavita', 'Mehta', '2011-11-10', 'FEMALE', 'Jaipur', '9001000010', 'kavita.mehta@student10.edu', '2021-07-30', 10, 10, 10, 'kavita.jpg', 'ACTIVE');


INSERT INTO books (school_id, title, author, isbn, category, total_copies, available_copies, publisher, published_year) VALUES
(1,'Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'Computer Science', 5, 5, 'MIT Press', 2009),
(1, 'The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'Fiction', 3, 2, 'Scribner', 1925),
(1, 'Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 4, 4, 'Prentice Hall', 2008),
(1, 'A Brief History of Time', 'Stephen Hawking', '9780553380163', 'Science', 6, 6, 'Bantam Books', 1988),
(1, 'The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Programming', 2, 1, 'Addison-Wesley', 1999),
(1, 'To Kill a Mockingbird', 'Harper Lee', '9780061120084', 'Fiction', 5, 3, 'J.B. Lippincott & Co.', 1960),
(1, 'Database System Concepts', 'Abraham Silberschatz', '9780073523323', 'Computer Science', 4, 4, 'McGraw-Hill', 2010),
(1, 'The Lean Startup', 'Eric Ries', '9780307887894', 'Business', 3, 3, 'Crown Publishing', 2011),
(1, 'The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 'Fiction', 2, 2, 'Little, Brown and Company', 1951),
(1, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', '9780136042594', 'Computer Science', 5, 5, 'Pearson', 2010);

INSERT INTO book_issues (book_id, student_id, school_id, issue_date, due_date, return_date, fine, studentStatus) VALUES
(1, 1, 1, '2026-03-01', '2026-03-15', '2026-03-14', 0.00, 'RETURNED'),
(2, 2, 1, '2026-03-05', '2026-03-19', NULL, 0.00, 'ISSUED'),
(3, 3, 1, '2026-02-20', '2026-03-06', '2026-03-10', 50.00, 'OVERDUE'),
(4, 4, 1, '2026-03-10', '2026-03-24', NULL, 0.00, 'ISSUED'),
(5, 5, 1, '2026-02-25', '2026-03-11', '2026-03-11', 0.00, 'RETURNED'),
(6, 6, 1, '2026-03-12', '2026-03-26', NULL, 0.00, 'ISSUED'),
(7, 7, 1, '2026-02-15', '2026-03-01', '2026-03-05', 30.00, 'OVERDUE'),
(8, 8, 1, '2026-03-08', '2026-03-22', '2026-03-20', 0.00, 'RETURNED'),
(9, 9, 1, '2026-03-14', '2026-03-28', NULL, 0.00, 'ISSUED'),
(10, 1, 1, '2026-02-28', '2026-03-14', '2026-03-16', 20.00, 'OVERDUE');

INSERT INTO attendance (school_id, student_id, class_id, date, attendance_status, marked_by, remarks) VALUES
(1, 1, 1, '2026-04-01', 'PRESENT', 1, 'On time'),
(1 ,2, 1, '2026-04-01', 'ABSENT', 1, 'Sick leave'),
(1, 3, 2, '2026-04-01', 'LATE', 2, 'Arrived 10 minutes late'),
(1, 4, 2, '2026-04-01', 'PRESENT', 2, 'Participated actively'),
(1, 5, 3, '2026-04-01', 'HALF_DAY', 3, 'Left after lunch'),
(1, 6, 3, '2026-04-01', 'HOLIDAY', 3, 'National holiday'),
(1, 7, 4, '2026-04-02', 'PRESENT', 4, 'Good performance'),
(1, 8, 4, '2026-04-02', 'ABSENT', 4, 'Family emergency'),
(1, 9, 5, '2026-04-02', 'PRESENT', 5, 'Excellent participation'),
(1, 1, 5, '2026-04-02', 'LATE', 5, 'Traffic delay');