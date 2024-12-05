CREATE SEQUENCE DNEVNIK_RADA_ID_GENERATOR AS INTEGER START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE KAZNE_RB_GENERATOR AS INTEGER START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE PRACENEVOZNJE_RB_GENERATOR AS INTEGER START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE VOZNJE_RB_GENERATOR AS INTEGER START WITH 1 INCREMENT BY 1;

DROP TABLE DNEVNIK_RADA;
DROP TABLE KAZNE;
DROP TABLE PRACENEVOZNJE;
DROP TABLE VOZNJE;

DROP SEQUENCE PRACENE_VOZNJE_RB_GENERATOR;

CREATE TABLE DNEVNIK_RADA (
	id int NOT NULL PRIMARY KEY,
	vrijeme timestamp,
	korisnickoIme char (20),
	adresaRacunala varchar (99),
	ipAdresaRacunala varchar (50),
	nazivOS varchar (30),
	verzijaVM varchar (20),
	opisRada varchar (512)
); 

CREATE TABLE KAZNE  (
	rb int NOT NULL PRIMARY KEY,
	id int NOT NULL,
	vrijemePocetak bigint NOT NULL,
	vrijemeKraj bigint NOT NULL,
	brzina double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL,
	gpsSirinaRadar double NOT NULL,
	gpsDuzinaRadar double NOT NULL,
    FOREIGN KEY (id) REFERENCES vozila (vozilo)
); 

CREATE TABLE PRACENEVOZNJE (
	rb int NOT NULL PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL,
    FOREIGN KEY (id) REFERENCES vozila (vozilo)
); 

CREATE TABLE VOZNJE (
	rb int NOT NULL PRIMARY KEY,
	id int NOT NULL,
	broj int NOT NULL,
	vrijeme bigint NOT NULL,
	brzina double NOT NULL,
	snaga double NOT NULL,
	struja double NOT NULL,
	visina double NOT NULL,
	gpsBrzina double NOT NULL,
	tempVozila int NOT NULL,
	postotakBaterija int NOT NULL,
	naponBaterija double NOT NULL,
	kapacitetBaterija int NOT NULL,
	tempBaterija int NOT NULL,
	preostaloKm double NOT NULL,
	ukupnoKm double NOT NULL,
	gpsSirina double NOT NULL,
	gpsDuzina double NOT NULL
); 

GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE dnevnik_rada TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE kazne TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE pracenevoznje TO "aplikacija";
GRANT SELECT, UPDATE, INSERT, DELETE ON TABLE voznje TO "aplikacija";

ALTER TABLE kazne ALTER COLUMN rb SET DEFAULT nextval('KAZNE_RB_GENERATOR');
ALTER TABLE PRACENEVOZNJE ALTER COLUMN rb SET DEFAULT nextval('PRACENEVOZNJE_RB_GENERATOR');

GRANT ALTER ON TABLE PRACENEVOZNJE TO nwtis_4;






