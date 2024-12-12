-- 최상위 CLOTHES 카테고리
INSERT INTO category (type, level) VALUES ('CLOTHES', 1);
SET @CLOTHES_ID = LAST_INSERT_ID();

-- CLOTHES 하위 MEN
INSERT INTO category (type, level, parent_id) VALUES ('MEN', 2, @CLOTHES_ID);
SET @MEN_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('MEN_TOP', 3, @MEN_ID);
SET @MEN_TOP_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('MEN_BOTTOM', 3, @MEN_ID);
INSERT INTO category (type, level, parent_id) VALUES ('MEN_SHOES', 3, @MEN_ID);

-- CLOTHES 하위 WOMEN
INSERT INTO category (type, level, parent_id) VALUES ('WOMEN', 2, @CLOTHES_ID);
SET @WOMEN_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('WOMEN_TOP', 3, @WOMEN_ID);
INSERT INTO category (type, level, parent_id) VALUES ('WOMEN_BOTTOM', 3, @WOMEN_ID);
INSERT INTO category (type, level, parent_id) VALUES ('WOMEN_SHOES', 3, @WOMEN_ID);

-- CLOTHES 하위 KID
INSERT INTO category (type, level, parent_id) VALUES ('KID', 2, @CLOTHES_ID);
SET @KID_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('KID_MEN', 3, @KID_ID);
INSERT INTO category (type, level, parent_id) VALUES ('KID_WOMEN', 3, @KID_ID);

-- 최상위 FOOD 카테고리
INSERT INTO category (type, level) VALUES ('FOOD', 1);
SET @FOOD_ID = LAST_INSERT_ID();

-- FOOD 하위 MEAT
INSERT INTO category (type, level, parent_id) VALUES ('MEAT', 2, @FOOD_ID);
SET @MEAT_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('MEAT_PIG', 3, @MEAT_ID);
INSERT INTO category (type, level, parent_id) VALUES ('MEAT_COW', 3, @MEAT_ID);
INSERT INTO category (type, level, parent_id) VALUES ('MEAT_ETC', 3, @MEAT_ID);

-- FOOD 하위 SEAFOOD
INSERT INTO category (type, level, parent_id) VALUES ('SEAFOOD', 2, @FOOD_ID);
SET @SEAFOOD_ID = LAST_INSERT_ID();
INSERT INTO category (type, level, parent_id) VALUES ('FISH', 3, @SEAFOOD_ID);
INSERT INTO category (type, level, parent_id) VALUES ('DRY_FISH', 3, @SEAFOOD_ID);
INSERT INTO category (type, level, parent_id) VALUES ('SHELL_FISH', 3, @SEAFOOD_ID);

INSERT INTO category (type, level, parent_id) VALUES ('VEGETABLE', 2, @FOOD_ID);
INSERT INTO category (type, level, parent_id) VALUES ('NUTS', 2, @FOOD_ID);

-- 최상위 HOME 카테고리
INSERT INTO category (type, level) VALUES ('HOME', 1);
SET @HOME_ID = LAST_INSERT_ID();

INSERT INTO category (type, level, parent_id) VALUES ('BED', 2, @HOME_ID);

-- ----- 옵션
-- 의류 옵션
INSERT INTO category_option (category_id, option_name, description) VALUES (@CLOTHES_ID, 'SIZE', '의류 사이즈');
SET @CLOTHES_SIZE_ID = LAST_INSERT_ID();
INSERT INTO category_option (category_id, option_name, description) VALUES (@CLOTHES_ID, 'FIT', '의류 핏');
SET @CLOTHES_FIT_ID = LAST_INSERT_ID();
INSERT INTO category_option (category_id, option_name, description) VALUES (@CLOTHES_ID, 'COLOR', '의류 색상');
SET @CLOTHES_COLOR_ID = LAST_INSERT_ID();

-- 의류 MEN_TOP 옵션
INSERT INTO category_option (category_id, option_name, description) VALUES (@MEN_TOP_ID, 'AGE', '나이');
SET @MEN_TOP_AGE_ID = LAST_INSERT_ID();

-- 음식 옵셥
INSERT INTO category_option (category_id, option_name, description) VALUES (@FOOD_ID, 'DOMESTIC_YN', '국내산 여부');
SET @FOOD_DOMESTIC_YN = LAST_INSERT_ID();

-- 홈 옵션
INSERT INTO category_option (category_id, option_name, description) VALUES (@HOME_ID, 'MATERIAL', '원자재');
SET @HOME_MATERIAL_ID = LAST_INSERT_ID();
INSERT INTO category_option (category_id, option_name, description) VALUES (@HOME_ID, 'COLOR', '색상');
SET @HOME_COLOR_ID = LAST_INSERT_ID();

-- ----- 옵션 타입
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'WHITE', '화이트');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'BLACK', '블랙');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'RED', '빨강');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'GREEN', '녹색');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_COLOR_ID, 'BLUE', '파랑');

INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'SLIM', '슬림핏');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'OVER', '오버핏');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_FIT_ID, 'REGULAR', '레귤러 핏');

INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'S', '스몰');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'M', '미디엄');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'L', '라지');
INSERT INTO option_value (category_option_id, value, description) VALUES (@CLOTHES_SIZE_ID, 'XL', '엑스라지');

INSERT INTO option_value (category_option_id, value, description) VALUES (@MEN_TOP_AGE_ID, '10', '10대');
INSERT INTO option_value (category_option_id, value, description) VALUES (@MEN_TOP_AGE_ID, '20', '20대');
INSERT INTO option_value (category_option_id, value, description) VALUES (@MEN_TOP_AGE_ID, '30', '30대');

INSERT INTO option_value (category_option_id, value, description) VALUES (@FOOD_DOMESTIC_yn, 'Y', '국내산');
INSERT INTO option_value (category_option_id, value, description) VALUES (@FOOD_DOMESTIC_yn, 'N', '수입산');

INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_COLOR_ID, 'WHITE', '화이트');
INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_COLOR_ID, 'BLACK', '블랙');
INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_COLOR_ID, 'ETC', '기타');

INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'WOOD', '원목');
INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'METAL', '철제');
INSERT INTO option_value (category_option_id, value, description) VALUES (@HOME_MATERIAL_ID, 'PLASTIC', '플라스틱');

