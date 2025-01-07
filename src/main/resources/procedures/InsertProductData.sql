desc coupang.product;
desc coupang.product_variant;

DELIMITER $$

create procedure InsertProductData()
begin
    declare i INT default 1;
    declare j INT default 1;
    declare last_product_id INT;
    declare store_id INT default 1;
    declare random_category_Id INT;
    declare random_price INT;
    declare random_stock_quantity INT;
    declare max INT;

    select max(store.store_id) into max from store;

    # 상점당 상품 한개씩
    while i <= max DO
            # 의류 최하위만 추가
            set random_category_Id = elt(floor(1 + (rand() * 6)), 3, 4, 5, 7, 8, 9);
            insert into product (store_id, category_id, name, description, is_active, review_count, star_avg, created_at, modified_at)
            values (
                       store_id,
                       random_category_Id,
                       concat('product ', i),
                       'description',
                       true,
                       0,
                       0.0,
                       now(),
                       now()
                   );

            set last_product_id = last_insert_id();
            set j = 1;

            # 상품당 상품변형 5개씩
            while j <= 5 DO
                    set random_price = floor(5000 + (rand() * (100000 - 5000)));
                    set random_stock_quantity = floor(1 + (rand() * 100));
                    insert into product_variant (product_id, price, stock_quantity, sales_count, status, is_active, is_default)
                    values (
                               last_product_id,
                               random_price,
                               random_stock_quantity,
                               0,
                               'ACTIVE',
                               true,
                               if(j = 1, true, false)
                           );
                    set j = j + 1;
                end while;

            set store_id = store_id + 1;
            set i = i + 1;
        end while;
end $$

DELIMITER ;

call InsertProductData();