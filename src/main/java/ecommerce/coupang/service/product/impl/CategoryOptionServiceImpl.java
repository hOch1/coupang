package ecommerce.coupang.service.product.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.domain.product.CategoryOption;
import ecommerce.coupang.repository.product.CategoryOptionRepository;
import ecommerce.coupang.service.product.CategoryOptionService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryOptionServiceImpl implements CategoryOptionService {

	private final CategoryOptionRepository categoryOptionRepository;

	@Override
	public List<CategoryOption> findByCategory(Category category) {
		return categoryOptionRepository.findByCategory(category);
	}
}
