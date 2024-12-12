package ecommerce.coupang.controller.product;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ecommerce.coupang.dto.response.product.OptionResponse;
import ecommerce.coupang.exception.CustomException;
import ecommerce.coupang.service.product.OptionValueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/options")
@RequiredArgsConstructor
@Tag(name = "옵션 API V1", description = "옵션관련 API")
public class OptionController {

	private final OptionValueService optionValueService;

	@GetMapping("/{categoryId}")
	@Operation(summary = "카테고리 관련 옵션 조회 API", description = "해당 카테고리와 하위 카테고리 관련 옵션을 조회합니다.")
	public ResponseEntity<List<OptionResponse>> findOptions(
		@PathVariable Long categoryId) throws CustomException {

		return ResponseEntity.ok(optionValueService.findCategoryOption(categoryId));
	}
}
