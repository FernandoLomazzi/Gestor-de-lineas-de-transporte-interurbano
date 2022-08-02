-- BusStop

INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (1, 'Alvear', 1950, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (2, 'Padilla', 1525, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (3, 'Necochea', 952, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (4, 'Lavalle', 2100, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (5, 'Martín Zapata', 1555, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (6, 'Güemes', 1402, true);
INSERT INTO busstop (stop_number, stop_street_name, stop_street_number, enabled) VALUES (7, 'Alberdi', 1955, true);


-- Route

INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (6, 3, 5);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (6, 4, 7.5);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (3, 4, 2);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (4, 7, 11.265408);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (3, 7, 8);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (7, 2, 7.5);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (7, 5, 12);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (5, 1, 5);
INSERT INTO route (source_stop_number, destination_stop_number, distance_in_km) VALUES (2, 1, 16.09344);

-- BusLine

INSERT INTO busline (name, color, seating_capacity) VALUES ('Linea 8', '0x808000ff', 15);
INSERT INTO busline (name, color, seating_capacity) VALUES ('Linea 14', '0xff0000ff', 20);
INSERT INTO busline (name, color, seating_capacity) VALUES ('Linea 10', '0x0000ffff', 8);
INSERT INTO busline (name, color, seating_capacity) VALUES ('Linea 18', '0x008000ff', 30);

-- BusLineRoute

INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 8', 6, 4, 10);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 8', 4, 7, 6);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 8', 7, 2, 10);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 8', 2, 1, 10);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 14', 6, 3, 5);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 14', 3, 7, 10);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 14', 7, 2, 15);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 14', 2, 1, 7);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 10', 3, 4, 3);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 10', 4, 7, 10);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 18', 7, 5, 1200);
INSERT INTO buslineroute (bus_line_name, source_stop_number, destination_stop_number, estimated_time) VALUES ('Linea 18', 5, 1, 3600);

-- BusLineStop

INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 8', 6, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 8', 4, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 8', 7, false);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 8', 2, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 8', 1, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 14', 6, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 14', 3, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 14', 7, false);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 14', 2, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 14', 1, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 10', 3, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 10', 4, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 10', 7, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 18', 7, true);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 18', 5, false);
INSERT INTO buslinestop (bus_line_name, stop_number, stops) VALUES ('Linea 18', 1, true);

-- CheapLine

INSERT INTO cheapline (name, standing_capacity_percentage, standing_capacity) VALUES ('Linea 14', 0.257, 20);
INSERT INTO cheapline (name, standing_capacity_percentage, standing_capacity) VALUES ('Linea 10', 0.193, 8);

-- PremiumLine

INSERT INTO premiumline (name) VALUES ('Linea 8');
INSERT INTO premiumline (name) VALUES ('Linea 18');

-- PremiumLineServices

INSERT INTO premiumlineservices (name_line, name_service) VALUES ('Linea 8', 'WIFI');
INSERT INTO premiumlineservices (name_line, name_service) VALUES ('Linea 18', 'AIR_CONDITIONING');
INSERT INTO premiumlineservices (name_line, name_service) VALUES ('Linea 18', 'WIFI');
