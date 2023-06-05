CREATE SCHEMA IF NOT EXISTS userservice;

SET SCHEMA 'userservice';

INSERT INTO doctors(doctor_id, first_name, last_name, email, rating, user_id, role)
VALUES (1, 'doctor1', '', null, 0, 1, 'DOCTOR');
INSERT INTO doctors(doctor_id, first_name, last_name, email, rating, user_id, role)
VALUES (2, 'doctor2', '', null, 0, 2, 'DOCTOR');

INSERT INTO patients(patient_id, first_name, last_name, email, health_insurance_number, user_id, role)
VALUES (1, 'patient1', '', '', 123455, 3, 'PATIENT');
INSERT INTO patients(patient_id, first_name, last_name, email, health_insurance_number, user_id, role)
VALUES (2, 'patient2', '', '', 123456, 4, 'PATIENT');

INSERT INTO medical_services
VALUES (1, 'service1');
INSERT INTO medical_services
VALUES (2, 'service2');
INSERT INTO medical_services
VALUES (3, 'service3');
INSERT INTO medical_services
VALUES (4, 'service4');

INSERT INTO doctors_services
VALUES (1, 1);
INSERT INTO doctors_services
VALUES (1, 2);
INSERT INTO doctors_services
VALUES (1, 3);
INSERT INTO doctors_services
VALUES (2, 1);
INSERT INTO doctors_services
VALUES (2, 4);