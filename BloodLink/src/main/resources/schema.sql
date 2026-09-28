CREATE DATABASE IF NOT EXISTS bloodlink_db;
USE bloodlink_db;

CREATE TABLE blood_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE donor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    city VARCHAR(100) NOT NULL,
    blood_group VARCHAR(10) NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    last_donation_date DATE NULL
);

CREATE TABLE donation_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    donation_date DATE NOT NULL,
    FOREIGN KEY (donor_id) REFERENCES donor(id) ON DELETE CASCADE
);
