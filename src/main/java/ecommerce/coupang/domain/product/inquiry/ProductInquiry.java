package ecommerce.coupang.domain.product.inquiry;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.inquiry.CreateInquiryRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateInquiryRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProductInquiry extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_inquiry_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "productInquiry", cascade = CascadeType.ALL, orphanRemoval = true)
	private Answer answer;

	@Column(name = "inquiry_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private InquiryType type;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_answered", nullable = false)
	private boolean isAnswered = false;

	public ProductInquiry(Product product, Member member, InquiryType type, String content) {
		this.product = product;
		this.member = member;
		this.type = type;
		this.content = content;
	}

	public static ProductInquiry of(CreateInquiryRequest request, Product product, Member member) {
		return new ProductInquiry(
			product,
			member,
			request.getType(),
			request.getContent()
		);
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
		this.isAnswered = true;
	}

	/* 문의 수정 */
	public void update(UpdateInquiryRequest request) {
		this.content = request.getContent() != null ? request.getContent() : this.content;
	}

	public void validateInquiryOwner(Member member) throws CustomException {
		if (!this.member.equals(member))
			throw new CustomException(ErrorCode.FORBIDDEN);
	}
}
