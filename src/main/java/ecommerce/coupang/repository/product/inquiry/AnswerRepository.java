package ecommerce.coupang.repository.product.inquiry;

import java.util.Optional;

import ecommerce.coupang.aop.log.LogLevel;
import ecommerce.coupang.domain.product.inquiry.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@LogLevel("AnswerRepository")
public interface AnswerRepository extends JpaRepository<Answer, Long> {

	@Query("select a from Answer a "
		+ "join fetch a.store s "
		+ "where a.productInquiry.id = :inquiryId")
	Optional<Answer> findByInquiryId(Long inquiryId);

	@Query("select a from Answer a "
		+ "join fetch a.store s "
		+ "join fetch s.member m "
		+ "where a.id = :answerId ")
	Optional<Answer> findByIdWithMember(Long answerId);
}
