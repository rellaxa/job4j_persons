create table persons (
    id       serial primary key not null,
    login    varchar(2000) unique,
    password varchar(2000)
);

insert into persons (login, password) values ('parsentev', '123');
insert into persons (login, password) values ('relaxa', '123');
insert into persons (login, password) values ('ivan', '123');