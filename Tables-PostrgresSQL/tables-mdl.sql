INSERT INTO BusStop VALUES
(1,'calle1',111,true),
(2,'calle2',222,true),
(3,'calle3',333,true),
(4,'calle4',444,true),
(5,'calle5',555,true);

INSERT INTO Route VALUES
(1,2,10.0,true),
(1,3,7.5,true),
(2,3,5.0,true),
(4,5,2.75,true);

INSERT INTO BusLine VALUES
('Linea1','Rojo',10);

INSERT INTO BusLineRoute VALUES
('Linea1',1,2,10.0);

INSERT INTO BusLineStop VALUES
('Linea1',1,true),
('Linea1',2,true);