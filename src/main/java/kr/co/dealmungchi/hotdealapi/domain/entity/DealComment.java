package kr.co.dealmungchi.hotdealapi.domain.entity;

import kr.co.dealmungchi.hotdealapi.common.exception.DomainException;
import kr.co.dealmungchi.hotdealapi.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table("deal_comments")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Setter
public class DealComment {
	@Id
	private Long id;

	@Column("hotdeals_id")
	private Long hotdealsId;

	@Column("parent_id")
	private Long parentId;

	@Column("user_id")
	private Long userId;

	@Column("content")
	private String content;

	@Column("is_deleted")
	private Boolean isDeleted;

	@CreatedDate
	@Column("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column("updated_at")
	private LocalDateTime updatedAt;

	@Transient
	private List<DealComment> replies = new ArrayList<>();

	public void setReplies(List<DealComment> replies) {
		this.replies = replies != null ? replies : new ArrayList<>();
	}

	public boolean isReply() {
		return parentId != null;
	}

	public void validateExistence() {
		if (this.id == null) {
			throw new DomainException(ErrorCode.COMMENT_NOT_FOUND);
		}
	}

	public void validateParentComment() {
		if (this.isReply() && this.parentId == null) {
			throw new DomainException(ErrorCode.PARENT_COMMENT_NOT_FOUND);
		}
	}
}