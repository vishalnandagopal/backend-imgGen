create database BMC_ImagGen;
use bmc_imaggen;

CREATE TABLE `bmc_imaggen`.`images` (
  `uuid` VARCHAR(36) NOT NULL,
  `name` VARCHAR(100) NULL,
  `file` MEDIUMBLOB NULL,
  PRIMARY KEY (`uuid`)
);


insert into images values('65c97eb5-a7fd-41b1-8505-97ceab7fa06c', 'cute_cat1', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\cute_cat1.jpg"));

insert into images values('7885633f-9234-4be4-9446-0ca2cd288d32', 'cute_kitten_2', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\cute_kitten_2.jpg"));

insert into images values('9503b673-25ab-4671-8d0c-8f90dd330272', 'hr_image', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\hr_image.png"));

insert into images values('7b42d047-819c-4b68-87da-d2664ecdd9ec', 'mkt_image', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\mkt_image.png"));

insert into images values('6b462cea-b5ee-4c0e-980f-ba4111a2d3f2', 'proc_img', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\proc_img.png"));

insert into images values('7b24911e-99a0-489a-b77a-b3f9254741c0', 'sales_img', LOAD_FILE("C:\\ProgramData\\MySQL\\MySQL Server 8.0\\Uploads\\sales_img.png"));


select * from images;


truncate images;

