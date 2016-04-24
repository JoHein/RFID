-- phpMyAdmin SQL Dump
-- version 4.5.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 24, 2016 at 12:39 PM
-- Server version: 5.7.9
-- PHP Version: 5.6.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rfid`
--

-- --------------------------------------------------------

--
-- Table structure for table `catalogue`
--

DROP TABLE IF EXISTS `catalogue`;
CREATE TABLE IF NOT EXISTS `catalogue` (
  `idCatalogue` int(11) NOT NULL AUTO_INCREMENT,
  `nomCatalogue` varchar(40) NOT NULL,
  `auteur` varchar(50) DEFAULT NULL,
  `nbDispo` int(11) DEFAULT NULL,
  `nbTotal` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `categorie` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idCatalogue`)
) ENGINE=MyISAM AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `catalogue`
--

INSERT INTO `catalogue` (`idCatalogue`, `nomCatalogue`, `auteur`, `nbDispo`, `nbTotal`, `type`, `categorie`) VALUES
(1, 'Du cote de chez Swann', 'Proust', 4, 6, 'Roman', 'Triptyque'),
(8, 'La chartreuse de Parme', 'Stendhal', 0, 0, 'Roman', 'Fiction'),
(3, 'Cyrano de Bergerac', 'Rostan', 0, 0, 'Théatre', 'Comédie Drame'),
(9, 'Le meilleur des mondes', 'Huxley', 0, 0, 'Roman', 'Anticipation'),
(11, 'Germinal', 'Zola', 0, 0, 'Roman', 'Epanadiplose');

-- --------------------------------------------------------

--
-- Table structure for table `emprunt`
--

DROP TABLE IF EXISTS `emprunt`;
CREATE TABLE IF NOT EXISTS `emprunt` (
  `idEmprunt` int(11) NOT NULL AUTO_INCREMENT,
  `uidProduit` varchar(8) NOT NULL,
  `uidUser` varchar(14) NOT NULL,
  PRIMARY KEY (`idEmprunt`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `emprunt`
--

INSERT INTO `emprunt` (`idEmprunt`, `uidProduit`, `uidUser`) VALUES
(1, '1B419616', '04923D422B4680');

-- --------------------------------------------------------

--
-- Table structure for table `stock`
--

DROP TABLE IF EXISTS `stock`;
CREATE TABLE IF NOT EXISTS `stock` (
  `idStock` int(11) NOT NULL AUTO_INCREMENT,
  `uidProduit` varchar(8) NOT NULL,
  `idCatalogue` int(11) NOT NULL,
  `dispo` tinyint(1) NOT NULL,
  PRIMARY KEY (`idStock`),
  UNIQUE KEY `uidProduit` (`uidProduit`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stock`
--

INSERT INTO `stock` (`idStock`, `uidProduit`, `idCatalogue`, `dispo`) VALUES
(1, '1B419616', 1, 0),
(10, 'CBE09516', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `nomUser` varchar(20) NOT NULL,
  `prenomUser` varchar(50) NOT NULL,
  `uidUser` varchar(14) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `uidUser` (`uidUser`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`idUser`, `nomUser`, `prenomUser`, `uidUser`) VALUES
(1, 'Niko', '', '04923D422B4680'),
(5, 'heinen', 'jocelin', '0468708A083C80');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
