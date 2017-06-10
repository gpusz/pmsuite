create user pmsuite identified by 12345678;
grant create session, create table, create view, create sequence, create any index, create procedure, create type, create trigger to pmsuite;



create table pm_wykonania (id number(9,0) primary key, data_wykonania date not null, uzytkownik varchar2(100), nazwa_pliku varchar2(100));
create sequence pm_wykonania_id;
