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