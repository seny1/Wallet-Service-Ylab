CREATE SCHEMA entities;
CREATE TABLE entities.transaction (id SERIAL PRIMARY KEY, type VARCHAR(255) NOT NULL, amount INT);
