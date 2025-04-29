CREATE TABLE IF NOT EXISTS `providers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `provider_type` enum('ARCA','CLIEN','COOLANDJOY','DAMOANG','FMKOREA','PPOMPPU','PPOMPPUEN','QUASAR','RULIWEB','DEALBADA','MISSYCOUPONS','MALLTAIL','BBASAK','CITY','EOMISAE','ZOD') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_provider_type` (`provider_type`)
) ENGINE=InnoDB AUTO_INCREMENT=333 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_type` enum('ELECTRONICS_DIGITAL_PC','SOFTWARE_GAME','HOUSEHOLD_INTERIOR_KITCHEN','FOOD','CLOTHING_FASHION_ACCESSORIES','COSMETICS_BEAUTY','BOOKS_MEDIA_CONTENTS','CAMERA_PHOTO','VOUCHER_COUPON_POINT','BABY_CHILDCARE','PET','SPORTS_OUTDOOR_LEISURE','HEALTH_VITAMIN','TRAVEL_SERVICE','EVENT_ENTRY_VIRAL','SCHOOL_OFFICE_SUPPLIES','ETC') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_category_type` (`category_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `category_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_link` (`link`),
  KEY `key_provider_id` (`provider_id`),
  KEY `key_category_id` (`category_id`),
  CONSTRAINT `fk_provider_id` FOREIGN KEY (`provider_id`) REFERENCES `providers` (`id`),
  CONSTRAINT `fk_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
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
  KEY `key_hotdeals_id` (`hotdeals_id`),
  KEY `key_parent_id` (`parent_id`),
  CONSTRAINT `fk_hotdeals_id` FOREIGN KEY (`hotdeals_id`) REFERENCES `hotdeals` (`id`),
  CONSTRAINT `fk_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `deal_comments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;