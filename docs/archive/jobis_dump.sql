-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: j10b309.p.ssafy.io    Database: jobis_db
-- ------------------------------------------------------
-- Server version	8.0.36

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
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `company_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `activity` double DEFAULT NULL,
  `efficiency` double DEFAULT NULL,
  `employee_cnt` int DEFAULT NULL,
  `growth` double DEFAULT NULL,
  `profitability` double DEFAULT NULL,
  `stability` double DEFAULT NULL,
  `youth_company` bit(1) NOT NULL,
  `capital` bigint DEFAULT NULL,
  `views` bigint NOT NULL DEFAULT '0',
  `address` varchar(255) DEFAULT NULL,
  `form` varchar(255) DEFAULT NULL,
  `found_at` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24873 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `company_scrap`
--

DROP TABLE IF EXISTS `company_scrap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company_scrap` (
  `company_scrap_id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `company_id` int NOT NULL,
  `users_id` int NOT NULL,
  PRIMARY KEY (`company_scrap_id`),
  KEY `FKsvicxwnfxigysjdn0dxwryiof` (`users_id`),
  CONSTRAINT `FKsvicxwnfxigysjdn0dxwryiof` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `financial_rate`
--

DROP TABLE IF EXISTS `financial_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_rate` (
  `first_quarter` float NOT NULL,
  `second_quarter` float NOT NULL,
  `third_quarter` float NOT NULL,
  `company_id` int NOT NULL,
  `financial_rate_id` int NOT NULL AUTO_INCREMENT,
  `asset_rate_type` enum('SALES_REVENUE_GROWTH_RATE','OPERATING_INCOME_GROWTH_RATE','NET_INCOME_GROWTH_RATE','EQUITY_GROWTH_RATE','INVENTORY_ASSETS_GROWTH_RATE','TOTAL_ASSETS_GROWTH_RATE','NET_PROFIT_MARGIN','OPERATING_MARGIN','RETURN_ON_ASSETS_ROA','RETURN_ON_EQUITY_ROE','STABILITY_INDICATOR','QUICK_RATIO','DEBT_RATIO','EQUITY_RATIO','TANGIBLE_ASSETS_TURNOVER_RATE','EQUITY_TURNOVER_RATE','INVENTORY_TURNOVER_RATE','ACCOUNTS_RECEIVABLE_TURNOVER_RATE','TOTAL_CAPITAL_TURNOVER_RATE') NOT NULL,
  PRIMARY KEY (`financial_rate_id`),
  KEY `FK3datexmw21924cpixp8qao025` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=471239 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `financial_statement`
--

DROP TABLE IF EXISTS `financial_statement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financial_statement` (
  `company_id` bigint NOT NULL,
  `financial_statement_id` int NOT NULL AUTO_INCREMENT,
  `first_quarter` bigint NOT NULL,
  `second_quarter` bigint NOT NULL,
  `third_quarter` bigint NOT NULL,
  `asset_value_type` enum('CURRENT_ASSETS','NON_CURRENT_ASSETS','TOTAL_ASSETS','CURRENT_LIABILITIES','NON_CURRENT_LIABILITIES','TOTAL_LIABILITIES','CAPITAL_STOCK','ADDITIONAL_PAID_IN_CAPITAL','RETAINED_EARNINGS','CAPITAL_ADJUSTMENTS','TOTAL_EQUITY','SALES_REVENUE','GROSS_PROFIT','OPERATING_INCOME','NON_OPERATING_INCOME','NON_OPERATING_EXPENSES','NET_INCOME','STATEMENT_OF_CASH_FLOWS','CASH_FLOWS_FROM_OPERATING_ACTIVITIES','CASH_FLOWS_FROM_INVESTING_ACTIVITIES','CASH_FLOWS_FROM_FINANCING_ACTIVITIES','NET_INCREASE_DECREASE_IN_CASH','CASH_AT_END_OF_PERIOD') NOT NULL,
  PRIMARY KEY (`financial_statement_id`),
  KEY `FK6b0i14rf5v2jt2yrtm55r9bia` (`company_id`),
  CONSTRAINT `FK6b0i14rf5v2jt2yrtm55r9bia` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=570447 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `company_id` bigint NOT NULL,
  `news_id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `date` varchar(255) NOT NULL,
  `link` text NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`news_id`),
  KEY `FKqj83qt93qlwj8p3tq9hnss1p7` (`company_id`),
  CONSTRAINT `FKqj83qt93qlwj8p3tq9hnss1p7` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patent`
--

DROP TABLE IF EXISTS `patent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patent` (
  `company_id` bigint NOT NULL,
  `patent_id` bigint NOT NULL AUTO_INCREMENT,
  `patent_date` varchar(255) NOT NULL,
  `patent_name` varchar(255) NOT NULL,
  `patent_summary` text NOT NULL,
  PRIMARY KEY (`patent_id`),
  KEY `FK8wu91k90oi4wo6iie9a85c0i4` (`company_id`),
  CONSTRAINT `FK8wu91k90oi4wo6iie9a85c0i4` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=153875 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quarter_info`
--

DROP TABLE IF EXISTS `quarter_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `quarter_info` (
  `company_id` bigint NOT NULL,
  `quarter_info_id` int NOT NULL AUTO_INCREMENT,
  `first_quarter_date` varchar(255) NOT NULL,
  `second_quarter_date` varchar(255) NOT NULL,
  `third_quarter_date` varchar(255) NOT NULL,
  PRIMARY KEY (`quarter_info_id`),
  UNIQUE KEY `UK_1vdxrjnltjtcnv4gtxsirc3wt` (`company_id`),
  CONSTRAINT `FKqwa0dnyvtw8d9hmris7l43mvr` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24806 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recommendation`
--

DROP TABLE IF EXISTS `recommendation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recommendation` (
  `recommendation_id` int NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `worldcup_id` bigint NOT NULL,
  PRIMARY KEY (`recommendation_id`),
  KEY `FKp9of3a6wysylygy4qjolnbb9n` (`company_id`),
  KEY `FKs981h69wkdc1op98va2604vee` (`worldcup_id`),
  CONSTRAINT `FKp9of3a6wysylygy4qjolnbb9n` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`),
  CONSTRAINT `FKs981h69wkdc1op98va2604vee` FOREIGN KEY (`worldcup_id`) REFERENCES `worldcup` (`worldcup_id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tendency`
--

DROP TABLE IF EXISTS `tendency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tendency` (
  `tendency_id` int NOT NULL AUTO_INCREMENT,
  `activity` double DEFAULT NULL,
  `efficiency` double DEFAULT NULL,
  `growth` double DEFAULT NULL,
  `profitability` double DEFAULT NULL,
  `stability` double DEFAULT NULL,
  `users_id` int NOT NULL,
  PRIMARY KEY (`tendency_id`),
  KEY `FKchfsjtuqh1xkfkar4jl6frxdb` (`users_id`),
  CONSTRAINT `FKchfsjtuqh1xkfkar4jl6frxdb` FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `social_id` varchar(255) DEFAULT NULL,
  `social_type` enum('GOOGLE','NAVER','KAKAO') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `worldcup`
--

DROP TABLE IF EXISTS `worldcup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `worldcup` (
  `activity` double DEFAULT NULL,
  `efficiency` double DEFAULT NULL,
  `growth` double DEFAULT NULL,
  `profitability` double DEFAULT NULL,
  `stability` double DEFAULT NULL,
  `worldcup_id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `img` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`worldcup_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `youth_company`
--

DROP TABLE IF EXISTS `youth_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `youth_company` (
  `company_id` bigint NOT NULL,
  `youth_company_id` int NOT NULL AUTO_INCREMENT,
  `business_lounge` bit(1) NOT NULL,
  `cafeteria` bit(1) NOT NULL,
  `childcare_facility` bit(1) NOT NULL,
  `company_club` bit(1) NOT NULL,
  `dorm_commute_bus` bit(1) NOT NULL,
  `employment_rate` float NOT NULL,
  `flexible_work_arrangement` bit(1) NOT NULL,
  `go_to_school_later` bit(1) NOT NULL,
  `gym` bit(1) NOT NULL,
  `nine_to_six` bit(1) NOT NULL,
  `refresh_holiday` bit(1) NOT NULL,
  `support_child` bit(1) NOT NULL,
  `support_culture_life` bit(1) NOT NULL,
  `support_education` bit(1) NOT NULL,
  `support_self_development` bit(1) NOT NULL,
  `support_something_else` bit(1) NOT NULL,
  `youth_hire_increasing_cnt` float NOT NULL,
  `youth_hire_increasing_rate` float NOT NULL,
  `youth_hire_rate` float NOT NULL,
  `youth_rate` float NOT NULL,
  `salary` bigint NOT NULL,
  PRIMARY KEY (`youth_company_id`),
  UNIQUE KEY `UK_h2ty9jbla6xt0p0gtpj24gj49` (`company_id`),
  CONSTRAINT `FK8j64qmxjncahuwlpq8snsl8y8` FOREIGN KEY (`company_id`) REFERENCES `company` (`company_id`)
) ENGINE=InnoDB AUTO_INCREMENT=865 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-04  9:05:30
