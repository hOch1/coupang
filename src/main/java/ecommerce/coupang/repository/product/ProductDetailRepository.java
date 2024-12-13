package ecommerce.coupang.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.domain.product.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
