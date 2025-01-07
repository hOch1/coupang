package ecommerce.coupang.dto.request;

import lombok.Data;

@Data
public class PagingRequest {
    private int page = 0;
    private int pageSize = 20;
}
