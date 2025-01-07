package ecommerce.coupang.dto.request.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductSearchRequest {

    private String keyword;
    private Long categoryId;
    private Long storeId;
    private List<Long> categoryOptions;
    private List<Long> variantOptions;
}
