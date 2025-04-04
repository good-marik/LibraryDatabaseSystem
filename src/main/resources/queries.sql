create table Person(
	id int generated by default as identity primary key,
	name varchar(100) NOT NULL UNIQUE,
	yearOfBirth int check(yearOfBirth >= 1900)
);

insert into Person(name, year_of_birth) values('Marat Khusniyarov', 1979);
insert into Person(name, year_of_birth) values('Robert Meyer', 1964);
insert into Person(name, year_of_birth) values('Anna Ley', 1984);
insert into Person(name, year_of_birth) values('Darth Vader', 1977);


create table Book(
	id int generated by default as identity primary key,
	owner_id int REFERENCES Person(id) ON DELETE SET NULL,
	title varchar NOT NULL,
	author varchar NOT NULL,
	year int check(year >= 0),
	borrowed_at TIMESTAMP
);

insert into Book(title, author, year) values('Clean Code', 'Robert C. Martin', 2009);
insert into Book(title, author, year) values('Java - Der umfassende Programmierkurs', 'Peter Müller', 2014);
insert into Book(title, author, year) values('Coordination Chemistry', 'Joan Ribas Gispert', 2000);
insert into Book(title, author, year) values('Fahrenheit 451', 'Ray Bradbury', 1953);
insert into Book(title, author, year) values('Der Meister und Margarita', 'Michail Bulgakow', 1966);




select * from person join book on person.id = ownerid;
select ownerid from book where id=5;
select * from person where id=(select ownerid from book where id=5);