package ecommerce.coupang.repository.product.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.inquiry.ProductInquiry;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@LogLevel("ProductInquiryRepository")
public interface ProductInquiryRepository extends JpaRepository<ProductInquiry, Long> {

    @Query("select pi from ProductInquiry pi " +
            "join fetch pi.product p " +
            "join fetch p.store s " +
            "join fetch s.member m " +
            "where pi.id = :productInquiryId")
    Optional<ProductInquiry> findByIdWithMember(Long productInquiryId);

    @Query("select pi from ProductInquiry pi " +
            "join fetch pi.product p " +
            "join fetch pi.answer a " +     // OneToOne N+1 문제
            "where pi.member.id = :memberId")
    List<ProductInquiry> findByMemberId(Long memberId);

    @Query("select pi from ProductInquiry pi " +
            "join fetch pi.product p " +
            "join fetch pi.answer a " +     // OneToOne N+1 문제
            "where p.id = :productId")
    List<ProductInquiry> findByProductIdWithMember(Long productId);
}
