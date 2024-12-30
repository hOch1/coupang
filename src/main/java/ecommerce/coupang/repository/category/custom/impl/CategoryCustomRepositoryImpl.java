package ecommerce.coupang.repository.category.custom.impl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import ecommerce.coupang.repository.category.custom.CategoryCustomRepository;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryCustomRepositoryImpl implements CategoryCustomRepository {

	private final JPAQueryFactory queryFactory;

}