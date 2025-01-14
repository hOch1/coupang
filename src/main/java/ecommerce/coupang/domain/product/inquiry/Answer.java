package ecommerce.coupang.domain.product.inquiry;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.dto.request.product.inquiry.CreateAnswerRequest;
import ecommerce.coupang.dto.request.product.inquiry.UpdateAnswerRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Answer extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "answer_id")
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_inquiry_id", unique = true)
	private ProductInquiry productInquiry;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@Column(name = "answer", nullable = false)
	private String answer;

	public Answer(ProductInquiry productInquiry, Store store, String answer) {
		this.productInquiry = productInquiry;
		this.store = store;
		this.answer = answer;
	}

	public static Answer of(CreateAnswerRequest request, ProductInquiry productInquiry, Store store) {
		return new Answer(
				productInquiry,
				store,
				request.getAnswer()
		);
	}

	/* 답변 수정 */
	public void update(UpdateAnswerRequest request) {
		this.answer = request.getAnswer() != null ? request.getAnswer() : this.answer;
	}
}
