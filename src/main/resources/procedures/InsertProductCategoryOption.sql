

DELIMITER $$
create procedure InsertProductCategoryOption()
begin
    declare product_id INT default 1;
    declare max_product_id INT;
    declare ran_category_option_value_id INT;

    select max(product.product_id) into max_product_id from product;

    while product_id <= max_product_id DO
            SET ran_category_option_value_id = ELT(FLOOR(1 + (RAND() * 4)), 1, 2, 3, 4);
            insert into product_category_option (product_id, category_option_value_id)
            values (
                       product_id,
                       ran_category_option_value_id
                   );

            SET ran_category_option_value_id = ELT(FLOOR(1 + (RAND() * 3)), 5, 6, 7);
            insert into product_category_option (product_id, category_option_value_id)
            values (
                       product_id,
                       ran_category_option_value_id
                   );

            set product_id = product_id + 1;
        end while;
end $$

DELIMITER ;

call InsertProductCategoryOption();