-- phpMyAdmin SQL Dump
-- version 4.5.3.1
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Sam 30 Avril 2016 à 11:01
-- Version du serveur :  5.7.10
-- Version de PHP :  5.6.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `rfid`
--
CREATE DATABASE IF NOT EXISTS `rfid` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `rfid`;

-- --------------------------------------------------------

--
-- Structure de la table `catalogue`
--

CREATE TABLE `catalogue` (
  `idCatalogue` int(11) NOT NULL,
  `nomCatalogue` varchar(40) NOT NULL,
  `auteur` varchar(50) DEFAULT NULL,
  `nbDispo` int(11) DEFAULT NULL,
  `nbTotal` int(11) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `categorie` varchar(50) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `catalogue`
--

INSERT INTO `catalogue` (`idCatalogue`, `nomCatalogue`, `auteur`, `nbDispo`, `nbTotal`, `type`, `categorie`) VALUES
  (1, 'Du cote de chez Swann', 'Proust', 2, 6, 'Roman', 'Triptyque'),
  (8, 'La chartreuse de Parme', 'Stendhal', 0, 0, 'Roman', 'Fiction'),
  (3, 'Cyrano de Bergerac', 'Rostan', 0, 0, 'Théatre', 'Comédie Drame'),
  (9, 'Le meilleur des mondes', 'Huxley', 0, 0, 'Roman', 'Anticipation'),
  (11, 'Germinal', 'Zola', 0, 0, 'Roman', 'Epanadiplose');

-- --------------------------------------------------------

--
-- Structure de la table `emprunt`
--

CREATE TABLE `emprunt` (
  `idEmprunt` int(11) NOT NULL,
  `uidProduit` varchar(8) NOT NULL,
  `uidUser` varchar(14) NOT NULL,
  `dateEmprunt` date DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `stock`
--

CREATE TABLE `stock` (
  `idStock` int(11) NOT NULL,
  `uidProduit` varchar(8) NOT NULL,
  `idCatalogue` int(11) NOT NULL,
  `dispo` tinyint(1) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `stock`
--

INSERT INTO `stock` (`idStock`, `uidProduit`, `idCatalogue`, `dispo`) VALUES
  (1, '1B419616', 1, 1),
  (10, 'CBE09516', 1, 1);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `idUser` int(11) NOT NULL,
  `nomUser` varchar(20) NOT NULL,
  `prenomUser` varchar(50) NOT NULL,
  `uidUser` varchar(14) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`idUser`, `nomUser`, `prenomUser`, `uidUser`) VALUES
  (1, 'Tyx', 'Niko', '04923D422B4680'),
  (5, 'Heinen', 'Jocelin', '0468708A083C80');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `catalogue`
--
ALTER TABLE `catalogue`
ADD PRIMARY KEY (`idCatalogue`);

--
-- Index pour la table `emprunt`
--
ALTER TABLE `emprunt`
ADD PRIMARY KEY (`idEmprunt`);

--
-- Index pour la table `stock`
--
ALTER TABLE `stock`
ADD PRIMARY KEY (`idStock`),
ADD UNIQUE KEY `uidProduit` (`uidProduit`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
ADD PRIMARY KEY (`idUser`),
ADD UNIQUE KEY `uidUser` (`uidUser`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `catalogue`
--
ALTER TABLE `catalogue`
MODIFY `idCatalogue` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT pour la table `emprunt`
--
ALTER TABLE `emprunt`
MODIFY `idEmprunt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT pour la table `stock`
--
ALTER TABLE `stock`
MODIFY `idStock` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;--


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
