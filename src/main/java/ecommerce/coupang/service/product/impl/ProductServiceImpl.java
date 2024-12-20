package ecommerce.coupang.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.member.Store;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.Product;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.exception.ErrorCode;
import ecommerce.coupang.repository.member.StoreRepository;
import ecommerce.coupang.repository.product.ProductRepository;
import ecommerce.coupang.service.product.CategoryService;
import ecommerce.coupang.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductServiceImpl implements ProductService {

	@Override
	public Product createProduct(CreateProductRequest request, Member member) throws CustomException {
		return null;
	}

	@Override
	public Product updateProduct(UpdateProductRequest request, Long productId, Member member) throws CustomException {
		return null;
	}

	@Override
	public List<Product> findProductsByCategory(Long categoryId) throws CustomException {
		return List.of();
	}

	@Override
	public List<Product> findProductsByCategoryAndOptions(Long categoryId, List<Long> options) throws CustomException {
		return List.of();
	}

	@Override
	public List<Product> findProductsByStore(Long storeId) throws CustomException {
		return List.of();
	}

	@Override
	public List<Product> findProductsByStoreAndOptions(Long storeId, List<Long> options) throws CustomException {
		return List.of();
	}

	@Override
	public List<Product> findProductsByStoreAndCategory(Long storeId, Long categoryId) throws CustomException {
		return List.of();
	}

	@Override
	public List<Product> findProductsByStoreAndCategoryAndOptions(Long storeId, Long categoryId,
		List<Long> options) throws CustomException {
		return List.of();
	}

	@Override
	public Product findProduct(Long productId) throws CustomException {
		return null;
	}
}

