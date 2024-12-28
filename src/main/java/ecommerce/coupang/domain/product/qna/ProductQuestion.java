package ecommerce.coupang.domain.product.qna;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;
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
public class ProductQuestion extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_question_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@OneToOne(mappedBy = "productQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
	private Answer answer;

	@Column(name = "question_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private QuestionType type;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_answered", nullable = false)
	private boolean isAnswered = false;

}
