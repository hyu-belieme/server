-- insert university
insert into university(id, code, name, api_url)
values (1, 'HYU', '한양대학교', 'https://api.hanyang.ac.kr/oauth/authorize/');
insert into university(id, code, name, api_url)
values (2, 'CKU', '가톨릭관동대학교', null);
insert into university(id, code, name, api_url)
values (3, 'SNU', '서울대학교', null);

-- insert major
insert into major(id, university_id, code)
values (1, 1, 'FH04067');
insert into major(id, university_id, code)
values (2, 1, 'FH04068');
insert into major(id, university_id, code)
values (3, 1, 'FH04069');
insert into major(id, university_id, code)
values (4, 2, 'TEST');
insert into major(id, university_id, code)
values (5, 1, 'TEST');
insert into major(id, university_id, code)
values (6, 2, 'A68');
insert into major(id, university_id, code)
values (7, 2, 'A69');
insert into major(id, university_id, code)
values (8, 2, 'A70');

-- insert department
insert into department(id, university_id, code, name)
values (1, 1, 'CSE', '컴퓨터소프트웨어학부');
insert into department(id, university_id, code, name)
values (2, 1, 'STU', '총학생회');
insert into department(id, university_id, code, name)
values (3, 2, 'MED', '의과대학');
insert into department(id, university_id, code, name)
values (4, 2, 'STU', '총학생회');
insert into department(id, university_id, code, name)
values (5, 2, 'ME', '기계공학과');

insert into major_department_join(department_id, major_id)
values (1, 1);
insert into major_department_join(department_id, major_id)
values (1, 2);

insert into major_department_join(department_id, major_id)
values (2, 1);
insert into major_department_join(department_id, major_id)
values (2, 2);
insert into major_department_join(department_id, major_id)
values (2, 3);
insert into major_department_join(department_id, major_id)
values (2, 5);

insert into major_department_join(department_id, major_id)
values (3, 6);

insert into major_department_join(department_id, major_id)
values (4, 4);
insert into major_department_join(department_id, major_id)
values (4, 6);
insert into major_department_join(department_id, major_id)
values (4, 7);
insert into major_department_join(department_id, major_id)
values (4, 8);

insert into major_department_join(department_id, major_id)
values (5, 4);
insert into major_department_join(department_id, major_id)
values (5, 7);

-- insert authority
insert into authority(id, department_id, permission)
values (1, 1, 'DEFAULT');
insert into authority(id, department_id, permission)
values (2, 1, 'BANNED');
insert into authority(id, department_id, permission)
values (3, 1, 'USER');
insert into authority(id, department_id, permission)
values (4, 1, 'STAFF');
insert into authority(id, department_id, permission)
values (5, 1, 'MASTER');
insert into authority(id, department_id, permission)
values (6, 1, 'DEVELOPER');
insert into authority(id, department_id, permission)
values (7, 2, 'DEFAULT');
insert into authority(id, department_id, permission)
values (8, 2, 'BANNED');
insert into authority(id, department_id, permission)
values (9, 2, 'USER');
insert into authority(id, department_id, permission)
values (10, 2, 'STAFF');
insert into authority(id, department_id, permission)
values (11, 2, 'MASTER');
insert into authority(id, department_id, permission)
values (12, 2, 'DEVELOPER');
insert into authority(id, department_id, permission)
values (13, 3, 'DEFAULT');
insert into authority(id, department_id, permission)
values (14, 3, 'BANNED');
insert into authority(id, department_id, permission)
values (15, 3, 'USER');
insert into authority(id, department_id, permission)
values (16, 3, 'STAFF');
insert into authority(id, department_id, permission)
values (17, 3, 'MASTER');
insert into authority(id, department_id, permission)
values (18, 3, 'DEVELOPER');
insert into authority(id, department_id, permission)
values (19, 4, 'DEFAULT');
insert into authority(id, department_id, permission)
values (20, 4, 'BANNED');
insert into authority(id, department_id, permission)
values (21, 4, 'USER');
insert into authority(id, department_id, permission)
values (22, 4, 'STAFF');
insert into authority(id, department_id, permission)
values (23, 4, 'MASTER');
insert into authority(id, department_id, permission)
values (24, 4, 'DEVELOPER');

-- insert user
insert into user(id, university_id, student_id, name, token, create_time_stamp, approval_time_stamp)
values (1, 1, '2018008886', '이석환', 'TEST1', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, token, create_time_stamp, approval_time_stamp)
values (2, 1, '2018008887', '이석현', 'TEST2', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, token, create_time_stamp, approval_time_stamp)
values (3, 1, '2019008887', '강백호', 'TEST3', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, token, create_time_stamp, approval_time_stamp)
values (4, 2, '2018008886', '이석환', 'TEST4', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, token, create_time_stamp, approval_time_stamp)
values (5, 2, '2018008887', '이석환', 'TEST5', 1673155358, 1673155358);

insert into authority_user_join(user_id, authority_id)
values (1, 1);
insert into authority_user_join(user_id, authority_id)
values (1, 7);
insert into authority_user_join(user_id, authority_id)
values (1, 5);
insert into authority_user_join(user_id, authority_id)
values (1, 11);
insert into authority_user_join(user_id, authority_id)
values (1, 15);

insert into authority_user_join(user_id, authority_id)
values (2, 1);
insert into authority_user_join(user_id, authority_id)
values (2, 7);
insert into authority_user_join(user_id, authority_id)
values (2, 2);

insert into authority_user_join(user_id, authority_id)
values (3, 7);

insert into authority_user_join(user_id, authority_id)
values (4, 13);
insert into authority_user_join(user_id, authority_id)
values (4, 19);
insert into authority_user_join(user_id, authority_id)
values (4, 17);

insert into authority_user_join(user_id, authority_id)
values (5, 19);

-- insert stuff
insert into stuff(id, department_id, name, emoji, next_item_num)
values (1, 1, '우산', '☂', 5);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (2, 1, '블루투스스피커', '📻', 3);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (3, 1, '축구공', '⚽️', 2);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (4, 1, '농구공', '🏀', 2);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (5, 2, '축구공', '⚽️', 4);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (6, 2, '농구공', '🏀', 4);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (7, 3, '볼펜', '🖋️', 9);
insert into stuff(id, department_id, name, emoji, next_item_num)
values (8, 3, '스케이트보드', '🛹', 2);


-- insert item
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (1, 1, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (2, 1, 2, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (3, 1, 3, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (4, 1, 4, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (5, 2, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (6, 2, 2, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (7, 3, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (8, 4, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (9, 5, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (10, 5, 2, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (11, 5, 3, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (12, 6, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (13, 6, 2, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (14, 6, 3, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (15, 7, 1, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (16, 7, 2, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (17, 7, 3, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (18, 7, 4, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (19, 7, 5, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (20, 7, 6, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (21, 7, 7, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (22, 7, 8, null, 1);
insert into item(id, stuff_id, num, last_history_id, next_history_num)
values (23, 8, 1, null, 1);

-- insert history
insert into history(id, item_id, num, requester_id, reserved_time_stamp, approve_manager_id, approve_time_stamp,
                    return_manager_id, return_time_stamp, lost_manager_id, lost_time_stamp, cancel_manager_id,
                    cancel_time_stamp)
values (1, 1, 1, 1, 1673155356, 1, 1673155430, 1, 1673159244, null, 0, null, 0);
update item
set last_history_id=1,
    next_history_num=2
where id = 1;

insert into history(id, item_id, num, requester_id, reserved_time_stamp, approve_manager_id, approve_time_stamp,
                    return_manager_id, return_time_stamp, lost_manager_id, lost_time_stamp, cancel_manager_id,
                    cancel_time_stamp)
values (2, 6, 1, 1, 1673155356, null, 0, null, 0, null, 0, null, 0);
update item
set last_history_id=2,
    next_history_num=2
where id = 6;

insert into history(id, item_id, num, requester_id, reserved_time_stamp, approve_manager_id, approve_time_stamp,
                    return_manager_id, return_time_stamp, lost_manager_id, lost_time_stamp, cancel_manager_id,
                    cancel_time_stamp)
values (3, 1, 2, 2, 1673172221, 1, 1673172521, null, 0, null, 0, null, 0);
update item
set last_history_id=3,
    next_history_num=3
where id = 1;

insert into history(id, item_id, num, requester_id, reserved_time_stamp, approve_manager_id, approve_time_stamp,
                    return_manager_id, return_time_stamp, lost_manager_id, lost_time_stamp, cancel_manager_id,
                    cancel_time_stamp)
values (4, 17, 1, 1, 1673172221, null, 0, null, 0, null, 0, 1, 1673172521);
update item
set last_history_id=4,
    next_history_num=2
where id = 17;