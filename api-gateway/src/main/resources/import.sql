CREATE SCHEMA IF NOT EXISTS authservice;

SET SCHEMA 'authservice';

INSERT INTO users(id, username, password, role)
VALUES (1, 'doctor1', '$2a$12$RpPOn3wx.CUqRKWDdXNHS.YZnvVGI4hXoI3zRH9rsUFtIVYRacN1a', 'DOCTOR');
INSERT INTO users(id, username, password, role)
VALUES (2, 'doctor2', '$2a$12$RpPOn3wx.CUqRKWDdXNHS.YZnvVGI4hXoI3zRH9rsUFtIVYRacN1a', 'DOCTOR');
INSERT INTO users(id, username, password, role)
VALUES (3, 'patient1', '$2a$12$RpPOn3wx.CUqRKWDdXNHS.YZnvVGI4hXoI3zRH9rsUFtIVYRacN1a', 'PATIENT');
INSERT INTO users(id, username, password, role)
VALUES (4, 'patient2', '$2a$12$RpPOn3wx.CUqRKWDdXNHS.YZnvVGI4hXoI3zRH9rsUFtIVYRacN1a', 'PATIENT');
