CREATE DATABASE  IF NOT EXISTS `chat`;
USE `chat`;


DROP TABLE IF EXISTS `amis`;

CREATE TABLE `amis` (
  `uuid_first_user` varchar(200) NOT NULL,
  `uuid_second_user` varchar(200) NOT NULL,
  PRIMARY KEY (`uuid_first_user`,`uuid_second_user`),
  KEY `fk_uuid_second_user_AMIS` (`uuid_second_user`),
  CONSTRAINT `fk_uuid_first_user_AMIS` FOREIGN KEY (`uuid_first_user`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `fk_uuid_second_user_AMIS` FOREIGN KEY (`uuid_second_user`) REFERENCES `user` (`uuid_user`)
) ;


LOCK TABLES `amis` WRITE;

UNLOCK TABLES;

DROP TABLE IF EXISTS `connected`;
CREATE TABLE `connected` (
  `uuid_user` varchar(200) NOT NULL,
  `uuid_room` varchar(200) NOT NULL,
  PRIMARY KEY (`uuid_user`,`uuid_room`),
  KEY `fk_uuid_room_connected` (`uuid_room`),
  CONSTRAINT `fk_uuid_room_connected` FOREIGN KEY (`uuid_room`) REFERENCES `room` (`uuid_room`),
  CONSTRAINT `fk_uuid_user_connected` FOREIGN KEY (`uuid_user`) REFERENCES `user` (`uuid_user`)
);



LOCK TABLES `connected` WRITE;
UNLOCK TABLES;


DROP TABLE IF EXISTS `messageroom`;
CREATE TABLE `messageroom` (
  `message_id` int NOT NULL AUTO_INCREMENT,
  `uuid_room` varchar(200) NOT NULL,
  `uuid_user` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `message_date` date NOT NULL DEFAULT (curdate()),
  `isDelated` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`message_id`),
  KEY `fk_uuid_room_messageroom` (`uuid_room`),
  KEY `fk_uuid_user_messageroom` (`uuid_user`),
  CONSTRAINT `fk_uuid_room_messageroom` FOREIGN KEY (`uuid_room`) REFERENCES `room` (`uuid_room`),
  CONSTRAINT `fk_uuid_user_messageroom` FOREIGN KEY (`uuid_user`) REFERENCES `user` (`uuid_user`)
) ;

LOCK TABLES `messageroom` WRITE;
UNLOCK TABLES;



DROP TABLE IF EXISTS `messageto`;

CREATE TABLE `messageto` (
  `uuid_reciver` varchar(200) NOT NULL,
  `uuid_sender` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `message_date` date NOT NULL DEFAULT (curdate()),
  `isDelated` tinyint(1) NOT NULL DEFAULT '0',
  KEY `fk_uuid_reciver` (`uuid_reciver`),
  KEY `fk_uuid_sender` (`uuid_sender`),
  CONSTRAINT `fk_uuid_reciver` FOREIGN KEY (`uuid_reciver`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `fk_uuid_sender` FOREIGN KEY (`uuid_sender`) REFERENCES `user` (`uuid_user`),
  CONSTRAINT `check_unique` CHECK ((`uuid_sender` <> `uuid_reciver`))
);



LOCK TABLES `messageto` WRITE;
UNLOCK TABLES;


DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `uuid_room` varchar(200) NOT NULL,
  `roomname` varchar(50) NOT NULL,
  `image` varchar(100) NOT NULL,
  PRIMARY KEY (`uuid_room`),
  UNIQUE KEY `roomname` (`roomname`)
);



LOCK TABLES `room` WRITE;
UNLOCK TABLES;



DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uuid_user` varchar(200) NOT NULL,
  `email` varchar(100) NOT NULL,
  `image` varchar(100) NOT NULL,
  `username` varchar(60) NOT NULL,
  `hashpasword` varchar(100) NOT NULL,
  `isadmin` tinyint(1) NOT NULL,
  PRIMARY KEY (`uuid_user`),
  UNIQUE KEY `username` (`username`)
);


LOCK TABLES `user` WRITE;
UNLOCK TABLES;
