package ecommerce.coupang.repository.product.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.product.inquiry.ProductInquiry;

public interface ProductInquiryRepository extends JpaRepository<ProductInquiry, Long> {
}
