package ecommerce.coupang.repository.category.custom.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import ecommerce.coupang.domain.category.Category;
import ecommerce.coupang.domain.category.QCategory;
import ecommerce.coupang.repository.category.custom.CategoryCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Category> findCategoryWithRoot(Long categoryId) {
		QCategory category = QCategory.category;
		QCategory parentCategory = new QCategory("parentCategory");

		// 기본 쿼리: 최하위 카테고리 조회 시작
		JPAQuery<Category> query = queryFactory.selectFrom(category)
			.leftJoin(category.parent, parentCategory).fetchJoin()
			.where(category.id.eq(categoryId));

		// 부모 카테고리를 동적으로 fetch join
		QCategory current = parentCategory;
		while (true) {
			QCategory nextParent = new QCategory("parent_" + current.getMetadata().getName());

			/*
			부모 카테고리 fetch join
			페이징이 불필요하기때문에 optionValue 도 fetch join
			 */
			query = query.leftJoin(current.parent, nextParent).fetchJoin();

			// 최상위 부모를 확인
			BooleanExpression isRoot = current.parent.isNull();
			if (queryFactory.selectOne().from(current).where(isRoot).fetchOne() != null) {
				break;
			}

			current = nextParent;
		}

		return Optional.ofNullable(query.fetchOne());
	}
}
