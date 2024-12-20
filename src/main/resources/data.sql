-- 최상위 CLOTHES 카테고리
INSERT INTO category (type, name, level) VALUES ('CLOTHES', "의류", 1);
SET @CLOTHES_ID = LAST_INSERT_ID();

-- CLOTHES 하위 MEN
INSERT INTO category (type, name, level, parent_id) VALUES ('MEN', "남성", 2, @CLOTHES_ID);
SET @MEN_ID = LAST_INSERT_ID();
INSERT INTO category (type, name, level, parent_id) VALUES ('MEN_TOP', "남성 상의", 3, @MEN_ID);
SET @MEN_TOP_ID = LAST_INSERT_ID();
INSERT INTO category (type, name, level, parent_id) VALUES ('MEN_BOTTOM', "남성 하의", 3, @MEN_ID);
INSERT INTO category (type, name, level, parent_id) VALUES ('MEN_SHOES', "남성 신발", 3, @MEN_ID);

-- CLOTHES 하위 WOMEN
INSERT INTO category (type, name, level, parent_id) VALUES ('WOMEN', "여성", 2, @CLOTHES_ID);
SET @WOMEN_ID = LAST_INSERT_ID();
INSERT INTO category (type, name, level, parent_id) VALUES ('WOMEN_TOP', "여성 상의", 3, @WOMEN_ID);
INSERT INTO category (type, name, level, parent_id) VALUES ('WOMEN_BOTTOM', "여성 하의", 3, @WOMEN_ID);
INSERT INTO category (type, name, level, parent_id) VALUES ('WOMEN_SHOES', "여성 신발", 3, @WOMEN_ID);

-- CLOTHES 하위 KID
INSERT INTO category (type, name, level, parent_id) VALUES ('KID', "아동", 2, @CLOTHES_ID);
SET @KID_ID = LAST_INSERT_ID();
INSERT INTO category (type, name, level, parent_id) VALUES ('KID_MEN', "남아", 3, @KID_ID);
INSERT INTO category (type, name, level, parent_id) VALUES ('KID_WOMEN', "여아", 3, @KID_ID);


# -- 최상위 FOOD 카테고리
# INSERT INTO category (type, name, level) VALUES ('FOOD', "음식", 1);
# SET @FOOD_ID = LAST_INSERT_ID();
#
# -- FOOD 하위 MEAT
# INSERT INTO category (type, name, level, parent_id) VALUES ('MEAT', "육류", 2, @FOOD_ID);
# SET @MEAT_ID = LAST_INSERT_ID();
# INSERT INTO category (type, name, level, parent_id) VALUES ('MEAT_PIG', "돼지고기", 3, @MEAT_ID);
# INSERT INTO category (type, name, level, parent_id) VALUES ('MEAT_COW', "소고기", 3, @MEAT_ID);
# INSERT INTO category (type, name, level, parent_id) VALUES ('MEAT_ETC', "기타", 3, @MEAT_ID);
#
# -- FOOD 하위 SEAFOOD
# INSERT INTO category (type, name, level, parent_id) VALUES ('SEAFOOD', "수산물", 2, @FOOD_ID);
# SET @SEAFOOD_ID = LAST_INSERT_ID();
# INSERT INTO category (type, name, level, parent_id) VALUES ('FISH', "어류", 3, @SEAFOOD_ID);
# INSERT INTO category (type, name, level, parent_id) VALUES ('DRY_FISH', "건어물", 3, @SEAFOOD_ID);
# INSERT INTO category (type, name, level, parent_id) VALUES ('SHELL_FISH', "갑각류", 3, @SEAFOOD_ID);
#
# INSERT INTO category (type, name, level, parent_id) VALUES ('VEGETABLE', "채소", 2, @FOOD_ID);
# INSERT INTO category (type, name, level, parent_id) VALUES ('NUTS', "견과류", 2, @FOOD_ID);
#
# -- 최상위 HOME 카테고리
# INSERT INTO category (type, name, level) VALUES ('HOME', "홈", 1);
# SET @HOME_ID = LAST_INSERT_ID();
#
# INSERT INTO category (type, name, level, parent_id) VALUES ('BED', "침구류", 2, @HOME_ID);

-- ----- 공통 옵션
-- 의류 옵션
INSERT INTO category_base_option (category_id, option_name, description) VALUES (@CLOTHES_ID, 'FIT', '의류 핏');
SET @CLOTHES_FIT_ID = LAST_INSERT_ID();
INSERT INTO category_base_option (category_id, option_name, description) VALUES (@CLOTHES_ID, 'MATERIAL', '의류 재질');
SET @CLOTHES_MATERIAL_ID = LAST_INSERT_ID();

# -- 음식 옵셥
# INSERT INTO category_base_option (category_id, option_name, description) VALUES (@FOOD_ID, 'DOMESTIC_YN', '국내산 여부');
# SET @FOOD_DOMESTIC_YN = LAST_INSERT_ID();
#
# -- 홈 옵션
# INSERT INTO category_base_option (category_id, option_name, description) VALUES (@HOME_ID, 'MATERIAL', '원자재');
# SET @HOME_MATERIAL_ID = LAST_INSERT_ID();


-- ---- 서브 옵션
-- 의류
INSERT INTO category_sub_option (category_id, option_name, description, parent_id, level) VALUES (@CLOTHES_ID, 'CLOTHES_COLOR', '의류 색상', null, 1);
SET @CLOTHES_COLOR_ID = LAST_INSERT_ID();
INSERT INTO category_sub_option (category_id, option_name, description, parent_id, level) VALUES (@CLOTHES_ID, 'CLOTHES_SIZE', '의류 사이즈', @CLOTHES_COLOR_ID, 2);
SET @CLOTHES_SIZE_ID = LAST_INSERT_ID();

# INSERT INTO category_sub_option (category_id, option_name, description) VALUES (@MEN_ID, 'AGE', '남성 나이대');
# SET @MEN_AGE_ID = LAST_INSERT_ID();
# INSERT INTO category_sub_option (category_id, option_name, description) VALUES (@WOMEN_ID, 'AGE', '여성 나이대');
# SET @WOMEN_AGE_ID = LAST_INSERT_ID();

-- 홈 옵션
# INSERT INTO category_sub_option (category_id, option_name, description) VALUES (@HOME_ID, 'COLOR', '색상');
# SET @HOME_COLOR_ID = LAST_INSERT_ID();

-- ----- 공통 옵션 값
-- 의류
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_MATERIAL_ID, 'COTTON', '면');
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_MATERIAL_ID, 'POLYESTER', '폴리에스테르');
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_MATERIAL_ID, 'WOOL', '울');
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_MATERIAL_ID, 'MIX', '혼합');

INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'SLIM', '슬림핏');
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'OVER', '오버핏');
INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'REGULAR', '레귤러 핏');

# -- 음식
# INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@FOOD_DOMESTIC_yn, 'Y', '국내산');
# INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@FOOD_DOMESTIC_yn, 'N', '수입산');

# -- 홈
# INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'WOOD', '원목');
# INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'METAL', '철제');
# INSERT INTO base_option_value (category_base_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'PLASTIC', '플라스틱');

-- ----- 서브 옵션 값
-- 의류
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'WHITE', '화이트');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'BLACK', '블랙');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'RED', '빨강');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'GREEN', '녹색');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'BLUE', '파랑');

INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'S', '스몰');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'M', '미디엄');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'L', '라지');
INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'XL', '엑스라지');

# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@MEN_AGE_ID, '10', '10대');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@MEN_AGE_ID, '20', '20대');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@MEN_AGE_ID, '30', '30대');

# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@WOMEN_AGE_ID, '10', '10대');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@WOMEN_AGE_ID, '20', '20대');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@WOMEN_AGE_ID, '30', '30대');
#
# -- 홈
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@HOME_COLOR_ID, 'WHITE', '화이트');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@HOME_COLOR_ID, 'BLACK', '블랙');
# INSERT INTO sub_option_value (category_sub_option_id, value, description) VALUES (@HOME_COLOR_ID, 'ETC', '기타');