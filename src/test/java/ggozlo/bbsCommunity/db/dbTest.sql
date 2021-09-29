INSERT INTO member(id, email, login_id, name, password) values (nextval('member_seq'),'abc', 'qq', 'wwe', 'pw');
INSERT INTO member(id, email, login_id, name, password) values (nextval('member_seq'),'zxc', 'asd', 'qwe', 'rfv');

select * FROM member;