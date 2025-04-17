package kr.co.dealmungchi.hotdealapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("providers")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    @Id
    private Long id;
    
    @Column("provider_type")
    private ProviderType providerType;
    
    public enum ProviderType {
        ARCA, CLIEN, COOLANDJOY, DAMOANG, FMKOREA, PPOMPPU, PPOMPPUEN, 
        QUASAR, RULIWEB, DEALBADA, MISSYCOUPONS, MALLTAIL, 
        BBASAK, CITY, EOMISAE, ZOD
    }
}