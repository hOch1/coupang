package ecommerce.coupang.dto.request.store;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateStoreRequest {

	@NotBlank(message = "상점 이름을 입력해주세요.")
	private final String name;

	@NotBlank(message = "상점 설명을 입력해주세요.")
	private final String description;

	@NotBlank(message = "사업자등록번호를 입력해주세요.")
	private final String storeNumber;
}
