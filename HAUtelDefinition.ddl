drop database if exists "HAUtel";
create database "HAUtel";

create table Klient
(
    ID            int4                    not null,
    imie          varchar(60)             not null,
    nazwisko      varchar(60)             not null,
    email         varchar(120)            not null unique,
    numerTelefonu varchar(11)             not null,
    naleznosci    numeric(8, 2) default 0 not null,
    primary key (ID)
);
create table Rezerwacja
(
    ID                 int4          not null,
    ZwierzeID          int4          not null,
    termin             date          not null,
    czasPobytu         int4          not null,
    kosztPobytu        numeric(8, 2) not null,
    StatusRezerwacjiID int4          not null,
    primary key (ID)
);
create table Zwierze
(
    ID        int4        not null,
    imie      varchar(60) not null,
    klientID  int4        not null,
    gatunekID int4        not null,
    Leki      varchar(1000),
    Alergie   varchar(1000),
    primary key (ID)
);
create table DodatkowaUsluga
(
    ID                 int4                    not null,
    nazwa              varchar(60)             not null unique,
    koszt              numeric(6, 2) default 0 not null,
    dziennyLimitMiejsc int4          default 0 not null,
    constraint Limit_dzienny primary key (ID)
);
create table Wplata
(
    ID           int4          not null,
    RezerwacjaID int4          not null,
    numerKonta   varchar(26)   not null,
    imie         varchar(60)   not null,
    nazwisko     varchar(60)   not null,
    kwota        numeric(8, 2) not null,
    primary key (ID)
);
create table ZamowionaUsluga
(
    ID                int4 not null,
    RezerwacjaID      int4 not null,
    DodatkowaUslugaID int4 not null,
    primary key (ID)
);
create table Gatunek
(
    ID           int4          not null,
    nazwa        varchar(255)  not null unique,
    dziennyKoszt numeric(6, 2) not null,
    limitMiejsc  int4          not null,
    primary key (ID)
);
create table StatusRezerwacji
(
    ID    int4        not null,
    Nazwa varchar(60) not null unique,
    primary key (ID)
);
create table Pracownik
(
    ID    int4         not null,
    haslo varchar(120) not null,
    primary key (ID)
);
create table Haslo
(
    KlientID int4         not null,
    haslo    varchar(120) not null,
    primary key (KlientID)
);
create unique index Klient_ID on Klient (ID);
create unique index Rezerwacja_ID on Rezerwacja (ID);
create unique index Zwierze_ID on Zwierze (ID);
create unique index DodatkowaUsluga_ID on DodatkowaUsluga (ID);
create unique index Wplata_ID on Wplata (ID);
create unique index ZamowionaUsluga_ID on ZamowionaUsluga (ID);
create unique index Gatunek_ID on Gatunek (ID);
create unique index StatusRezerwacji_ID on StatusRezerwacji (ID);
create unique index Pracownik_ID on Pracownik (ID);
create unique index Haslo_KlientID on Haslo (KlientID);
alter table Zwierze
    add constraint FKZwierze94787 foreign key (klientID) references Klient (ID) on delete cascade;
alter table Rezerwacja
    add constraint FKRezerwacja122562 foreign key (ZwierzeID) references Zwierze (ID) on delete cascade;
alter table ZamowionaUsluga
    add constraint FKZamowionaU686219 foreign key (RezerwacjaID) references Rezerwacja (ID) on delete cascade;
alter table ZamowionaUsluga
    add constraint FKZamowionaU330990 foreign key (DodatkowaUslugaID) references DodatkowaUsluga (ID) on delete cascade;
alter table Zwierze
    add constraint FKZwierze832141 foreign key (gatunekID) references Gatunek (ID) on delete cascade;
alter table Rezerwacja
    add constraint FKRezerwacja90401 foreign key (StatusRezerwacjiID) references StatusRezerwacji (ID) on delete cascade;
alter table Wplata
    add constraint FKWplata290252 foreign key (RezerwacjaID) references Rezerwacja (ID) on delete cascade;
alter table Haslo
    add constraint FKHaslo320816 foreign key (KlientID) references Klient (ID) on delete cascade;



REVOKE CREATE ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON DATABASE "HAUtel" FROM PUBLIC;
GRANT CONNECT ON DATABASE "HAUtel" TO PUBLIC;


create role client
    login;
grant insert, select, update on public.klient to client;
grant insert, select, update on public.rezerwacja to client;
grant insert, select, update on public.zwierze to client;
grant select on public.dodatkowausluga to client;
grant select on public.wplata to client;
grant insert, select on public.zamowionausluga to client;
grant select on public.gatunek to client;
grant select on public.statusrezerwacji to client;
grant insert, select, update on public.haslo to client;

create role employee
    login;
grant select, update on public.klient to employee;
grant select, update on public.rezerwacja to employee;
grant select on public.zwierze to employee;
grant select, update on public.dodatkowausluga to employee;
grant select on public.wplata to employee;
grant delete, insert, select on public.zamowionausluga to employee;
grant select, update on public.gatunek to employee;
grant select on public.statusrezerwacji to employee;
grant select on public.pracownik to employee;

create role admin
    login
    createdb
    createrole
    bypassrls;
grant delete, insert, select, update on klient to admin;
grant delete, insert, select, update on rezerwacja to admin;
grant delete, insert, select, update on zwierze to admin;
grant delete, insert, select, update on dodatkowausluga to admin;
grant delete, insert, select, update on wplata to admin;
grant delete, insert, select, update on zamowionausluga to admin;
grant delete, insert, select, update on gatunek to admin;
grant delete, insert, select, update on statusrezerwacji to admin;
grant delete, insert, update on pracownik to admin;
grant delete, insert on haslo to admin;


CREATE USER testclient WITH PASSWORD 'testClient';
GRANT client TO testclient;

CREATE USER testemployee WITH PASSWORD 'testEmployee';
GRANT employee TO testEmployee;

CREATE USER admin0 WITH PASSWORD 'admin';
GRANT admin TO admin0;

CREATE SEQUENCE klient_id_seq MINVALUE 1;
CREATE SEQUENCE rezerwacja_id_seq MINVALUE 1;
CREATE SEQUENCE zwierze_id_seq MINVALUE 1;
CREATE SEQUENCE dodatkowaUsluga_id_seq MINVALUE 1;
CREATE SEQUENCE wplata_id_seq MINVALUE 1;
CREATE SEQUENCE zamowionaUsluga_id_seq MINVALUE 1;
CREATE SEQUENCE gatunek_id_seq MINVALUE 1;
CREATE SEQUENCE statusRezerwacji_id_seq MINVALUE 1;
CREATE SEQUENCE pracownik_id_seq MINVALUE 1;

ALTER TABLE klient
    ALTER id SET DEFAULT nextval('klient_id_seq');
ALTER TABLE rezerwacja
    ALTER id SET DEFAULT nextval('rezerwacja_id_seq');
ALTER TABLE zwierze
    ALTER id SET DEFAULT nextval('zwierze_id_seq');
ALTER TABLE dodatkowausluga
    ALTER id SET DEFAULT nextval('dodatkowaUsluga_id_seq');
ALTER TABLE wplata
    ALTER id SET DEFAULT nextval('wplata_id_seq');
ALTER TABLE zamowionausluga
    ALTER id SET DEFAULT nextval('zamowionaUsluga_id_seq');
ALTER TABLE gatunek
    ALTER id SET DEFAULT nextval('gatunek_id_seq');
ALTER TABLE statusrezerwacji
    ALTER id SET DEFAULT nextval('statusRezerwacji_id_seq');
ALTER TABLE pracownik
    ALTER id SET DEFAULT nextval('pracownik_id_seq');

ALTER SEQUENCE klient_id_seq OWNED BY klient.id;
ALTER SEQUENCE rezerwacja_id_seq OWNED BY rezerwacja.id;
ALTER SEQUENCE zwierze_id_seq OWNED BY zwierze.id;
ALTER SEQUENCE dodatkowaUsluga_id_seq OWNED BY dodatkowausluga.id;
ALTER SEQUENCE wplata_id_seq OWNED BY wplata.id;
ALTER SEQUENCE zamowionaUsluga_id_seq OWNED BY zamowionausluga.id;
ALTER SEQUENCE gatunek_id_seq OWNED BY gatunek.id;
ALTER SEQUENCE statusRezerwacji_id_seq OWNED BY statusrezerwacji.id;
ALTER SEQUENCE pracownik_id_seq OWNED BY pracownik.id;
