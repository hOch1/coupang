package ecommerce.coupang.controller.category;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.dto.response.Result;
import ecommerce.coupang.dto.response.option.AllOptionResponse;
import ecommerce.coupang.service.product.option.CategoryOptionService;
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
public class CategoryOptionController {

    private final CategoryOptionService categoryOptionService;

    @GetMapping("/{categoryId}")
    public ResponseEntity<Result<List<AllOptionResponse.CategoryOptionResponse>>> findCategoryOptions(
            @PathVariable Long categoryId) throws CustomException {

        categoryOptionService.getCategoryOption(categoryId);
        // TODO
    }
}
