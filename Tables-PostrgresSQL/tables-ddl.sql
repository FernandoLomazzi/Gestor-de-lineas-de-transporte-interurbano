CREATE TYPE PremiumLineService AS ENUM (
	'WIFI',
	'AIR_CONDITIONING'
);

CREATE TABLE BusLine (
	name varchar(50) PRIMARY KEY CHECK (LENGTH(name)>0),
	color varchar(20) NOT NULL,
	seating_capacity integer NOT NULL
);

CREATE TABLE CheapLine (
	name varchar(50) PRIMARY KEY REFERENCES BusLine(name) ON DELETE CASCADE,
	standing_capacity_percentage real NOT NULL,
	standing_capacity integer NOT NULL
);

CREATE TABLE PremiumLine (
	name varchar(50) PRIMARY KEY REFERENCES BusLine(name)
);

CREATE TABLE PremiumLineServices (
	name_line varchar(50) REFERENCES PremiumLine(name) ON DELETE CASCADE,
	name_service PremiumLineService,
	PRIMARY KEY (name_line, name_service)
);

CREATE TABLE BusStop (
	stop_number integer PRIMARY KEY,
	stop_street_name varchar(50) NOT NULL CHECK (LENGTH(stop_street_name)>0),
	stop_street_number integer NOT NULL,
	enabled boolean NOT NULL
);

CREATE TABLE Incident (
	bus_stop_disabled_number integer REFERENCES BusStop(stop_number) ON DELETE CASCADE,
	begin_date date,
	end_date date CHECK(end_date>=begin_date),
	description varchar(150),
	concluded boolean NOT NULL CHECK(concluded=false OR concluded=true AND end_date IS NOT NULL),
	PRIMARY KEY (bus_stop_disabled_number,begin_date)
);

CREATE TABLE Route (
	source_stop_number integer REFERENCES BusStop(stop_number) ON DELETE CASCADE,
	destination_stop_number integer REFERENCES BusStop(stop_number) ON DELETE CASCADE,
	distance_in_km real NOT NULL,
	PRIMARY KEY(source_stop_number,destination_stop_number)
);

CREATE TABLE BusLineRoute (
	bus_line_name varchar(50) REFERENCES BusLine(name) ON DELETE CASCADE,
	source_stop_number integer,
	destination_stop_number integer,
	estimated_time integer NOT NULL,
    FOREIGN KEY (source_stop_number,destination_stop_number) REFERENCES Route(source_stop_number,destination_stop_number) ON DELETE CASCADE,
	PRIMARY KEY (bus_line_name, source_stop_number,destination_stop_number)
);

CREATE TABLE BusLineStop (
	bus_line_name varchar(50) REFERENCES BusLine(name) ON DELETE CASCADE,
	stop_number integer REFERENCES BusStop(stop_number) ON DELETE CASCADE,
	stops boolean NOT NULL,
	PRIMARY KEY (bus_line_name, stop_number)
);

/*
DROP TYPE PremiumLineService CASCADE;
DROP TABLE BusLine CASCADE;
DROP TABLE BusLineRoute CASCADE;
DROP TABLE BusLineStop CASCADE;
DROP TABLE BusStop CASCADE;
DROP TABLE CheapLine CASCADE;
DROP TABLE Incident CASCADE;
DROP TABLE PremiumLine CASCADE;
DROP TABLE PremiumLineServices;
DROP TABLE Route CASCADE;
*/