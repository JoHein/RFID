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
-- Base de données :  `uno`
--
CREATE DATABASE IF NOT EXISTS `uno` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `uno`;

-- --------------------------------------------------------

--
-- Structure de la table `cards`
--

CREATE TABLE `cards` (
  `c_id` int(5) NOT NULL,
  `c_value` varchar(25) DEFAULT NULL,
  `c_color` varchar(25) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `deck`
--

CREATE TABLE `deck` (
  `d_t_id` int(5) NOT NULL,
  `d_m_id` int(5) NOT NULL,
  `d_c_id` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `games`
--

CREATE TABLE `games` (
  `g_id` int(5) NOT NULL,
  `g_nom` varchar(50) NOT NULL,
  `g_nbr_max_joueur` int(2) DEFAULT NULL,
  `g_nbr_max_ia` int(2) DEFAULT NULL,
  `g_etat` int(2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `hands_players_in_game`
--

CREATE TABLE `hands_players_in_game` (
  `h_id_match` int(5) NOT NULL,
  `h_id_user` int(5) NOT NULL,
  `h_id_card` int(5) NOT NULL,
  `h_tour` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `matchs`
--

CREATE TABLE `matchs` (
  `m_id` int(7) NOT NULL,
  `m_g_id` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `players_in_game`
--

CREATE TABLE `players_in_game` (
  `p_g_id` int(5) NOT NULL,
  `p_id_user` int(5) NOT NULL,
  `p_position` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `stack`
--

CREATE TABLE `stack` (
  `s_t_id` int(5) NOT NULL,
  `s_m_id` int(5) NOT NULL,
  `s_c_id` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `stats`
--

CREATE TABLE `stats` (
  `st_g_id` int(5) NOT NULL,
  `st_u_id` int(5) NOT NULL,
  `nbr_of_cards_in_hand` int(3) NOT NULL,
  `nbr_of_strokes` int(5) NOT NULL,
  `st_score` int(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `statut`
--

CREATE TABLE `statut` (
  `s_id` int(5) NOT NULL,
  `s_libelle` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `statut`
--

INSERT INTO `statut` (`s_id`, `s_libelle`) VALUES
  (4, 'admin'),
  (1, 'bot'),
  (2, 'guest'),
  (3, 'member');

-- --------------------------------------------------------

--
-- Structure de la table `turns`
--

CREATE TABLE `turns` (
  `t_id` int(7) NOT NULL,
  `t_m_id` int(7) NOT NULL,
  `t_sens` tinyint(1) NOT NULL,
  `id_user_ready` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `u_id` int(5) NOT NULL,
  `u_pseudo` varchar(30) NOT NULL,
  `u_email` varchar(50) DEFAULT NULL,
  `u_password` varchar(64) DEFAULT NULL,
  `u_statut` int(5) NOT NULL,
  `u_banned` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`u_id`, `u_pseudo`, `u_email`, `u_password`, `u_statut`, `u_banned`) VALUES
  (1, 'admin', 'admin@admin.fr', '4xB/NgBysNuegEkv5fQ7vg==', 4, 0),
  (2, 'nikotyx', 'nikotyx@gmail.com', 'Vt8Z74FXMoINApU+TDFQ6A==', 4, 1);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `cards`
--
ALTER TABLE `cards`
ADD PRIMARY KEY (`c_id`);

--
-- Index pour la table `deck`
--
ALTER TABLE `deck`
ADD PRIMARY KEY (`d_t_id`,`d_m_id`),
ADD KEY `FK_deck_match` (`d_m_id`);

--
-- Index pour la table `games`
--
ALTER TABLE `games`
ADD PRIMARY KEY (`g_id`),
ADD UNIQUE KEY `g_nom` (`g_nom`);

--
-- Index pour la table `hands_players_in_game`
--
ALTER TABLE `hands_players_in_game`
ADD PRIMARY KEY (`h_id_match`,`h_id_user`,`h_id_card`),
ADD KEY `fk_user_hand` (`h_id_user`),
ADD KEY `fk_card_hand` (`h_id_card`);

--
-- Index pour la table `matchs`
--
ALTER TABLE `matchs`
ADD PRIMARY KEY (`m_id`),
ADD KEY `fk_matchs_game` (`m_g_id`);

--
-- Index pour la table `players_in_game`
--
ALTER TABLE `players_in_game`
ADD PRIMARY KEY (`p_g_id`,`p_id_user`),
ADD KEY `fk_user_in_game` (`p_id_user`);

--
-- Index pour la table `stack`
--
ALTER TABLE `stack`
ADD PRIMARY KEY (`s_t_id`,`s_m_id`),
ADD KEY `FK_stack_match` (`s_m_id`);

--
-- Index pour la table `stats`
--
ALTER TABLE `stats`
ADD PRIMARY KEY (`st_g_id`,`st_u_id`),
ADD KEY `fk_score_user` (`st_u_id`);

--
-- Index pour la table `statut`
--
ALTER TABLE `statut`
ADD PRIMARY KEY (`s_id`),
ADD UNIQUE KEY `s_libelle` (`s_libelle`);

--
-- Index pour la table `turns`
--
ALTER TABLE `turns`
ADD PRIMARY KEY (`t_id`),
ADD KEY `fk_match_turns` (`t_m_id`),
ADD KEY `fk_user_turns` (`id_user_ready`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
ADD PRIMARY KEY (`u_id`),
ADD UNIQUE KEY `u_pseudo` (`u_pseudo`,`u_email`),
ADD KEY `fk_user_statut` (`u_statut`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `cards`
--
ALTER TABLE `cards`
MODIFY `c_id` int(5) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `games`
--
ALTER TABLE `games`
MODIFY `g_id` int(5) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `matchs`
--
ALTER TABLE `matchs`
MODIFY `m_id` int(7) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `statut`
--
ALTER TABLE `statut`
MODIFY `s_id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT pour la table `turns`
--
ALTER TABLE `turns`
MODIFY `t_id` int(7) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
MODIFY `u_id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `deck`
--
ALTER TABLE `deck`
ADD CONSTRAINT `FK_deck_match` FOREIGN KEY (`d_m_id`) REFERENCES `matchs` (`m_id`),
ADD CONSTRAINT `FK_deck_turn` FOREIGN KEY (`d_t_id`) REFERENCES `turns` (`t_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `hands_players_in_game`
--
ALTER TABLE `hands_players_in_game`
ADD CONSTRAINT `fk_card_hand` FOREIGN KEY (`h_id_card`) REFERENCES `cards` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_match_hand` FOREIGN KEY (`h_id_match`) REFERENCES `matchs` (`m_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_user_hand` FOREIGN KEY (`h_id_user`) REFERENCES `users` (`u_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `matchs`
--
ALTER TABLE `matchs`
ADD CONSTRAINT `fk_matchs_game` FOREIGN KEY (`m_g_id`) REFERENCES `games` (`g_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `players_in_game`
--
ALTER TABLE `players_in_game`
ADD CONSTRAINT `fk_game_player` FOREIGN KEY (`p_g_id`) REFERENCES `games` (`g_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_user_in_game` FOREIGN KEY (`p_id_user`) REFERENCES `users` (`u_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `stack`
--
ALTER TABLE `stack`
ADD CONSTRAINT `FK_stack_match` FOREIGN KEY (`s_m_id`) REFERENCES `matchs` (`m_id`),
ADD CONSTRAINT `FK_stack_turn` FOREIGN KEY (`s_t_id`) REFERENCES `turns` (`t_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `stats`
--
ALTER TABLE `stats`
ADD CONSTRAINT `fk_score_game` FOREIGN KEY (`st_g_id`) REFERENCES `games` (`g_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_score_user` FOREIGN KEY (`st_u_id`) REFERENCES `users` (`u_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `turns`
--
ALTER TABLE `turns`
ADD CONSTRAINT `fk_match_turns` FOREIGN KEY (`t_m_id`) REFERENCES `matchs` (`m_id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_user_turns` FOREIGN KEY (`id_user_ready`) REFERENCES `users` (`u_id`);

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
ADD CONSTRAINT `fk_user_statut` FOREIGN KEY (`u_statut`) REFERENCES `statut` (`s_id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
