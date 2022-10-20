alter table inventory.cars
    add create_date timestamp default now() not null;

alter table inventory.cars
    add update_date timestamp;
