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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autore`
--

LOCK TABLES `autore` WRITE;
/*!40000 ALTER TABLE `autore` DISABLE KEYS */;
INSERT INTO `autore` VALUES (1,'Matteo','Professione','Ernia'),(2,'Mirko','Martorana','Rkomi'),(3,'Fabio','Rizzo','Marracash'),(4,'Colson','Baker','MGK'),(5,'Joseph','Orison','Twenty One Pilots'),(6,'Travis','Baker','Sum 41'),(7,'Simone','Benussi','Mace'),(8,'Cosimo','Fini','Gue Pequeno'),(9,'Edoardo','Fontana','Silent Bob');
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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dischi_collezione`
--

LOCK TABLES `dischi_collezione` WRITE;
/*!40000 ALTER TABLE `dischi_collezione` DISABLE KEYS */;
INSERT INTO `dischi_collezione` VALUES (35,8,31),(36,8,32),(37,1,33),(38,2,34),(39,2,33),(40,2,35),(41,3,36),(42,1,37),(43,3,38),(44,3,39),(45,4,40),(46,4,41),(47,5,42),(48,5,43),(49,6,44),(50,6,45),(51,6,46),(52,7,47),(53,1,48),(54,8,48);
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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disco`
--

LOCK TABLES `disco` WRITE;
/*!40000 ALTER TABLE `disco` DISABLE KEYS */;
INSERT INTO `disco` VALUES (31,1,'68','2020'),(32,1,'Gemelli','2021'),(33,5,'Vessel','2013'),(34,5,'Blurryface','2015'),(35,5,'Trench','2018'),(36,4,'Tickets To My Downfall','2020'),(37,9,'Piano B','2020'),(38,7,'OBE','2021'),(39,7,'Non Mi Fido','2029'),(40,8,'Bravo Ragazzo','2013'),(41,8,'Vero','2015'),(42,6,'All Killer,No Filter','2001'),(43,6,'Does This Look Infected?','2002'),(44,2,'Dasein Sollen','2016'),(45,2,'Io In Terra','2017'),(46,2,'Taxi Driver','2021'),(47,3,'Status','2016'),(48,1,'66','2016');
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
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tracce_disco`
--

LOCK TABLES `tracce_disco` WRITE;
/*!40000 ALTER TABLE `tracce_disco` DISABLE KEYS */;
INSERT INTO `tracce_disco` VALUES (89,30,88),(90,30,89),(91,30,90),(92,30,91),(93,30,92),(94,30,93),(95,31,94),(96,31,95),(97,31,96),(98,31,97),(99,32,98),(100,32,99),(101,32,100),(102,32,101),(103,32,102),(104,32,103),(105,33,104),(106,33,105),(107,33,106),(108,33,107),(109,33,108),(110,34,109),(111,34,110),(112,34,111),(113,34,112),(114,34,113),(115,34,114),(116,34,115),(117,35,116),(118,35,117),(119,35,118),(120,35,119),(121,36,120),(122,36,121),(123,36,122),(124,36,123),(125,36,124),(126,36,125),(127,37,126),(128,37,127),(129,37,128),(130,38,129),(131,38,130),(132,38,131),(133,38,132),(134,38,133),(135,39,134),(136,39,135),(137,40,136),(138,40,137),(139,40,138),(140,40,139),(141,40,140),(142,41,141),(143,41,142),(144,41,143),(145,41,144),(146,41,145),(147,41,146),(148,41,147),(149,42,148),(150,42,149),(151,42,150),(152,43,151),(153,43,152),(154,44,153),(155,44,154),(156,44,155),(157,44,156),(158,45,157),(159,45,158),(160,45,159),(161,45,160),(162,46,161),(163,46,162),(164,46,163),(165,46,164),(166,47,165),(167,47,166),(168,47,167),(169,48,168),(170,48,169),(171,48,170),(172,48,171),(173,48,172),(174,48,173);
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
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traccia`
--

LOCK TABLES `traccia` WRITE;
/*!40000 ALTER TABLE `traccia` DISABLE KEYS */;
INSERT INTO `traccia` VALUES (88,'EGO',257),(89,'Lei no',231),(90,'Disgusting',201),(91,'Noia',193),(92,'QT',227),(93,'Bella',220),(94,'68',203),(95,'Simba',243),(96,'Tosse',182),(97,'La paura',164),(98,'Vivo',203),(99,'Superclassico',204),(100,'Puro Sinaloa',220),(101,'Morto dentro',164),(102,'Ferma a guardare',219),(103,'Cigni',200),(104,'Ode To Sleep',320),(105,'Holding on to You',231),(106,'House of Gold',259),(107,'Car Radio',199),(108,'Fake You Out',216),(109,'Stressed Out',320),(110,'Ride',231),(111,'Fairly Local',259),(112,'Tear in My Heart',199),(113,'Lane Boy',216),(114,'The Judge',237),(115,'Goner',204),(116,'Jumpsuit',301),(117,'My Blood',204),(118,'Cut My Lip',197),(119,'Leave The City',219),(120,'Kiss Kiss',230),(121,'Drunk Face',208),(122,'Forget Me Too',199),(123,'Concert For Aliens',206),(124,'Jawbreaker',212),(125,'Nothing Inside',219),(126,'Autostrada Del Sole',200),(127,'Questione Di Tempo',208),(128,'Hooligans',199),(129,'Colpa Tua',200),(130,'La Canzone Nostra',208),(131,'Senza Fiato',199),(132,'Dal Tramonto all\'Alba',203),(133,'Sirena',211),(134,'Non Mi Fido',219),(135,'Guidando Forte',210),(136,'Business',200),(137,'Bravo Ragazzo',208),(138,'Uno Come Me',201),(139,'Brivido',220),(140,'Fuori',238),(141,'Pequeno',197),(142,'Le Bimbe Piangono',210),(143,'Squalo',219),(144,'Equilibrio',223),(145,'Interstellar',205),(146,'Fuori Orario',216),(147,'Eravamo Re',205),(148,'Never Wake Up',204),(149,'Fat Lip',221),(150,'In Too Deep',227),(151,'The Hell Song',214),(152,'Still Waiting',201),(153,'Sul Serio',214),(154,'Sissignore',201),(155,'Oh Mama',237),(156,'Aereoplanini Di Carta',207),(157,'Io In Terra',220),(158,'Apnea',200),(159,'Mai Più',231),(160,'Milano Bachata',196),(161,'Partire Da te',220),(162,'Sopra Le Canzoni',200),(163,'Taxi Driver',231),(164,'10 Ragazze',196),(165,'Bentornato',194),(166,'Il Nostro Tempo',203),(167,'Vita Da Star',216),(168,'EGO',214),(169,'Lei no',142),(170,'Disgusting',231),(171,'Noia',204),(172,'QT',220),(173,'Bella',246);
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
INSERT INTO `utente` VALUES (1,'federico','federico123','0ec7baaf-dfc0-4e1d-970e-3af01e63955b'),(2,'davide','davide123',''),(3,'mario','mario123',NULL);
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

-- Dump completed on 2021-07-23 15:59:47
