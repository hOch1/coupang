package ecommerce.coupang.repository.product;

import ecommerce.coupang.repository.product.custom.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.coupang.common.aop.log.LogLevel;
import ecommerce.coupang.domain.product.Product;

@LogLevel("ProductRepository")
public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

}
