INSERT INTO BusStop VALUES
(1,'calle1',111,true),
(2,'calle2',222,true),
(3,'calle3',333,true),
(4,'calle4',444,true),
(5,'calle5',555,true);

INSERT INTO Route VALUES
(1,2,10.0),
(1,3,7.5),
(2,3,5.0),
(3, 4, 4.0),
(4,5,2.75);

INSERT INTO BusLine VALUES
('Linea1','0x16a216',10),
('Linea2', '0x16a213', 20),
('Linea3', '0x0000FF', 30);

INSERT INTO BusLineRoute VALUES
('Linea1',1,2,10.0),
('Linea2', 1, 2, 5),
('Linea2', 2, 3, 4),
('Linea2', 3, 4, 4),
('Linea3', 1, 3, 4),
('Linea3', 3, 4, 6),
('Linea3', 4, 5, 4);

INSERT INTO BusLineStop VALUES
('Linea1',1,true),
('Linea1',2,true),
('Linea2', 1,true),
('Linea2', 2,true),
('Linea2', 3,true),
('Linea2', 4,true),
('Linea3', 1, true),
('Linea3', 3, true),
('Linea3', 4, true),
('Linea3', 5, true);

INSERT INTO CheapLine VALUES
('Linea2', 0.12, 23),
('Linea3', 0.20, 50);
