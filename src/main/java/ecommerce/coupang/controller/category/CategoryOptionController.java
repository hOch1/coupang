package ecommerce.coupang.controller.category;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.domain.category.CategoryOption;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.option.CategoryOptionResponse;
import ecommerce.coupang.service.category.CategoryService;
import ecommerce.coupang.service.product.option.CategoryOptionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category/options")
@RequiredArgsConstructor
@Tag(name = "카테고리 옵션 API V1", description = "카테고리 옵션 관련 API")
public class CategoryOptionController {

    private final CategoryService categoryService;
    private final CategoryOptionService categoryOptionService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<Result<List<CategoryOptionResponse>>> findCategoryOptions(
            @PathVariable Long categoryId) throws CustomException {

        List<CategoryOption> categoryOptions = categoryOptionService.getCategoryOption(categoryId);
        List<CategoryOptionResponse> responses = categoryOptions.stream()
            .map(CategoryOptionResponse::from)
            .toList();
        return ResponseEntity.ok(new Result<>(responses, responses.size()));
    }
}
