
DELIMITER $$
create procedure InsertProductVariantOption()
begin

    declare product_variant_id INT default 1;
    declare max_product_variant_id INT;
    declare ran_variant_value_id INT;

    select max(product_variant.product_variant_id) into max_product_variant_id from product_variant;

    while product_variant_id <= max_product_variant_id DO

            SET ran_variant_value_id = ELT(FLOOR(1 + (RAND() * 5)), 1, 2, 3, 4, 5);
            insert into product_variant_option (product_variant_id, variant_option_value_id)
            values (
                       product_variant_id,
                       ran_variant_value_id
                   );

            SET ran_variant_value_id = ELT(FLOOR(1 + (RAND() * 4)), 6, 7, 8, 9);
            insert into product_variant_option (product_variant_id, variant_option_value_id)
            values (
                       product_variant_id,
                       ran_variant_value_id
                   );

            set product_variant_id = product_variant_id + 1;
        end while;
end $$
DELIMITER ;

call InsertProductVariantOption();