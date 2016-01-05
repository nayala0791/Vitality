-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 20, 2015 at 12:02 PM
-- Server version: 5.5.42-cll-lve
-- PHP Version: 5.4.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `japadb`
--

-- --------------------------------------------------------

--
-- Table structure for table `device_info`
--

CREATE TABLE IF NOT EXISTS `device_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `deviceRegistrationId` text NOT NULL,
  `IMEI` text NOT NULL,
  `device_model` varchar(100) NOT NULL,
  `lastUpdatedBy` text NOT NULL,
  `lastUpdatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

--
-- Dumping data for table `device_info`
--

INSERT INTO `device_info` (`id`, `userId`, `deviceRegistrationId`, `IMEI`, `device_model`, `lastUpdatedBy`, `lastUpdatedOn`) VALUES
(3, 4, 'APA91bEYxt5TBHPOV9O6qmhgN7Wg7ar4nqQqboi1sEHu-AoXpPb84Ml5E-e_se8idt-B_lR01yqiwd1mnMDbCPrniKS5nb4bu1cpRFsu5NiGfgFY2ewanbStaftfIdqNg2PbwNNQgeVyYh63wJQJn5rOurCP9Xk4fw', '355465059224473', 'Sony', '', '2015-10-03 06:08:14');

-- --------------------------------------------------------

--
-- Table structure for table `donar_details`
--

CREATE TABLE IF NOT EXISTS `donar_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `blood_group` varchar(10) NOT NULL,
  `gender` smallint(1) NOT NULL,
  `number_of_donation` bigint(20) NOT NULL,
  `ready_to_donate` bit(1) NOT NULL DEFAULT b'1',
  `phonenumber` varchar(20) NOT NULL,
  `alt_phone_number` varchar(15) NOT NULL,
  `address` text NOT NULL,
  `last_known_location` varchar(50) NOT NULL,
  `last_known_location_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=11 ;

--
-- Dumping data for table `donar_details`
--

INSERT INTO `donar_details` (`id`, `user_id`, `blood_group`, `gender`, `number_of_donation`, `ready_to_donate`, `phonenumber`, `alt_phone_number`, `address`, `last_known_location`, `last_known_location_time`, `last_updated_on`, `last_updated_by`) VALUES
(2, 4, 'B ', 0, 0, b'1', '9880473930', '123456789', 'Bangalore', '12.77;77.88', '2015-10-03 12:30:25', '2015-09-12 15:27:25', 'admin'),
(4, 6, 'O+', 0, 0, b'1', '3828282828', '8283828282', 'Karwar', '17.77;77.88', '2015-10-03 12:39:35', '2015-09-27 20:43:31', 'admin'),
(5, 7, 'O+', 0, 0, b'1', '8794563215', '8794563215', 'Yellapur', '', '0000-00-00 00:00:00', '2015-09-27 20:46:13', 'admin'),
(6, 8, 'O+', 0, 0, b'1', '8794563215', '8794563215', 'Yellapur', '', '0000-00-00 00:00:00', '2015-09-27 20:47:10', 'admin'),
(7, 13, 'O+', 0, 0, b'1', '9194551978', '', '', '', '0000-00-00 00:00:00', '2015-10-14 00:50:38', 'admin'),
(8, 14, 'B+', 0, 0, b'1', '3369086555', '', '', '', '0000-00-00 00:00:00', '2015-10-15 05:25:27', 'admin'),
(9, 15, 'A-', 0, 0, b'1', '7044608119', '3362888767', '703 King Arthur Dr.', '', '0000-00-00 00:00:00', '2015-10-15 22:15:13', 'admin'),
(10, 16, 'A+', 0, 0, b'1', '9196755390', '', '1801 Trailwood Heights Lane', '', '0000-00-00 00:00:00', '2015-10-18 09:21:17', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `hospital`
--

CREATE TABLE IF NOT EXISTS `hospital` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `hospital_name` varchar(100) NOT NULL,
  `hospital_address` text NOT NULL,
  `hospital_license` varchar(50) NOT NULL,
  `speciality` varchar(100) NOT NULL,
  `phone_number` varchar(20) NOT NULL,
  `alternative_phonenumber` varchar(20) NOT NULL,
  `last_updatedon` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `hospital`
--

INSERT INTO `hospital` (`id`, `user_id`, `hospital_name`, `hospital_address`, `hospital_license`, `speciality`, `phone_number`, `alternative_phonenumber`, `last_updatedon`, `last_update`) VALUES
(1, 12, 'Raghav', '123', '123', 'Cardiologist', '8123387976', '8123387976', '2015-10-02 20:31:10', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `hospital_donar_request`
--

CREATE TABLE IF NOT EXISTS `hospital_donar_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `hospital_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `requested_dateTime` timestamp NULL DEFAULT NULL,
  `has_user_accepted` bit(1) NOT NULL,
  `accepted_date_time` timestamp NULL DEFAULT NULL,
  `is_donation_successfull` bit(1) NOT NULL,
  `last_updated_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(50) NOT NULL DEFAULT 'admin',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

--
-- Dumping data for table `hospital_donar_request`
--

INSERT INTO `hospital_donar_request` (`id`, `hospital_id`, `user_id`, `requested_dateTime`, `has_user_accepted`, `accepted_date_time`, `is_donation_successfull`, `last_updated_on`, `last_updated_by`) VALUES
(1, 12, 4, '2015-10-03 13:11:55', b'0', '2015-10-03 14:45:21', b'0', '2015-10-03 06:11:55', 'admin'),
(2, 12, 4, '2015-10-03 13:12:53', b'1', '2015-10-03 13:50:16', b'0', '2015-10-03 06:12:53', 'admin');

-- --------------------------------------------------------

--
-- Table structure for table `pushNotificationTable`
--

CREATE TABLE IF NOT EXISTS `pushNotificationTable` (
  `id` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  `message` text NOT NULL,
  `isDelivered` bit(1) NOT NULL,
  `deliveredDateTime` date NOT NULL,
  `lastUpdateBy` text NOT NULL,
  `lastpdatedOn` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `User_Credntials`
--

CREATE TABLE IF NOT EXISTS `User_Credntials` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `User_Name` varchar(10) NOT NULL,
  `Email` varchar(50) NOT NULL,
  `Password` varchar(15) NOT NULL,
  `Temp_Password` varchar(15) NOT NULL,
  `Type` bit(1) NOT NULL DEFAULT b'0',
  `is_Active` bit(1) NOT NULL DEFAULT b'0',
  `do_change_password` bit(1) NOT NULL DEFAULT b'0',
  `last_updataed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(50) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=17 ;

--
-- Dumping data for table `User_Credntials`
--

INSERT INTO `User_Credntials` (`Id`, `User_Name`, `Email`, `Password`, `Temp_Password`, `Type`, `is_Active`, `do_change_password`, `last_updataed_on`, `last_updated_by`) VALUES
(4, 'nitin@123', 'ns.mesta@gmail.com', 'nitin', '7ICQfzmG2W', b'0', b'1', b'1', '2015-09-12 08:27:25', ''),
(6, 'Raghav', 'raghav.karwar@gmail.com', 'NULL', 'oGXPb3g5sN', b'0', b'1', b'1', '2015-09-27 13:43:31', ''),
(8, 'Mithun', 'mithunbanavalikar@gmail.com', 'NULL', 'RAqPkcfIFe', b'0', b'0', b'1', '2015-09-27 13:47:10', ''),
(12, 'Raghav', 'raghav.soraba@gmail.com', 'raghav123', 'IjQmBnCa3N', b'1', b'1', b'0', '2015-10-02 13:31:10', ''),
(13, 'Pranavi Ra', 'p_ramesh@uncg.edu', 'NULL', '8DnJaNUzei', b'0', b'0', b'1', '2015-10-13 17:50:38', ''),
(14, 'Gillie', 'mcgillie@uncg.edu', 'NULL', '3dM7qTJrxP', b'0', b'0', b'1', '2015-10-14 22:25:27', ''),
(15, 'Nick Ayala', 'n_ayala@uncg.edu', 'NULL', 'MsKNYDVyOg', b'0', b'0', b'1', '2015-10-15 15:15:13', ''),
(16, 'Jeffrey', 'jeffreywolz@gmail.com', 'NULL', 'UzTjxue9Nd', b'0', b'0', b'1', '2015-10-18 02:21:17', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
