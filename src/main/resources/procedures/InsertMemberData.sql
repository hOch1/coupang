DELIMITER $$

create procedure InsertMemberData()
begin
    declare i INT default 1;
    declare max INT default 10000;
    declare last_member_id INT;

    WHILE i <= max DO
            insert into member (name, email, phone_number, password, role, is_active, created_at, modified_at, grade)
            values (
                       concat('member', i),
                       concat('member', i, '@example.com'),
                       CONCAT('010-', LPAD(i, 8, '0')), -- 전화번호 형식 유지
                       '$2a$10$TA/jDO86POe/xUZ7I6OLyOhh4uHHa73dvLsmvcgb2IMNauhy4amdi',
                       'SELLER',
                       true,
                       now(),
                       now(),
                       'VIP'
                   );

            set last_member_id = LAST_INSERT_ID();

            insert into cart(member_id) values (last_member_id);
            set i = i + 1;
        end while;
end$$

DELIMITER ;
call InsertMemberData();

