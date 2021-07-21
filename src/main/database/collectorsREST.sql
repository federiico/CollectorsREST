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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autore`
--

LOCK TABLES `autore` WRITE;
/*!40000 ALTER TABLE `autore` DISABLE KEYS */;
INSERT INTO `autore` VALUES (1,'Matteo','Professione','Ernia'),(2,'Mirko','Martorana','Rkomi'),(3,'Fabio','Rizzo','Marracash'),(4,'Colson','Baker','MGK'),(5,'Joseph','Orison','TwentyOnePilots'),(6,'Travis','Baker','Sum41'),(7,'Simone','Benussi','Mace'),(8,'Cosimo','Fini','GuePequeno');
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione`
--

LOCK TABLES `collezione` WRITE;
/*!40000 ALTER TABLE `collezione` DISABLE KEYS */;
INSERT INTO `collezione` VALUES (1,1,'HipHopItaly','Personale'),(2,1,'CarRadio','Personale'),(3,1,'Mix','Pubblica'),(4,2,'MyMorningPlaylist','Personale'),(5,2,'Chilling','Personale'),(6,3,'BestOfRkomi','Pubblica'),(7,3,'BestOfMarracash','Pubblica'),(8,3,'BestOfErnia','Pubblica');
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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione_condivisa`
--

LOCK TABLES `collezione_condivisa` WRITE;
/*!40000 ALTER TABLE `collezione_condivisa` DISABLE KEYS */;
INSERT INTO `collezione_condivisa` VALUES (1,5,1),(2,6,1),(3,7,1),(4,1,2),(5,8,2),(6,4,3),(7,1,3),(8,6,2),(9,7,2);
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
  KEY `id_disco` (`id_disco`),
  KEY `dischi_collezione_ibfk_1_idx` (`id_collezione`),
  CONSTRAINT `dischi_collezione_ibfk_2` FOREIGN KEY (`id_disco`) REFERENCES `disco` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk1` FOREIGN KEY (`id_collezione`) REFERENCES `collezione` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dischi_collezione`
--

LOCK TABLES `dischi_collezione` WRITE;
/*!40000 ALTER TABLE `dischi_collezione` DISABLE KEYS */;
INSERT INTO `dischi_collezione` VALUES (33,1,30),(34,8,30),(35,8,31),(36,8,32);
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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disco`
--

LOCK TABLES `disco` WRITE;
/*!40000 ALTER TABLE `disco` DISABLE KEYS */;
INSERT INTO `disco` VALUES (30,1,'67','2016'),(31,1,'68','2020'),(32,1,'Gemelli','2021');
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
  KEY `id_traccia` (`id_traccia`),
  KEY `tracce_disco_ibfk_1_idx` (`id_disco`,`id_traccia`),
  CONSTRAINT `tracce_disco_ibfk_2` FOREIGN KEY (`id_traccia`) REFERENCES `traccia` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracce_disco`
--

LOCK TABLES `tracce_disco` WRITE;
/*!40000 ALTER TABLE `tracce_disco` DISABLE KEYS */;
INSERT INTO `tracce_disco` VALUES (89,30,88),(90,30,89),(91,30,90),(92,30,91),(93,30,92),(94,30,93),(95,31,94),(96,31,95),(97,31,96),(98,31,97),(99,32,98),(100,32,99),(101,32,100),(102,32,101),(103,32,102),(104,32,103);
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
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traccia`
--

LOCK TABLES `traccia` WRITE;
/*!40000 ALTER TABLE `traccia` DISABLE KEYS */;
INSERT INTO `traccia` VALUES (88,'EGO',257),(89,'Lei no',231),(90,'Disgusting',201),(91,'Noia',193),(92,'QT',227),(93,'Bella',220),(94,'68',203),(95,'Simba',243),(96,'Tosse',182),(97,'La paura',164),(98,'Vivo',203),(99,'Superclassico',204),(100,'Puro Sinaloa',220),(101,'Morto dentro',164),(102,'Ferma a guardare',219),(103,'Cigni',200);
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
INSERT INTO `utente` VALUES (1,'federico','federico123','92682e31-93bb-4954-8c98-7df120eae57a'),(2,'davide','davide123',''),(3,'mario','mario123',NULL);
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

-- Dump completed on 2021-07-21  9:54:10
