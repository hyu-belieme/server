-- insert university
insert into university(id, code, name, api_url) values (1, 'HYU', '한양대학교', 'https://api.hanyang.ac.kr/oauth/authorize/');
insert into university(id, code, name, api_url) values (2, 'CKU', '가톨릭관동대학교', null);
insert into university(id, code, name, api_url) values (3, 'SNU', '서울대학교', null);

-- insert major
insert into major(id, university_id, code) values (1, 1, 'FH04067');
insert into major(id, university_id, code) values (2, 1, 'FH04068');
insert into major(id, university_id, code) values (3, 1, 'FH04069');
insert into major(id, university_id, code) values (4, 2, 'TEST');
insert into major(id, university_id, code) values (5, 1, 'TEST');
insert into major(id, university_id, code) values (6, 2, 'A68');
insert into major(id, university_id, code) values (7, 2, 'A69');
insert into major(id, university_id, code) values (8, 2, 'A70');

-- insert department
insert into department(id, university_id, code, name) values (1, 1, 'CSE', '컴퓨터소프트웨어학부');
insert into department(id, university_id, code, name) values (2, 1, 'STU', '총학생회');
insert into department(id, university_id, code, name) values (3, 2, 'MED', '의과대학');
insert into department(id, university_id, code, name) values (4, 2, 'STU', '총학생회');

insert into major_department_join(department_id, major_id) values (1, 1);
insert into major_department_join(department_id, major_id) values (1, 2);

insert into major_department_join(department_id, major_id) values (2, 1);
insert into major_department_join(department_id, major_id) values (2, 2);
insert into major_department_join(department_id, major_id) values (2, 3);
insert into major_department_join(department_id, major_id) values (2, 5);

insert into major_department_join(department_id, major_id) values (3, 6);

insert into major_department_join(department_id, major_id) values (4, 4);
insert into major_department_join(department_id, major_id) values (4, 6);
insert into major_department_join(department_id, major_id) values (4, 7);
insert into major_department_join(department_id, major_id) values (4, 8);