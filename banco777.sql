-- --------------------------------------------------------
-- Servidor:                     localhost
-- Versão do servidor:           5.5.20 - MySQL Community Server (GPL)
-- OS do Servidor:               Win32
-- HeidiSQL Versão:              12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Copiando estrutura do banco de dados para db_777
CREATE DATABASE IF NOT EXISTS `db_777` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `db_777`;

-- Copiando estrutura para tabela db_777.tb01_apostas
CREATE TABLE IF NOT EXISTS `tb01_apostas` (
  `tb01_id` int(11) NOT NULL AUTO_INCREMENT,
  `tb01_nome` varchar(50) NOT NULL,
  `tb01_data` date NOT NULL,
  `tb01_hora` time NOT NULL,
  `tb01_vl_aposta` varchar(50) NOT NULL DEFAULT '0.000000',
  `tb01_ganho` varchar(50) NOT NULL DEFAULT '0.000000',
  `tb01_status` varchar(50) NOT NULL DEFAULT '0.000000',
  PRIMARY KEY (`tb01_id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=latin1;

-- Copiando dados para a tabela db_777.tb01_apostas: ~2 rows (aproximadamente)
INSERT INTO `tb01_apostas` (`tb01_id`, `tb01_nome`, `tb01_data`, `tb01_hora`, `tb01_vl_aposta`, `tb01_ganho`, `tb01_status`) VALUES
	(64, 'Juliano', '2023-11-25', '10:29:02', '3.0', '3.0', 'Emitido Java'),
	(65, 'Juliano', '2023-11-25', '10:34:08', 'R$ 5,00', 'R$ 3,00', 'Emitido Java');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
