package ecommerce.coupang.domain.product;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long id;

	@Column(name = "type", nullable = false, unique = true)
	private String type;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "level", nullable = false)
	private int level;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Category parent;

	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Category> children = new ArrayList<>();

	/**
	 * 최하위 카테고리 확인
	 * @return 자식 카테고리가 없으면 true, 있으면 false
	 */
	public boolean isBottom(){
		return children.isEmpty();
	}

	/**
	 * 최상위 카테고리 찾기
	 * @return 현재 카테고리 중 최상위 카테고리 반환
	 */
	public Category findTopLevelCategory() {
		Category current = this;
		while (current.getParent() != null) {
			current = current.getParent();
		}
		return current;
	}
}
