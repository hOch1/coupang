package ecommerce.coupang.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

	private T data;
	private Integer count;		// 현재 데이터 개수
	private Integer page;  	 	// 현재 페이지 번호
	private Integer pageSize; 	// 페이지 크기
	private Integer totalPages; // 총 페이지 수
	private Long totalCount;   	// 전체 데이터 수

	public Result(T data) {
		this.data = data;
		this.count = null;
	}

	public Result(T data, Integer count) {
		this.count = count;
		this.data = data;
	}

	public Result(T data, Integer count, Integer page, Integer pageSize, Integer totalPages, Long totalCount) {
		this.data = data;
		this.count = count;
		this.page = page;
		this.pageSize = pageSize;
		this.totalPages = totalPages;
		this.totalCount = totalCount;
	}
}
