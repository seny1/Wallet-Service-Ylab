CREATE TABLE entities.client (id SERIAL PRIMARY KEY, login VARCHAR(255) UNIQUE, password VARCHAR(255), balance INT);
