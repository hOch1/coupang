package ecommerce.coupang.domain.store;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ecommerce.coupang.common.exception.CustomException;
import ecommerce.coupang.common.exception.ErrorCode;
import ecommerce.coupang.domain.member.Member;
import ecommerce.coupang.domain.product.Product;

class StoreTest {

	private final Member member = mock(Member.class);
	private final Product product = mock(Product.class);
	private Store store;

	@BeforeEach
	void beforeEach() {
		store = new Store("store", "description", "00000", member);
	}

	@Test
	void update() {

	}

	@Test
	@DisplayName("상점 삭제 테스트")
	void delete() {
		store.addProducts(product);

		store.delete();

		verify(product).delete();
		assertThat(store.isActive()).isFalse();
	}

	@Test
	@DisplayName("상점 주인 검증 성공")
	void validateOwner() {
		assertDoesNotThrow(() -> store.validateOwner(member));
	}

	@Test
	@DisplayName("상점 주인 검증 실패")
	void validateOwner_fail() {
		Member other = mock(Member.class);

		CustomException customException = assertThrows(CustomException.class, () -> store.validateOwner(other));

		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.FORBIDDEN);
	}
}