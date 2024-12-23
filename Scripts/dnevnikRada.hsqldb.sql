SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS;

CREATE USER "nwtis_4" PASSWORD "nwtis#4";

ALTER USER "nwtis_4" SET PASSWORD "nwtis#4";

SELECT * FROM INFORMATION_SCHEMA.SYSTEM_USERS;

create table dnevnik_Rada (
	id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	vrijeme timestamp,
	korisnickoIme char (20),
	adresaRacunala varchar (99),
	ipAdresaRacunala varchar (50),
	nazivOS varchar (30),
	verzijaVM varchar (20),
	opisRada varchar (512)
); 

CREATE ROLE "aplikacija";

GRANT "aplikacija" TO "nwtis_4";

GRANT SELECT, UPDATE, INSERT ON TABLE dnevnik_Rada TO "aplikacija";

SELECT * FROM INFORMATION_SCHEMA.TABLE_PRIVILEGES;
