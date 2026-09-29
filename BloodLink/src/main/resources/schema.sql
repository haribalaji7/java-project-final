CREATE DATABASE IF NOT EXISTS bloodlink_db;
USE bloodlink_db;

DROP TABLE IF EXISTS donation_record;
DROP TABLE IF EXISTS donor;
DROP TABLE IF EXISTS blood_group;

CREATE TABLE blood_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) NOT NULL UNIQUE
);

CREATE TABLE donor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    city VARCHAR(100) NOT NULL,
    blood_group_id BIGINT NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    last_donation_date DATE NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (blood_group_id) REFERENCES blood_group(id)
);

CREATE TABLE donation_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donor_id BIGINT NOT NULL,
    donation_date DATE NOT NULL,
    hospital_name VARCHAR(100),
    doctor_name VARCHAR(100),
    units_donated INT,
    remarks VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_id) REFERENCES donor(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_donor_city ON donor(city);
CREATE INDEX idx_donor_available ON donor(available);
CREATE INDEX idx_donor_blood_group ON donor(blood_group_id);
CREATE INDEX idx_donation_date ON donation_record(donation_date);
