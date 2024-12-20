package ecommerce.coupang.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

	private Integer count;
	private T data;

	public Result(T data) {
		this.data = data;
		this.count = null;
	}

	public Result(T data, Integer count) {
		this.count = count;
		this.data = data;
	}
}
