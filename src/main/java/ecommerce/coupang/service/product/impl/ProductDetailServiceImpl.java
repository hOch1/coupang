package ecommerce.coupang.service.product.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.domain.product.ProductDetail;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.repository.product.ProductDetailRepository;
import ecommerce.coupang.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

	private final ProductDetailRepository productDetailRepository;

	@Override
	@Transactional
	public ProductDetail save(CreateProductRequest.CreateDetailRequest request, Product product) {
		return productDetailRepository.save(ProductDetail.create(request, product));
	}
}
