-- phpMyAdmin SQL Dump
-- version 4.1.12
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 31 Mar 2017 pada 16.33
-- Versi Server: 5.6.16
-- PHP Version: 5.5.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `lazday_tentangkamudev1`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `image` text NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=14 ;

--
-- Dumping data untuk tabel `category`
--

INSERT INTO `category` (`category_id`, `name`, `image`) VALUES
(1, 'Motivasi', 'motivasi.png'),
(2, 'Inspirasi', 'inspirasi.png'),
(3, 'Hubungan', 'hubungan.png'),
(4, 'Kehidupan', 'kehidupan.png'),
(5, 'Masa depan', 'masa-depan.png'),
(6, 'Galau', 'galau.png'),
(7, 'Gaya hidup', 'gaya-hidup.png'),
(8, 'Tips', 'tips.png'),
(9, 'Hiburan', 'hiburan.png'),
(10, 'Tentang kamu', 'tentang-kamu.png'),
(11, 'Unek-unek', 'unek-unek.png'),
(12, 'Sukses', 'sukses.png'),
(13, 'Humor', 'humor.png');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
