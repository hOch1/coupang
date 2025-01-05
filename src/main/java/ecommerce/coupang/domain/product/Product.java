package ecommerce.coupang.domain.product;

import java.util.ArrayList;
import java.util.List;

import ecommerce.coupang.domain.BaseTimeEntity;
import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.product.review.ProductReview;
import ecommerce.coupang.domain.store.CouponProduct;
import ecommerce.coupang.domain.store.Store;
import ecommerce.coupang.domain.product.variant.ProductVariant;
import ecommerce.coupang.dto.request.product.CreateProductRequest;
import ecommerce.coupang.dto.request.product.UpdateProductRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Product extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "star_avg", nullable = false)
	private double starAvg = 0.0;

	@Column(name = "review_count", nullable = false)
	private int reviewCount = 0;

	@Column(name = "is_active", nullable = false)
	private boolean isActive = true;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductCategoryOption> productOptions = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductVariant> productVariants = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductReview> productReviews = new ArrayList<>();

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CouponProduct> couponProducts = new ArrayList<>();

	public Product(String name, String description, Store store, Category category) {
		this.name = name;
		this.description = description;
		this.store = store;
		this.category = category;
	}

	public static Product create(CreateProductRequest request, Store store, Category category) {
		return new Product(
			request.getName(),
			request.getDescription(),
			store,
			category
		);
	}

	public void update(UpdateProductRequest request) {
		this.name = request.getName() != null ? request.getName() : this.name;
		this.description = request.getDescription() != null ? request.getDescription() : this.description;
	}

	public void addProductOptions(ProductCategoryOption productCategoryOption) {
		this.productOptions.add(productCategoryOption);
	}

	public void addProductVariants(ProductVariant productVariant) {
		this.productVariants.add(productVariant);
	}

	/* 리뷰 추가 */
	public void addProductReviews(ProductReview productReview) {
		this.productReviews.add(productReview);

		increaseReviewCount();
		updateStarAvg();
	}

	/* 쿠폰 추가 */
	public void addCouponProducts(CouponProduct couponProduct) {
		this.couponProducts.add(couponProduct);
	}

	/* 리뷰 수 증가 */
	public void increaseReviewCount() {
		this.reviewCount++;
	}

	/* 리뷰 수 감소 */
	public void decreaseReviewCount() {
		this.reviewCount--;
	}

	public void updateStarAvg() {
		if (this.productReviews.isEmpty())
			this.starAvg = 0.0;
		else {
			this.starAvg = this.productReviews.stream()
				.mapToInt(ProductReview::getStar)
				.average()
				.orElse(0.0);
		}
	}

	public void delete() {
		productVariants.forEach(ProductVariant::delete);
		this.isActive = false;
	}
}
