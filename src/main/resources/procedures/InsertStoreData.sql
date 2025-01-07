DELIMITER $$

create procedure InsertStoreData()
begin
    declare i INT default 1;
    declare member_id INT default 1;
    declare max INT;

    select max(member.member_id) into max from member;

    # 회원당 상점 하나씩
    while i <= max DO
            insert into store (member_id, name, description, store_number, is_active, created_at, modified_at)
            values(
                      member_id,
                      concat('store ', i),
                      'description',
                      concat('000', i),
                      true,
                      now(),
                      now()
                  );

            set i = i + 1;
            set member_id = member_id + 1;
        end while;
end $$

DELIMITER ;

call InsertStoreData();