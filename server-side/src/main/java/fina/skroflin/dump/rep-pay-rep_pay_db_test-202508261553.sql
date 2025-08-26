/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19-11.7.2-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: rep_pay_db_test
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*M!100616 SET @OLD_NOTE_VERBOSITY=@@NOTE_VERBOSITY, NOTE_VERBOSITY=0 */;

--
-- Table structure for table `booking`
--

DROP TABLE IF EXISTS `booking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking` (
  `booking_id` int(11) NOT NULL AUTO_INCREMENT,
  `booking_status` enum('accepted','pending','rejected') DEFAULT NULL,
  `training_session_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`booking_id`),
  KEY `FKk9kxc1uj21lassyj6845cbuok` (`training_session_id`),
  KEY `FKkgseyy7t56x7lkjgu3wah5s3t` (`user_id`),
  CONSTRAINT `FKk9kxc1uj21lassyj6845cbuok` FOREIGN KEY (`training_session_id`) REFERENCES `training_session` (`training_session_id`),
  CONSTRAINT `FKkgseyy7t56x7lkjgu3wah5s3t` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking`
--

LOCK TABLES `booking` WRITE;
/*!40000 ALTER TABLE `booking` DISABLE KEYS */;
INSERT INTO `booking` VALUES
(1,'pending',1,3),
(2,'pending',2,3),
(3,'pending',3,3),
(4,'pending',4,3),
(5,'pending',5,3),
(6,'pending',6,3),
(7,NULL,10,3),
(8,'pending',23,3);
/*!40000 ALTER TABLE `booking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership`
--

DROP TABLE IF EXISTS `membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership` (
  `membership_id` int(11) NOT NULL AUTO_INCREMENT,
  `end_date` date DEFAULT NULL,
  `membership_price` float DEFAULT NULL,
  `start_date` date DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`membership_id`),
  KEY `FKjp7ht675da9n751xycuwii77s` (`user_id`),
  CONSTRAINT `FKjp7ht675da9n751xycuwii77s` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership`
--

LOCK TABLES `membership` WRITE;
/*!40000 ALTER TABLE `membership` DISABLE KEYS */;
/*!40000 ALTER TABLE `membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_session`
--

DROP TABLE IF EXISTS `training_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_session` (
  `training_session_id` int(11) NOT NULL AUTO_INCREMENT,
  `beginning_of_session` datetime(6) DEFAULT NULL,
  `end_of_session` datetime(6) DEFAULT NULL,
  `training_level` enum('advanced','beginner','intermediate') DEFAULT NULL,
  `training_type` enum('conditioning','crossfit','legs','pull','push','weightlifting','yoga') DEFAULT NULL,
  `trainer_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`training_session_id`),
  KEY `FKj0yocr68t6gklt2ovxrytxfml` (`trainer_id`),
  CONSTRAINT `FKj0yocr68t6gklt2ovxrytxfml` FOREIGN KEY (`trainer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_session`
--

LOCK TABLES `training_session` WRITE;
/*!40000 ALTER TABLE `training_session` DISABLE KEYS */;
INSERT INTO `training_session` VALUES
(1,'2025-08-14 18:00:00.000000','2025-08-14 19:30:00.000000','intermediate','weightlifting',2),
(2,'2025-08-15 17:00:00.000000','2025-08-15 18:30:00.000000','beginner','weightlifting',2),
(3,'2025-08-16 17:00:00.000000','2025-08-16 18:30:00.000000','advanced','weightlifting',2),
(4,'2025-08-18 18:00:00.000000','2025-08-18 19:30:00.000000','advanced','weightlifting',2),
(5,'2025-08-19 17:00:00.000000','2025-08-19 18:30:00.000000','advanced','weightlifting',2),
(6,'2025-08-20 18:00:00.000000','2025-08-20 19:30:00.000000','advanced','weightlifting',2),
(7,'2025-08-21 17:00:00.000000','2025-08-21 18:30:00.000000','advanced','weightlifting',2),
(8,'2025-08-22 18:00:00.000000','2025-08-22 19:30:00.000000','advanced','weightlifting',2),
(9,'2025-08-23 17:00:00.000000','2025-08-23 18:30:00.000000','advanced','weightlifting',2),
(10,'2025-08-25 18:00:00.000000','2025-08-25 19:30:00.000000','advanced','weightlifting',2),
(11,'2025-08-01 18:00:00.000000','2025-08-01 19:30:00.000000','advanced','weightlifting',2),
(12,NULL,NULL,NULL,NULL,2),
(13,NULL,NULL,NULL,NULL,2),
(14,NULL,NULL,NULL,NULL,2),
(15,NULL,NULL,NULL,NULL,2),
(16,NULL,NULL,NULL,NULL,2),
(17,NULL,NULL,NULL,NULL,2),
(18,NULL,NULL,NULL,NULL,2),
(19,NULL,NULL,NULL,NULL,2),
(20,NULL,NULL,NULL,NULL,2),
(21,NULL,NULL,NULL,NULL,2),
(22,NULL,NULL,NULL,NULL,2),
(23,'2025-08-25 10:00:00.000000','2025-08-25 11:30:00.000000','intermediate','weightlifting',2),
(24,'2025-08-26 10:00:00.000000','2025-08-26 11:30:00.000000','intermediate','weightlifting',2),
(25,'2025-08-27 10:00:00.000000','2025-08-27 11:30:00.000000','intermediate','weightlifting',2),
(26,NULL,NULL,NULL,NULL,2),
(27,NULL,NULL,NULL,NULL,2),
(28,NULL,NULL,NULL,NULL,2),
(29,NULL,NULL,NULL,NULL,2),
(30,NULL,NULL,NULL,NULL,2),
(31,NULL,NULL,NULL,NULL,2),
(32,NULL,NULL,NULL,NULL,2),
(33,'2025-08-28 00:01:00.000000','2025-08-28 03:05:00.000000','beginner','legs',2),
(34,'2025-10-14 18:00:00.000000','2025-10-14 19:30:00.000000','intermediate','weightlifting',2),
(35,'2025-07-14 18:00:00.000000','2025-07-14 19:30:00.000000','intermediate','weightlifting',2),
(36,'2025-09-01 09:00:00.000000','2025-09-01 10:00:00.000000','beginner','weightlifting',2),
(37,'2025-08-26 17:00:00.000000','2025-08-26 18:00:00.000000','beginner','weightlifting',2);
/*!40000 ALTER TABLE `training_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_membership_paid` bit(1) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `membership_month` date DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('coach','superuser','user') DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`),
  UNIQUE KEY `UKsb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES
(1,'skroflin@fina.hr','Sven',0x00,'Kroflin',NULL,'$2a$10$6tqh2jvD4viH7E52daG3MOZ6Z4ylIfpiTB4hyTmD4DFyUbvANnkMu','superuser','skroflin'),
(2,'mkroflin@gmail.com','Matej',0x00,'Kroflin',NULL,'$2a$10$1NPbBE6wYOUrZcwvW3pXzOTh0xzHA8TZjqxyGIvtvFrDixPDM9K3S','coach','mkroflin'),
(3,'fkroflin@etfos.hr','Fran',0x00,'Kroflin',NULL,'$2a$10$HbExJ3jBJCrE6222K6aFLOnMk0fls46.oOvXfGONINR9XVHkZ/j5G','user','fkroflin');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'rep_pay_db_test'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*M!100616 SET NOTE_VERBOSITY=@OLD_NOTE_VERBOSITY */;

-- Dump completed on 2025-08-26 15:53:43
