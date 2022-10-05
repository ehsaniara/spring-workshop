CREATE SEQUENCE car_id_seq;
alter table inventory.cars
    alter column id set default nextval('car_id_seq');
