insert into university(code, name, api_url) values ('HYU', '한양대학교', 'https://api.hanyang.ac.kr/oauth/authorize/');
insert into university(code, name, api_url) values ('CKU', '가톨릭관동대학교', null);
insert into university(code, name, api_url) values ('SNU', '서울대학교', null);

insert into major(university_id, code) values (1, 'FH04067');
insert into major(university_id, code) values (1, 'FH04068');
insert into major(university_id, code) values (1, 'FH04069');
insert into major(university_id, code) values (2, 'TEST');
insert into major(university_id, code) values (1, 'TEST');
insert into major(university_id, code) values (2, 'A68');
insert into major(university_id, code) values (2, 'A69');
insert into major(university_id, code) values (2, 'A70');