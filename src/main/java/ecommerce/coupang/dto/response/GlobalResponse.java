package ecommerce.coupang.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalResponse<T> {

	private Integer count;
	private T data;

	public GlobalResponse(T data) {
		this.data = data;
		this.count = null;
	}

	public GlobalResponse(T data, Integer count) {
		this.count = count;
		this.data = data;
	}
}
