-- V2__Optimize_Schema.sql
-- Migration-safe SQL script to optimize the MySQL database schema.

-- 1. Optimize column lengths
-- Standard E.164 phone numbers have a maximum of 15 characters.
ALTER TABLE donor MODIFY COLUMN phone VARCHAR(15) NOT NULL;

-- 2. Migrate donor table to use foreign key blood_group_id instead of string blood_group
ALTER TABLE donor ADD COLUMN blood_group_id BIGINT;

-- Update the new column with corresponding IDs from the blood_group table
UPDATE donor d 
JOIN blood_group bg ON d.blood_group = bg.name 
SET d.blood_group_id = bg.id;

-- Enforce NOT NULL on the new foreign key and drop the old string column
ALTER TABLE donor MODIFY COLUMN blood_group_id BIGINT NOT NULL;
ALTER TABLE donor DROP COLUMN blood_group;

-- 3. Add Foreign Key constraints
-- Link donor to blood_group table securely
ALTER TABLE donor ADD CONSTRAINT fk_donor_blood_group 
    FOREIGN KEY (blood_group_id) REFERENCES blood_group(id);

-- 4. Add Indexes for faster lookup
-- Index on city for location-based searches
CREATE INDEX idx_donor_city ON donor(city);

-- Index on blood_group_id for blood type filtering
CREATE INDEX idx_donor_blood_group_id ON donor(blood_group_id);

-- 5. Add Unique constraint for phone number
-- Prevents duplicate donor registrations with the same phone number
ALTER TABLE donor ADD CONSTRAINT uk_donor_phone UNIQUE (phone);

-- 6. Add Timestamps (created_at, updated_at) to all tables
-- blood_group timestamps
ALTER TABLE blood_group
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- donor timestamps
ALTER TABLE donor
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- donation_record timestamps
ALTER TABLE donation_record
ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

-- 7. Add new fields to donation_record (if missing)
ALTER TABLE donation_record
ADD COLUMN hospital_name VARCHAR(100),
ADD COLUMN doctor_name VARCHAR(100),
ADD COLUMN units_donated INT,
ADD COLUMN remarks VARCHAR(500);
