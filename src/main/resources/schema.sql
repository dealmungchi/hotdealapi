CREATE TABLE IF NOT EXISTS `providers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `provider_type` enum('ARCA','CLIEN','COOLANDJOY','DAMOANG','FMKOREA','PPOMPPU','PPOMPPUEN','QUASAR','RULIWEB','DEALBADA','MISSYCOUPONS','MALLTAIL','BBASAK','CITY','EOMISAE','ZOD') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3q2qqkf03yqsbwchcskhlb12f` (`provider_type`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `hotdeals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `link` varchar(255) NOT NULL,
  `post_id` varchar(255) NOT NULL,
  `posted_at` varchar(255) DEFAULT NULL,
  `price` varchar(255) DEFAULT NULL,
  `thumbnail_hash` varchar(255) DEFAULT NULL,
  `thumbnail_link` varchar(2000) DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `provider_id` bigint NOT NULL,
  `view_count` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3fkmjvck2poucqtdebmedkssb` (`link`),
  KEY `FKje3gg8qn53f8d4n3ffhtx0l9s` (`provider_id`),
  CONSTRAINT `FKje3gg8qn53f8d4n3ffhtx0l9s` FOREIGN KEY (`provider_id`) REFERENCES `providers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1421 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `deal_comments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `hotdeals_id` bigint NOT NULL,
  `parent_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `content` text NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `hotdeals_id` (`hotdeals_id`),
  KEY `idx_deal_comments_parent_id` (`parent_id`),
  CONSTRAINT `deal_comments_ibfk_1` FOREIGN KEY (`hotdeals_id`) REFERENCES `hotdeals` (`id`),
  CONSTRAINT `deal_comments_ibfk_2` FOREIGN KEY (`parent_id`) REFERENCES `deal_comments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;