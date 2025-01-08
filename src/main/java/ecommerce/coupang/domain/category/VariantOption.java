package ecommerce.coupang.domain.category;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VariantOption implements Option{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "variant_option_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "option_name", nullable = false)
	private String optionName;

	@Column(name = "description", nullable = false)
	private String description;

	@OneToMany(mappedBy = "variantOption", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<VariantOptionValue> variantOptionValues = new ArrayList<>();
}
