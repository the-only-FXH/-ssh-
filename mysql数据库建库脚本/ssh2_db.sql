/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50051
Source Host           : localhost:3306
Source Database       : ssh2_db

Target Server Type    : MYSQL
Target Server Version : 50051
File Encoding         : 65001

Date: 2014-07-17 04:36:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) default NULL,
  PRIMARY KEY  (`username`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('a', 'a');

-- ----------------------------
-- Table structure for `t_book`
-- ----------------------------
DROP TABLE IF EXISTS `t_book`;
CREATE TABLE `t_book` (
  `barcode` varchar(20) NOT NULL,
  `bookName` varchar(20) default NULL,
  `bookType` int(11) default NULL,
  `price` float default NULL,
  `count` int(11) default NULL,
  `publishDate` varchar(10) default NULL,
  `publish` varchar(20) default NULL,
  `introduction` varchar(500) default NULL,
  `bookPhoto` varchar(50) default NULL,
  PRIMARY KEY  (`barcode`),
  KEY `FKCB4C8FF4C78C387B` (`bookType`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_book
-- ----------------------------
INSERT INTO `t_book` VALUES ('TS001', '安卓Android程序设计', '1', '35.5', '12', '2013-07-03', '人民教育出版社', '这是一个介绍android系统的手机程序设计的书籍！', 'upload/42046828-0322-4a33-a747-dfcb962c7bd1.jpg');
INSERT INTO `t_book` VALUES ('TS002', 'html5网站设计', '1', '42.5', '18', '2013-10-23', '四川师范大学出版社', '介绍新的网站设计技术！', 'upload/8fe00c6c-286f-4d0f-8878-f011963bca06.jpg');
INSERT INTO `t_book` VALUES ('TS003', '中国近代史', '2', '19.5', '52', '2014-07-01', '成都理工大学出版社', '介绍中国近代史', 'upload/650898fb-2723-467c-b623-3f5add3dde28.jpg');

-- ----------------------------
-- Table structure for `t_booktype`
-- ----------------------------
DROP TABLE IF EXISTS `t_booktype`;
CREATE TABLE `t_booktype` (
  `bookTypeId` int(11) NOT NULL auto_increment,
  `bookTypeName` varchar(18) default NULL,
  `days` int(11) default NULL,
  PRIMARY KEY  (`bookTypeId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_booktype
-- ----------------------------
INSERT INTO `t_booktype` VALUES ('1', '计算机类', '30');
INSERT INTO `t_booktype` VALUES ('2', '历史类', '25');

-- ----------------------------
-- Table structure for `t_loaninfo`
-- ----------------------------
DROP TABLE IF EXISTS `t_loaninfo`;
CREATE TABLE `t_loaninfo` (
  `loadId` int(11) NOT NULL auto_increment,
  `book` varchar(20) default NULL,
  `reader` varchar(20) default NULL,
  `borrowDate` varchar(10) default NULL,
  `returnDate` varchar(10) default NULL,
  PRIMARY KEY  (`loadId`),
  KEY `FK29107D4923D05BDB` (`reader`),
  KEY `FK29107D49F442B167` (`book`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_loaninfo
-- ----------------------------
INSERT INTO `t_loaninfo` VALUES ('1', 'TS001', 'DZ001', '2014-07-01', '2014-07-10');
INSERT INTO `t_loaninfo` VALUES ('2', 'TS002', 'DZ002', '2014-07-01', '2014-07-15');

-- ----------------------------
-- Table structure for `t_reader`
-- ----------------------------
DROP TABLE IF EXISTS `t_reader`;
CREATE TABLE `t_reader` (
  `readerNo` varchar(20) NOT NULL,
  `readerType` int(11) default NULL,
  `readerName` varchar(20) default NULL,
  `sex` varchar(2) default NULL,
  `birthday` varchar(10) default NULL,
  `telephone` varchar(20) default NULL,
  `email` varchar(50) default NULL,
  `qq` varchar(20) default NULL,
  `address` varchar(80) default NULL,
  `photo` varchar(50) default NULL,
  PRIMARY KEY  (`readerNo`),
  KEY `FK4522970ECA9788EF` (`readerType`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_reader
-- ----------------------------
INSERT INTO `t_reader` VALUES ('DZ001', '1', '黄老师', '男', '1978-07-05', '13594105012', 'huang@163.com', '65105175', '四川成都红星路', 'upload/c6c86f8b-d7a5-47b0-806b-d6ff536e6022.jpg');
INSERT INTO `t_reader` VALUES ('DZ002', '2', '双鱼林', '男', '1994-07-07', '13558690869', 'linlin@163.com', '287307421', '四川成都十里店', 'upload/fe62edd5-4bc0-4abd-b9a8-2c23ce1a8243.jpg');

-- ----------------------------
-- Table structure for `t_readertype`
-- ----------------------------
DROP TABLE IF EXISTS `t_readertype`;
CREATE TABLE `t_readertype` (
  `readerTypeId` int(11) NOT NULL auto_increment,
  `readerTypeName` varchar(20) default NULL,
  `number` int(11) default NULL,
  PRIMARY KEY  (`readerTypeId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_readertype
-- ----------------------------
INSERT INTO `t_readertype` VALUES ('1', '教师类', '5');
INSERT INTO `t_readertype` VALUES ('2', '学生类', '3');
