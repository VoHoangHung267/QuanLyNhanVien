-- ============================================================
-- HỆ THỐNG QUẢN LÝ NHÂN VIÊN - DATABASE SCHEMA
-- ============================================================

CREATE DATABASE IF NOT EXISTS employee_management
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE employee_management;

-- ============================================================
-- Bảng user: tài khoản đăng nhập
-- ============================================================
CREATE TABLE `user` (
  `id`       BIGINT       NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50)  NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `role`     ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  `created_at` DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng employee: thông tin nhân viên
-- ============================================================
CREATE TABLE `employee` (
  `id`      BIGINT       NOT NULL AUTO_INCREMENT,
  `name`    VARCHAR(100) NOT NULL,
  `dob`     DATE         NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `phone`   VARCHAR(15)  NOT NULL,
  `created_at` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng language: danh mục ngoại ngữ và trình độ
-- Anh: A, B, C  |  Nhật: N1, N2, N3, N4, N5
-- ============================================================
CREATE TABLE `language` (
  `id`    BIGINT      NOT NULL AUTO_INCREMENT,
  `name`  VARCHAR(50) NOT NULL COMMENT 'Ví dụ: Tiếng Anh, Tiếng Nhật',
  `level` VARCHAR(10) NOT NULL COMMENT 'Ví dụ: A, B, C, N1, N2...',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_language_name_level` (`name`, `level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng employee_language: nhân viên - ngoại ngữ (N-N)
-- ============================================================
CREATE TABLE `employee_language` (
  `id`          BIGINT NOT NULL AUTO_INCREMENT,
  `employee_id` BIGINT NOT NULL,
  `language_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_emp_lang` (`employee_id`, `language_id`),
  CONSTRAINT `fk_el_employee` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_el_language` FOREIGN KEY (`language_id`) REFERENCES `language` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng certificate: danh mục chứng chỉ (PMP, Agile...)
-- ============================================================
CREATE TABLE `certificate` (
  `id`   BIGINT      NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Bảng employee_certificate: nhân viên - chứng chỉ (N-N)
-- ============================================================
CREATE TABLE `employee_certificate` (
  `id`             BIGINT NOT NULL AUTO_INCREMENT,
  `employee_id`    BIGINT NOT NULL,
  `certificate_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_emp_cert` (`employee_id`, `certificate_id`),
  CONSTRAINT `fk_ec_employee`    FOREIGN KEY (`employee_id`)    REFERENCES `employee` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_ec_certificate` FOREIGN KEY (`certificate_id`) REFERENCES `certificate` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- DATA MẪU
-- ============================================================

-- User (password: Admin@123 - BCrypt encoded)
INSERT INTO `user` (`username`, `password`, `role`) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
('user1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'USER');

-- Language catalogue
INSERT INTO `language` (`name`, `level`) VALUES
('Tiếng Anh', 'A'), ('Tiếng Anh', 'B'), ('Tiếng Anh', 'C'),
('Tiếng Nhật', 'N1'), ('Tiếng Nhật', 'N2'), ('Tiếng Nhật', 'N3'),
('Tiếng Nhật', 'N4'), ('Tiếng Nhật', 'N5');

-- Certificate catalogue
INSERT INTO `certificate` (`name`) VALUES ('PMP'), ('Agile'), ('AWS'), ('ITIL');

-- Sample employees
INSERT INTO `employee` (`name`, `dob`, `address`, `phone`) VALUES
('Nguyễn Văn An',  '1990-05-15', 'Hà Nội',         '0912345678'),
('Trần Thị Bình',  '1995-08-20', 'TP. Hồ Chí Minh','0987654321'),
('Lê Minh Cường',  '1988-12-01', 'Đà Nẵng',        '0934567890');

-- Employee languages
INSERT INTO `employee_language` (`employee_id`, `language_id`) VALUES
(1, 2), -- An: Tiếng Anh B
(1, 5), -- An: Tiếng Nhật N2
(2, 3), -- Bình: Tiếng Anh C
(3, 1); -- Cường: Tiếng Anh A

-- Employee certificates
INSERT INTO `employee_certificate` (`employee_id`, `certificate_id`) VALUES
(1, 1), -- An: PMP
(1, 2), -- An: Agile
(2, 2), -- Bình: Agile
(3, 1); -- Cường: PMP