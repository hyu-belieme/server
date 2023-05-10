-- insert new university
insert into university(id, name, api_url)
values ('34453032353133424643444434464445', 'DEV', 'DEV');
insert into university(id, name, api_url)
values ('37364342434533424143344334433445', '한양대학교', 'https://api.hanyang.ac.kr/rs/user/loginInfo.json');
insert into university(id, name, api_url)
values ('41463146463944354244463734353333', '가톨릭관동대학교', null);
insert into university(id, name, api_url)
values ('507e25c492464863bff8059589989563', '서울대학교', null);

-- insert major
insert into major(id, university_id, code) values ('edbbfebc63b446e1975367451d3614f8', '37364342434533424143344334433445', 'FH04067');
insert into major(id, university_id, code) values ('b9624786b93f422ebb1183c6c2f6cbd8', '37364342434533424143344334433445', 'FH04068');
insert into major(id, university_id, code) values ('ebfba44eb632467298e8e6aeb8fcca8c', '37364342434533424143344334433445', 'FH04069');
insert into major(id, university_id, code) values ('422c2f41f0fa42569c59178c68c6172c', '41463146463944354244463734353333', 'A68');
insert into major(id, university_id, code) values ('272dc52d346b417991c6e894b2ff2b63', '41463146463944354244463734353333', 'A69');
insert into major(id, university_id, code) values ('40044cd4f5e54a78855c4f4f52c92799', '41463146463944354244463734353333', 'A70');

-- insert new department
insert into department(id, university_id, name)
values ('37434636433933453946313634443633', '34453032353133424643444434464445', 'DEV');
insert into department(id, university_id, name)
values ('c7312fe706264068911251a1b8e04309', '37364342434533424143344334433445', '컴퓨터소프트웨어학부');
insert into department(id, university_id, name)
values ('b069fa11c416427696e1aaad27f73aeb', '37364342434533424143344334433445', '기계공학부');
insert into department(id, university_id, name)
values ('9ebea9d296694560990c3628ddc29d23', '37364342434533424143344334433445', '공과대학');
insert into department(id, university_id, name)
values ('fd16b1f820ff47ef9a7d46e5d1e1c080', '41463146463944354244463734353333', '의과대학');
insert into department(id, university_id, name)
values ('65fd918548b6407b8bca8177b4d64713', '41463146463944354244463734353333', '총학생회');

-- insert major department join
insert major_department_join(id, major_id, department_id) values (1, 'edbbfebc63b446e1975367451d3614f8', 'c7312fe706264068911251a1b8e04309');
insert major_department_join(id, major_id, department_id) values (2, 'b9624786b93f422ebb1183c6c2f6cbd8', 'c7312fe706264068911251a1b8e04309');
insert major_department_join(id, major_id, department_id) values (3, 'ebfba44eb632467298e8e6aeb8fcca8c', 'b069fa11c416427696e1aaad27f73aeb');
insert major_department_join(id, major_id, department_id) values (4, 'edbbfebc63b446e1975367451d3614f8', '9ebea9d296694560990c3628ddc29d23');
insert major_department_join(id, major_id, department_id) values (5, 'b9624786b93f422ebb1183c6c2f6cbd8', '9ebea9d296694560990c3628ddc29d23');
insert major_department_join(id, major_id, department_id) values (6, 'ebfba44eb632467298e8e6aeb8fcca8c', '9ebea9d296694560990c3628ddc29d23');
insert major_department_join(id, major_id, department_id) values (7, '272dc52d346b417991c6e894b2ff2b63', 'fd16b1f820ff47ef9a7d46e5d1e1c080');
insert major_department_join(id, major_id, department_id) values (8, '422c2f41f0fa42569c59178c68c6172c', '65fd918548b6407b8bca8177b4d64713');
insert major_department_join(id, major_id, department_id) values (9, '272dc52d346b417991c6e894b2ff2b63', '65fd918548b6407b8bca8177b4d64713');
insert major_department_join(id, major_id, department_id) values (10, '40044cd4f5e54a78855c4f4f52c92799', '65fd918548b6407b8bca8177b4d64713');

-- insert new authority
insert into authority(id, department_id, permission)
values (1, '37434636433933453946313634443633', 'DEFAULT');
insert into authority(id, department_id, permission)
values (2, '37434636433933453946313634443633', 'BANNED');
insert into authority(id, department_id, permission)
values (3, '37434636433933453946313634443633', 'USER');
insert into authority(id, department_id, permission)
values (4, '37434636433933453946313634443633', 'STAFF');
insert into authority(id, department_id, permission)
values (5, '37434636433933453946313634443633', 'MASTER');
insert into authority(id, department_id, permission)
values (6, '37434636433933453946313634443633', 'DEVELOPER');
insert into authority(id, department_id, permission)
values (7, 'c7312fe706264068911251a1b8e04309', 'DEFAULT');
insert into authority(id, department_id, permission)
values (8, 'c7312fe706264068911251a1b8e04309', 'BANNED');
insert into authority(id, department_id, permission)
values (9, 'c7312fe706264068911251a1b8e04309', 'USER');
insert into authority(id, department_id, permission)
values (10, 'c7312fe706264068911251a1b8e04309', 'STAFF');
insert into authority(id, department_id, permission)
values (11, 'c7312fe706264068911251a1b8e04309', 'MASTER');
insert into authority(id, department_id, permission)
values (12, 'c7312fe706264068911251a1b8e04309', 'DEVELOPER');
insert into authority(id, department_id, permission)
values (13, 'b069fa11c416427696e1aaad27f73aeb', 'DEFAULT');
insert into authority(id, department_id, permission)
values (14, 'b069fa11c416427696e1aaad27f73aeb', 'BANNED');
insert into authority(id, department_id, permission)
values (15, 'b069fa11c416427696e1aaad27f73aeb', 'USER');
insert into authority(id, department_id, permission)
values (16, 'b069fa11c416427696e1aaad27f73aeb', 'STAFF');
insert into authority(id, department_id, permission)
values (17, 'b069fa11c416427696e1aaad27f73aeb', 'MASTER');
insert into authority(id, department_id, permission)
values (18, 'b069fa11c416427696e1aaad27f73aeb', 'DEVELOPER');
insert into authority(id, department_id, permission)
values (19, '9ebea9d296694560990c3628ddc29d23', 'DEFAULT');
insert into authority(id, department_id, permission)
values (20, '9ebea9d296694560990c3628ddc29d23', 'BANNED');
insert into authority(id, department_id, permission)
values (21, '9ebea9d296694560990c3628ddc29d23', 'USER');
insert into authority(id, department_id, permission)
values (22, '9ebea9d296694560990c3628ddc29d23', 'STAFF');
insert into authority(id, department_id, permission)
values (23, '9ebea9d296694560990c3628ddc29d23', 'MASTER');
insert into authority(id, department_id, permission)
values (24, '9ebea9d296694560990c3628ddc29d23', 'DEVELOPER');
insert into authority(id, department_id, permission)
values (25, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'DEFAULT');
insert into authority(id, department_id, permission)
values (26, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'BANNED');
insert into authority(id, department_id, permission)
values (27, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'USER');
insert into authority(id, department_id, permission)
values (28, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'STAFF');
insert into authority(id, department_id, permission)
values (29, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'MASTER');
insert into authority(id, department_id, permission)
values (30, 'fd16b1f820ff47ef9a7d46e5d1e1c080', 'DEVELOPER');
insert into authority(id, department_id, permission)
values (31, '65fd918548b6407b8bca8177b4d64713', 'DEFAULT');
insert into authority(id, department_id, permission)
values (32, '65fd918548b6407b8bca8177b4d64713', 'BANNED');
insert into authority(id, department_id, permission)
values (33, '65fd918548b6407b8bca8177b4d64713', 'USER');
insert into authority(id, department_id, permission)
values (34, '65fd918548b6407b8bca8177b4d64713', 'STAFF');
insert into authority(id, department_id, permission)
values (35, '65fd918548b6407b8bca8177b4d64713', 'MASTER');
insert into authority(id, department_id, permission)
values (36, '65fd918548b6407b8bca8177b4d64713', 'DEVELOPER');

-- insert new user
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('38433142313432313741384234424539', '34453032353133424643444434464445', 'DEV1', '개발자1', 0, '5fc9d9a4-4a41-416f-b0b4-24db6d9c87e1', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('31313446433639433136453534334641', '34453032353133424643444434464445', 'DEV2', '개발자2', 0, 'eba8ee88-e9d0-4ba7-a821-769597b276c3', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('31383941393032373036464534333241', '34453032353133424643444434464445', 'DEV3', '개발자3', 0, 'ef09ec44-baf7-47c1-aec8-2097dd12277f', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('8ff21103bad74666a512d73747b68cd7', '37364342434533424143344334433445', 'CSE-TEST1', '테스터1', 0, '6ca0bc09-349b-4e42-862a-ce9ff2e607f0', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('0df5d10895d0405aa280aad324f21311', '37364342434533424143344334433445', 'ME-TEST1', '테스터1', 0, 'f22121e3-7c23-4716-a123-10fc34d6cde5', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('7adccf31116449bd9031dd0ffb00b9ae', '37364342434533424143344334433445', '2018008886', '이석환', 2018, '8231999b-3bf4-4036-afb7-34cd98810b3c', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('87c01625b916466e8d813be92e2527bd', '37364342434533424143344334433445', '2018008887', '강백호', 2018, 'ef4233a0-2bfc-4633-9cc9-38d4adbfa10e', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('058e870393bb41e4a114e2cbf005bf54', '37364342434533424143344334433445', '2018008888', '서태웅', 2018, '48ee4dc7-3279-4fe9-9159-83d98899212f', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('92e35636d3564cbaac072ec665187b0c', '37364342434533424143344334433445', '2018008889', '다빈치', 2018, 'b25964ee-79f4-4b58-9115-325c2cf60205', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('2831d7478d8e44648d70f063bcfdb735', '37364342434533424143344334433445', '2018008890', '박용수', 2018, '879aad94-1611-41ec-a71d-f1796100fd38', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('52570b695c164bdbbc1e063247a93b4d', '37364342434533424143344334433445', '2018007129', '김경민', 2018, 'aea85218-c069-4879-b5c2-451c3ad774ba', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('561953f1bae34f3491a9d5fcd16f05ca', '37364342434533424143344334433445', '2018007130', '손성준', 2018, 'fbd703d0-1851-447f-bdb7-51edf8f3ecde', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('35a92ffd416c49d5ae64f5af337246a5', '37364342434533424143344334433445', '2018007131', '이수경', 2018, '8c4e9ebd-28ac-475f-9316-b37e97b6cf77', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('4070d6da6b5a4decb07618fa9bc52777', '37364342434533424143344334433445', '2018007132', '황희수', 2018, '02017126-2d23-44b0-a308-c295275711d7', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('a010437a9b4c462980365a5410ced8cc', '41463146463944354244463734353333', 'C202201234', '박창훈', 2022, '9c22d304-c667-4d80-a692-c719bd9e2ac5', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('cca1df09197f4baeacec7841a9da9397', '41463146463944354244463734353333', 'C202201235', '윤효성', 2022, '725a4da7-b354-40b2-8d78-cbb9109c7546', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('e74bd461eb4a4c9792abf797b4438a88', '41463146463944354244463734353333', 'C202201236', '서수빈', 2022, '2af4c44f-9a67-47cd-9464-c1fc112810cd', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('2c113864dfce4aab92aaef756c7caa55', '41463146463944354244463734353333', 'C202201237', '황형기', 2022, 'a2fd57e8-b44d-41f2-aaab-1c530d5cca7b', 1673155358, 1673155358);
insert into user(id, university_id, student_id, name, entrance_year, token, created_at, approved_at)
values ('119b4a30285f4f0aa037a9134b7e7bb7', '41463146463944354244463734353333', 'C202201238', '전승용', 2022, '715a52ac-f312-4091-800c-475d9b727dbb', 1673155358, 1673155358);

-- insert authority user join
insert into authority_user_join(id, authority_id, user_id)
values (1, 0 * 6 + 6, '38433142313432313741384234424539');

insert into authority_user_join(id, authority_id, user_id)
values (2, 0 * 6 + 6, '31313446433639433136453534334641');

insert into authority_user_join(id, authority_id, user_id)
values (3, 0 * 6 + 6, '31383941393032373036464534333241');

insert into authority_user_join(id, authority_id, user_id)
values (4, 1 * 6 + 4, '8ff21103bad74666a512d73747b68cd7');

insert into authority_user_join(id, authority_id, user_id)
values (5, 2 * 6 + 3, '0df5d10895d0405aa280aad324f21311');

insert into authority_user_join(id, authority_id, user_id)
values (6, 1 * 6 + 1, '7adccf31116449bd9031dd0ffb00b9ae');
insert into authority_user_join(id, authority_id, user_id)
values (7, 3 * 6 + 1, '7adccf31116449bd9031dd0ffb00b9ae');
insert into authority_user_join(id, authority_id, user_id)
values (8, 1 * 6 + 5, '7adccf31116449bd9031dd0ffb00b9ae');

insert into authority_user_join(id, authority_id, user_id)
values (9, 1 * 6 + 1, '87c01625b916466e8d813be92e2527bd');
insert into authority_user_join(id, authority_id, user_id)
values (10, 3 * 6 + 1, '87c01625b916466e8d813be92e2527bd');
insert into authority_user_join(id, authority_id, user_id)
values (11, 1 * 6 + 4, '87c01625b916466e8d813be92e2527bd');

insert into authority_user_join(id, authority_id, user_id)
values (12, 1 * 6 + 1, '058e870393bb41e4a114e2cbf005bf54');
insert into authority_user_join(id, authority_id, user_id)
values (13, 3 * 6 + 1, '058e870393bb41e4a114e2cbf005bf54');

insert into authority_user_join(id, authority_id, user_id)
values (14, 2 * 6 + 1, '92e35636d3564cbaac072ec665187b0c');
insert into authority_user_join(id, authority_id, user_id)
values (15, 3 * 6 + 1, '92e35636d3564cbaac072ec665187b0c');
insert into authority_user_join(id, authority_id, user_id)
values (16, 1 * 6 + 3, '92e35636d3564cbaac072ec665187b0c');

insert into authority_user_join(id, authority_id, user_id)
values (17, 1 * 6 + 1, '2831d7478d8e44648d70f063bcfdb735');
insert into authority_user_join(id, authority_id, user_id)
values (18, 3 * 6 + 1, '2831d7478d8e44648d70f063bcfdb735');
insert into authority_user_join(id, authority_id, user_id)
values (19, 1 * 6 + 2, '2831d7478d8e44648d70f063bcfdb735');

insert into authority_user_join(id, authority_id, user_id)
values (20, 2 * 6 + 1, '52570b695c164bdbbc1e063247a93b4d');
insert into authority_user_join(id, authority_id, user_id)
values (21, 3 * 6 + 1, '52570b695c164bdbbc1e063247a93b4d');
insert into authority_user_join(id, authority_id, user_id)
values (22, 3 * 6 + 5, '52570b695c164bdbbc1e063247a93b4d');

insert into authority_user_join(id, authority_id, user_id)
values (23, 2 * 6 + 1, '561953f1bae34f3491a9d5fcd16f05ca');
insert into authority_user_join(id, authority_id, user_id)
values (24, 3 * 6 + 1, '561953f1bae34f3491a9d5fcd16f05ca');
insert into authority_user_join(id, authority_id, user_id)
values (25, 1 * 6 + 2, '561953f1bae34f3491a9d5fcd16f05ca');

insert into authority_user_join(id, authority_id, user_id)
values (26, 2 * 6 + 1, '35a92ffd416c49d5ae64f5af337246a5');
insert into authority_user_join(id, authority_id, user_id)
values (27, 3 * 6 + 1, '35a92ffd416c49d5ae64f5af337246a5');

insert into authority_user_join(id, authority_id, user_id)
values (28, 2 * 6 + 1, '4070d6da6b5a4decb07618fa9bc52777');
insert into authority_user_join(id, authority_id, user_id)
values (29, 3 * 6 + 1, '4070d6da6b5a4decb07618fa9bc52777');
insert into authority_user_join(id, authority_id, user_id)
values (30, 2 * 6 + 4, '4070d6da6b5a4decb07618fa9bc52777');

insert into authority_user_join(id, authority_id, user_id)
values (31, 5 * 6 + 1, 'a010437a9b4c462980365a5410ced8cc');
insert into authority_user_join(id, authority_id, user_id)
values (32, 5 * 6 + 5, 'a010437a9b4c462980365a5410ced8cc');

insert into authority_user_join(id, authority_id, user_id)
values (33, 4 * 6 + 1, 'cca1df09197f4baeacec7841a9da9397');
insert into authority_user_join(id, authority_id, user_id)
values (34, 5 * 6 + 1, 'cca1df09197f4baeacec7841a9da9397');
insert into authority_user_join(id, authority_id, user_id)
values (35, 4 * 6 + 4, 'cca1df09197f4baeacec7841a9da9397');

insert into authority_user_join(id, authority_id, user_id)
values (36, 5 * 6 + 1, 'e74bd461eb4a4c9792abf797b4438a88');

insert into authority_user_join(id, authority_id, user_id)
values (37, 4 * 6 + 1, '2c113864dfce4aab92aaef756c7caa55');
insert into authority_user_join(id, authority_id, user_id)
values (38, 5 * 6 + 1, '2c113864dfce4aab92aaef756c7caa55');

insert into authority_user_join(id, authority_id, user_id)
values (39, 5 * 6 + 1, '119b4a30285f4f0aa037a9134b7e7bb7');

-- insert stuff
insert into stuff(id, department_id, name, thumbnail) values ('5f35946da596466c94d30a09431a6309', 'c7312fe706264068911251a1b8e04309', '블루투스 스피커', '📻');
insert into stuff(id, department_id, name, thumbnail) values ('b1b566baef964bff9883abb62aec7245', 'c7312fe706264068911251a1b8e04309', '우산', '🌂');
insert into stuff(id, department_id, name, thumbnail) values ('02143755e80c472b9a231f9624364846', 'c7312fe706264068911251a1b8e04309', '볼펜', '🖋️');
insert into stuff(id, department_id, name, thumbnail) values ('ec5de918030f40beb0603ddb415e358e', 'c7312fe706264068911251a1b8e04309', '가위', '✂️');
insert into stuff(id, department_id, name, thumbnail) values ('a6aee430b84247c6a6efc7410869b4a7', 'c7312fe706264068911251a1b8e04309', '스케이트보드', '🛹');

insert into stuff(id, department_id, name, thumbnail) values ('d540534434f64b84beaf24f6506a4fc7', 'b069fa11c416427696e1aaad27f73aeb', '우산', '🌂');
insert into stuff(id, department_id, name, thumbnail) values ('13801bd5b74a43bbb32b323c3c59f9f2', 'b069fa11c416427696e1aaad27f73aeb', '가위', '✂️');
insert into stuff(id, department_id, name, thumbnail) values ('fe54a40127a64561b749ad392024185a', 'b069fa11c416427696e1aaad27f73aeb', '망치', '🔨');

insert into stuff(id, department_id, name, thumbnail) values ('a731bba0f5ee49cea8309e5ae1727dad', '9ebea9d296694560990c3628ddc29d23', '우산', '🌂');
insert into stuff(id, department_id, name, thumbnail) values ('6223fa3673674099b0d89bbfff04c755', '9ebea9d296694560990c3628ddc29d23', '볼펜', '🖋️');
insert into stuff(id, department_id, name, thumbnail) values ('cda0319e0e8541c98da733ccf04b4311', '9ebea9d296694560990c3628ddc29d23', '가위', '✂️');
insert into stuff(id, department_id, name, thumbnail) values ('9ada08c580004ed8a0882486b25d8cf2', '9ebea9d296694560990c3628ddc29d23', '계산기', '🧮️');

insert into stuff(id, department_id, name, thumbnail) values ('89a9e2c16102475db1a8ce237c0b0223', 'fd16b1f820ff47ef9a7d46e5d1e1c080', '볼펜', '🖋️');
insert into stuff(id, department_id, name, thumbnail) values ('0613bdfd71a046ee910d51e9b058efee', 'fd16b1f820ff47ef9a7d46e5d1e1c080', '가위', '✂');
insert into stuff(id, department_id, name, thumbnail) values ('eebb33783d044ba9a1cc297d2d0ab156', 'fd16b1f820ff47ef9a7d46e5d1e1c080', '축구공', '⚽');

-- insert item
insert into item(id, stuff_id, num, last_history_id) values ('91fe5765d8da4590bb5f0cc9325a0f99', '5f35946da596466c94d30a09431a6309', 1, null);
insert into item(id, stuff_id, num, last_history_id) values ('781007d9ac1f440aa2a8faffcd366b9a', '5f35946da596466c94d30a09431a6309', 2, null);
insert into item(id, stuff_id, num, last_history_id) values ('979acb01ea6f47c7a32f9faaa5bc6fd2', 'b1b566baef964bff9883abb62aec7245', 1, null);
insert into item(id, stuff_id, num, last_history_id) values ('e9bcb3df40a14132b6d357814fb42b9b', 'b1b566baef964bff9883abb62aec7245', 2, null);
insert into item(id, stuff_id, num, last_history_id) values ('dee510ec87b2483cbdbb26798464f1f7', 'b1b566baef964bff9883abb62aec7245', 3, null);
insert into item(id, stuff_id, num, last_history_id) values ('8977e997e60a432196160da5d2e92169', 'b1b566baef964bff9883abb62aec7245', 4, null);
insert into item(id, stuff_id, num, last_history_id) values ('daf5957960294450a635f5fb8a2ff7f9', '02143755e80c472b9a231f9624364846', 1, null);
insert into item(id, stuff_id, num, last_history_id) values ('573c1eef52674b1e9f18c5539425ad52', '02143755e80c472b9a231f9624364846', 2, null);
insert into item(id, stuff_id, num, last_history_id) values ('13b0d597f45c4c2990eb583b5ae81f45', '02143755e80c472b9a231f9624364846', 3, null);
insert into item(id, stuff_id, num, last_history_id) values ('d0c3732d6c624398b43de59b421314ce', '02143755e80c472b9a231f9624364846', 4, null);
insert into item(id, stuff_id, num, last_history_id) values ('ee7c67cea9ce475f83c8040423c36586', '02143755e80c472b9a231f9624364846', 5, null);
insert into item(id, stuff_id, num, last_history_id) values ('b1c9e3c9dc2f4deda7ff4923d34f55fd', '02143755e80c472b9a231f9624364846', 6, null);
insert into item(id, stuff_id, num, last_history_id) values ('a97821c229c74434992b5ceb9f6d7ca0', 'ec5de918030f40beb0603ddb415e358e', 1, null);
insert into item(id, stuff_id, num, last_history_id) values ('57a88c8d446f4d858cad7bb9df81f5e7', 'ec5de918030f40beb0603ddb415e358e', 2, null);
insert into item(id, stuff_id, num, last_history_id) values ('59b46b319e3544f6b9a64a8970481438', 'ec5de918030f40beb0603ddb415e358e', 3, null);
insert into item(id, stuff_id, num, last_history_id) values ('fde3b0bff1b4452986df844af597244a', 'a6aee430b84247c6a6efc7410869b4a7', 1, null);

-- insert history
insert into history(id, item_id, num, requester_id, approve_manager_id, return_manager_id, lost_manager_id, cancel_manager_id, requested_at, approved_at, returned_at, lost_at, canceled_at)
values ('f820abebeaa341169850d2d6867da7d8', '91fe5765d8da4590bb5f0cc9325a0f99', 1, '058e8703-93bb-41e4-a114-e2cbf005bf54', null, null, null, null, 1673155356, 0, 0, 0, 0);
update item
set last_history_id='f820abebeaa341169850d2d6867da7d8'
where id = '91fe5765d8da4590bb5f0cc9325a0f99';

insert into history(id, item_id, num, requester_id, approve_manager_id, return_manager_id, lost_manager_id, cancel_manager_id, requested_at, approved_at, returned_at, lost_at, canceled_at)
values ('d55195deaa604dac8beef04ddb713fa6', '91fe5765d8da4590bb5f0cc9325a0f99', 2, '058e8703-93bb-41e4-a114-e2cbf005bf54', '7adccf31116449bd9031dd0ffb00b9ae', null, null, null, 1673155193, 1673155275, 0, 0, 0);
update item
set last_history_id='d55195deaa604dac8beef04ddb713fa6'
where id = '91fe5765d8da4590bb5f0cc9325a0f99';

insert into history(id, item_id, num, requester_id, approve_manager_id, return_manager_id, lost_manager_id, cancel_manager_id, requested_at, approved_at, returned_at, lost_at, canceled_at)
values ('dcf372d11df14e04b2c568cdcf314bd8','781007d9ac1f440aa2a8faffcd366b9a', 1, '92e35636-d356-4cba-ac07-2ec665187b0c', '87c01625b916466e8d813be92e2527bd', null, '7adccf31116449bd9031dd0ffb00b9ae', null, 1673195193, 1673195275, 0, 1673209275, 0);
update item
set last_history_id='dcf372d11df14e04b2c568cdcf314bd8'
where id = '781007d9ac1f440aa2a8faffcd366b9a';

insert into history(id, item_id, num, requester_id, approve_manager_id, return_manager_id, lost_manager_id, cancel_manager_id, requested_at, approved_at, returned_at, lost_at, canceled_at)
values ('a9b617f99a784691b024b79fd0b0d884', '979acb01ea6f47c7a32f9faaa5bc6fd2', 1, '92e35636-d356-4cba-ac07-2ec665187b0c', null, null, null, null, UNIX_TIMESTAMP(), 0, 0, 0, 0);
update item
set last_history_id='a9b617f99a784691b024b79fd0b0d884'
where id = '979acb01ea6f47c7a32f9faaa5bc6fd2';

insert into history(id, item_id, num, requester_id, approve_manager_id, return_manager_id, lost_manager_id, cancel_manager_id, requested_at, approved_at, returned_at, lost_at, canceled_at)
values ('e2e473478fed4d62b18bdc8765add9ce', '13b0d597f45c4c2990eb583b5ae81f45', 1, '92e35636-d356-4cba-ac07-2ec665187b0c', '87c01625b916466e8d813be92e2527bd', null, null, null, 1673195193, 1673195275, 0, 0, 0);
update item
set last_history_id='e2e473478fed4d62b18bdc8765add9ce'
where id = '13b0d597f45c4c2990eb583b5ae81f45';