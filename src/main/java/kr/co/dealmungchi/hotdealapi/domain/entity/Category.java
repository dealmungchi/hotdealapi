package kr.co.dealmungchi.hotdealapi.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("categories")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private Long id;

    @Column("category_type")
    private CategoryType categoryType;

    public enum CategoryType {
        ELECTRONICS_DIGITAL_PC, SOFTWARE_GAME, HOUSEHOLD_INTERIOR_KITCHEN, 
        FOOD, CLOTHING_FASHION_ACCESSORIES, COSMETICS_BEAUTY, 
        BOOKS_MEDIA_CONTENTS, CAMERA_PHOTO, VOUCHER_COUPON_POINT, 
        BABY_CHILDCARE, PET, SPORTS_OUTDOOR_LEISURE, 
        HEALTH_VITAMIN, TRAVEL_SERVICE, EVENT_ENTRY_VIRAL, 
        SCHOOL_OFFICE_SUPPLIES, ETC
    }
} 