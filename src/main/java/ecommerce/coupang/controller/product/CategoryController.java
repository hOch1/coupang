package ecommerce.coupang.controller.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.domain.product.Category;
import ecommerce.coupang.dto.response.product.CategoryResponse;
import ecommerce.coupang.service.product.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "카테고리 API V1", description = "카테고리 관련 API")
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	@Operation(summary = "전체 카테고리 조회 API", description = "전체 카테고리를 조회합니다.")
	public ResponseEntity<List<CategoryResponse>> findAllCategories() {
		List<Category> categories = categoryService.findAll();
		return ResponseEntity.ok(categories.stream()
			.map(CategoryResponse::from)
			.toList());
	}
}
