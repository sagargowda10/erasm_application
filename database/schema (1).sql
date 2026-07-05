-- ============================================================
-- ERASM - Enterprise Resource Allocation & Skill Management
-- Database Creation Script (MySQL 8+)
-- ------------------------------------------------------------
-- Column names match Hibernate's default snake_case mapping
-- of the JPA entity fields. Tables are ordered by foreign-key
-- dependency so the script runs top-to-bottom cleanly.
-- ============================================================

CREATE DATABASE IF NOT EXISTS erasm_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE erasm_db;

-- Drop in reverse dependency order so re-runs don't hit FK errors
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS allocations;
DROP TABLE IF EXISTS resource_requests;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS certifications;
DROP TABLE IF EXISTS employee_skills;
DROP TABLE IF EXISTS skills;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;

-- ------------------------------------------------------------
-- 1. roles
-- ------------------------------------------------------------
CREATE TABLE roles (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- 2. users  (Many-to-One -> roles)
-- ------------------------------------------------------------
CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id  INT NOT NULL,
    CONSTRAINT fk_users_role
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- ------------------------------------------------------------
-- 3. employees  (One-to-One -> users)
-- ------------------------------------------------------------
CREATE TABLE employees (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    designation VARCHAR(255),
    experience  INT,
    user_id     INT NOT NULL UNIQUE,
    CONSTRAINT fk_employees_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

-- ------------------------------------------------------------
-- 4. skills
-- ------------------------------------------------------------
CREATE TABLE skills (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- 5. employee_skills  (Many-to-Many bridge: employees <-> skills)
-- ------------------------------------------------------------
CREATE TABLE employee_skills (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    skill_id    INT NOT NULL,
    skill_level VARCHAR(255),
    experience  INT,
    CONSTRAINT fk_empskill_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_empskill_skill
        FOREIGN KEY (skill_id) REFERENCES skills (id)
);

-- ------------------------------------------------------------
-- 6. certifications  (Many-to-One -> employees)
-- ------------------------------------------------------------
CREATE TABLE certifications (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    issued_date DATE,
    employee_id INT NOT NULL,
    CONSTRAINT fk_certification_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
);

-- ------------------------------------------------------------
-- 7. projects
-- ------------------------------------------------------------
CREATE TABLE projects (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    project_name     VARCHAR(255) NOT NULL,
    client_name      VARCHAR(255),
    start_date       DATE,
    end_date         DATE,
    technology_stack VARCHAR(255),
    budget           DOUBLE
);

-- ------------------------------------------------------------
-- 8. resource_requests  (Many-to-One -> projects)
-- ------------------------------------------------------------
CREATE TABLE resource_requests (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    required_skill VARCHAR(255),
    required_count INT,
    status         VARCHAR(255),
    project_id     INT NOT NULL,
    CONSTRAINT fk_reqrequest_project
        FOREIGN KEY (project_id) REFERENCES projects (id)
);

-- ------------------------------------------------------------
-- 9. allocations  (Many-to-One -> employees, projects)
-- ------------------------------------------------------------
CREATE TABLE allocations (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    employee_id           INT NOT NULL,
    project_id            INT NOT NULL,
    allocation_percentage INT,
    status                VARCHAR(255),
    CONSTRAINT fk_allocation_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id),
    CONSTRAINT fk_allocation_project
        FOREIGN KEY (project_id) REFERENCES projects (id)
);

-- ------------------------------------------------------------
-- 10. audit_logs
-- ------------------------------------------------------------
CREATE TABLE audit_logs (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    action        VARCHAR(255),
    entity_name   VARCHAR(255),
    performed_by  VARCHAR(255),
    created_date  DATETIME,
    modified_date DATETIME
);

-- ------------------------------------------------------------
-- Helpful indexes for common lookups
-- ------------------------------------------------------------
CREATE INDEX idx_users_email             ON users (email);
CREATE INDEX idx_empskill_employee       ON employee_skills (employee_id);
CREATE INDEX idx_certification_employee  ON certifications (employee_id);
CREATE INDEX idx_allocation_employee     ON allocations (employee_id);
CREATE INDEX idx_allocation_project      ON allocations (project_id);
CREATE INDEX idx_reqrequest_project      ON resource_requests (project_id);

-- ============================================================
-- Seed data: the five roles the RBAC layer expects.
-- Names are stored WITHOUT the ROLE_ prefix to match the
-- @PreAuthorize("hasRole('ADMIN')") style checks in the code.
-- ============================================================
INSERT INTO roles (name) VALUES
    ('ADMIN'),
    ('DELIVERY_MANAGER'),
    ('RESOURCE_MANAGER'),
    ('EMPLOYEE'),
    ('AUDITOR');

-- End of script
