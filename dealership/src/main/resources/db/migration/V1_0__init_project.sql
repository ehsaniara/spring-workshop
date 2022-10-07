CREATE SCHEMA IF NOT EXISTS inventory;

CREATE TABLE IF NOT EXISTS inventory.cars
(
    id       INTEGER NOT NULL PRIMARY KEY,
    car_type VARCHAR(20),
    name     VARCHAR(100)
);
