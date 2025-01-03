package ecommerce.coupang.domain.product.review;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.review.CreateReviewRequest;
import ecommerce.coupang.dto.request.product.review.UpdateReviewRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductReview extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_review_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "star", nullable = false)
	private int star = 0;

	@Column(name = "like_count", nullable = false)
	private int likeCount = 0;

	@OneToMany(mappedBy = "productReview", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReviewLike> likes = new ArrayList<>();

	public ProductReview(Member member, Product product, String content, int star) {
		this.member = member;
		this.product = product;
		this.content = content;
		this.star = star;
	}

	public static ProductReview create(CreateReviewRequest request, Product product, Member member) {
		return new ProductReview(
			member,
			product,
			request.getContent(),
			request.getStar()
		);
	}

	public void addLikes(ReviewLike newLike) {
		this.likes.add(newLike);
		increaseLikeCount();
	}

	public void increaseLikeCount() {
		this.likeCount++;
	}

	public void decreaseLikeCount() {
		this.likeCount--;
	}

	public void update(UpdateReviewRequest request) {
		this.content = request.getContent() != null ? request.getContent() : this.content;
		this.star = request.getStar() != null ? request.getStar().intValue() : this.star;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ProductReview that = (ProductReview)o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}
}
