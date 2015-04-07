DROP DATABASE IF EXISTS kwl_database;
CREATE DATABASE kwl_database;
USE kwl_database;
CREATE TABLE user_profile
(
	id int(32) auto_increment not null primary key,
	userName varchar(30),
	password varchar(128)
);
CREATE TABLE owl_profile
(
	userName varchar(30),
	fileId int(32) not null auto_increment primary key,
	title varchar(100),
	description varchar(300),
	tag varchar(100),
	classification varchar(100),
	reference varchar(200),
	version varchar(30),
	filepath varchar(100),
	finaldate TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);