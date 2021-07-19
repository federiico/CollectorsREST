-- MySQL dump 10.13  Distrib 8.0.20, for macos10.15 (x86_64)
--
-- Host: localhost    Database: collectorsdb
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `autore`
--

DROP TABLE IF EXISTS `autore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `autore` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `cognome` varchar(255) NOT NULL,
  `nome_arte` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autore`
--

LOCK TABLES `autore` WRITE;
/*!40000 ALTER TABLE `autore` DISABLE KEYS */;
INSERT INTO `autore` VALUES (1,'Matteo','Professione','Ernia');
/*!40000 ALTER TABLE `autore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collezione`
--

DROP TABLE IF EXISTS `collezione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collezione` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_utente` int NOT NULL,
  `titolo` varchar(255) NOT NULL,
  `privacy` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_utente` (`id_utente`),
  CONSTRAINT `collezione_ibfk_1` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione`
--

LOCK TABLES `collezione` WRITE;
/*!40000 ALTER TABLE `collezione` DISABLE KEYS */;
INSERT INTO `collezione` VALUES (1,1,'Summer2k21','Personale'),(2,1,'HipHopTopia','Personale'),(3,2,'Inverno2k21','Pubblica');
/*!40000 ALTER TABLE `collezione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collezione_condivisa`
--

DROP TABLE IF EXISTS `collezione_condivisa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collezione_condivisa` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_collezione` int NOT NULL,
  `id_utente` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_collezione` (`id_collezione`),
  KEY `id_utente` (`id_utente`),
  CONSTRAINT `collezione_condivisa_ibfk_1` FOREIGN KEY (`id_collezione`) REFERENCES `collezione` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `collezione_condivisa_ibfk_2` FOREIGN KEY (`id_utente`) REFERENCES `utente` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione_condivisa`
--

LOCK TABLES `collezione_condivisa` WRITE;
/*!40000 ALTER TABLE `collezione_condivisa` DISABLE KEYS */;
INSERT INTO `collezione_condivisa` VALUES (1,1,2),(2,3,1);
/*!40000 ALTER TABLE `collezione_condivisa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dischi_collezione`
--

DROP TABLE IF EXISTS `dischi_collezione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dischi_collezione` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_collezione` int NOT NULL,
  `id_disco` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_collezione` (`id_collezione`),
  KEY `id_disco` (`id_disco`),
  CONSTRAINT `dischi_collezione_ibfk_1` FOREIGN KEY (`id_collezione`) REFERENCES `collezione` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `dischi_collezione_ibfk_2` FOREIGN KEY (`id_disco`) REFERENCES `disco` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dischi_collezione`
--

LOCK TABLES `dischi_collezione` WRITE;
/*!40000 ALTER TABLE `dischi_collezione` DISABLE KEYS */;
INSERT INTO `dischi_collezione` VALUES (1,1,1),(2,2,1),(3,3,1),(7,1,2);
/*!40000 ALTER TABLE `dischi_collezione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disco`
--

DROP TABLE IF EXISTS `disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disco` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_autore` int NOT NULL,
  `titolo` varchar(255) NOT NULL,
  `anno` varchar(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_autore` (`id_autore`),
  CONSTRAINT `disco_ibfk_1` FOREIGN KEY (`id_autore`) REFERENCES `autore` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disco`
--

LOCK TABLES `disco` WRITE;
/*!40000 ALTER TABLE `disco` DISABLE KEYS */;
INSERT INTO `disco` VALUES (1,1,'68','2020'),(2,1,'67','2016');
/*!40000 ALTER TABLE `disco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tracce_disco`
--

DROP TABLE IF EXISTS `tracce_disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tracce_disco` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_disco` int NOT NULL,
  `id_traccia` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `id_disco` (`id_disco`),
  KEY `id_traccia` (`id_traccia`),
  CONSTRAINT `tracce_disco_ibfk_1` FOREIGN KEY (`id_disco`) REFERENCES `disco` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `tracce_disco_ibfk_2` FOREIGN KEY (`id_traccia`) REFERENCES `traccia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracce_disco`
--

LOCK TABLES `tracce_disco` WRITE;
/*!40000 ALTER TABLE `tracce_disco` DISABLE KEYS */;
INSERT INTO `tracce_disco` VALUES (1,1,1),(2,1,2),(3,2,3),(4,2,4),(5,2,5);
/*!40000 ALTER TABLE `tracce_disco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traccia`
--

DROP TABLE IF EXISTS `traccia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traccia` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(255) NOT NULL,
  `durata` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traccia`
--

LOCK TABLES `traccia` WRITE;
/*!40000 ALTER TABLE `traccia` DISABLE KEYS */;
INSERT INTO `traccia` VALUES (1,'Domani',120),(2,'68',194),(3,'QT8',128),(4,'Bella',214),(5,'La ballata di Mario Rossi',243);
/*!40000 ALTER TABLE `traccia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id` int NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'mario','mario123','ea481d95-b6a5-4bc4-8028-4a6ed2728cee'),(2,'giovanni','giovanni321',NULL);
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'collectorsdb'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-07-19 10:54:06
