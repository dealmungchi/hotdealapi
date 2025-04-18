package kr.co.dealmungchi.hotdealapi.domain.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import kr.co.dealmungchi.hotdealapi.common.exception.DomainException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table("hotdeals")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotDeal {
	@Id
	private Long id;

	@Column("title")
	private String title;

	@Column("link")
	private String link;

	@Column("post_id")
	private String postId;

	@Column("thumbnail_link")
	private String thumbnailLink;

	@Column("thumbnail_hash")
	private String thumbnailHash;

	@Column("price")
	private String price;

	@Column("posted_at")
	private String postedAt;

	@Column("provider_id")
	private Long providerId;

	@Column("view_count")
	private Long viewCount;

	@CreatedDate
	@Column("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column("updated_at")
	private LocalDateTime updatedAt;

	@Transient
	private Provider provider;

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public void incrementViewCount() {
		this.viewCount = this.viewCount + 1;
	}

	public void validateExistence() {
		if (this.id == null) {
			throw new DomainException(ErrorCode.HOTDEAL_NOT_FOUND);
		}
	}
}