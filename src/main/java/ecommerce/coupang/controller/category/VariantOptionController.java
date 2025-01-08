package ecommerce.coupang.controller.category;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.product.variant.VariantOption;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.option.VariantOptionResponse;
import ecommerce.coupang.service.product.option.VariantOptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/variant/options")
@RequiredArgsConstructor
@Tag(name = "변형 옵션 API V1", description = "변형 옵션 관련 API")
public class VariantOptionController {

	private final VariantOptionService variantOptionService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<Result<List<VariantOptionResponse>>> findCategoryOptions(
		@PathVariable Long categoryId) throws CustomException {

		List<VariantOption> variantOptions = variantOptionService.getVariantOption(categoryId);
		List<VariantOptionResponse> responses = variantOptions.stream()
			.map(VariantOptionResponse::from)
			.toList();

		return ResponseEntity.ok(new Result<>(responses, responses.size()));
	}
}
