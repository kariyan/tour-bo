delete
from account;
delete
from department;

insert into account (account_id, authority_group_cd, account_name, account_email, phone, employment_cd, position_cd,
                     job_cd, ones_duty_cd, department_cd, account_status, change_date, use_flag, delete_flag,
                     cs_passwd_change_flag, cs_passwd, passwd_change_date, passwd_fail_cnt, creator, created, updater,
                     updated)
values ('2018110011', 'MASTER', '김은택', '***', '***', '10',
        '5', 'A0500', '180', 'WST0000126', 'WORK', '2020-12-13', 1, 0, 0, null, null, 0, '1111111111',
        '2019-07-23 10:18:34', 'SYSTEM', '2020-12-14 03:10:13');
insert into account (account_id, authority_group_cd, account_name, account_email, phone, employment_cd, position_cd,
                     job_cd, ones_duty_cd, department_cd, account_status, change_date, use_flag, delete_flag,
                     cs_passwd_change_flag, cs_passwd, passwd_change_date, passwd_fail_cnt, creator, created, updater,
                     updated)
values ('2019060005', 'MASTER', '이원희', '***', '***', '10',
        '5', 'A0500', '180', 'WSS4001037', 'WORK', '2020-12-14', 1, 0, 0, null, null, 0, '1111111111',
        '2019-07-23 10:16:59', 'SYSTEM', '2020-12-15 03:10:13');

insert into department (department_cd, parent_department_cd, department_name, department_level, department_order,
                        department_email, change_date, delete_flag, creator, created, updater, updated)
values ('WST0000126', 'WSS4001036', '투어컬처개발파트', 5, 3, '***@wemakeprice.com', '2020-12-01', 0, 'SYSTEM',
        '2020-06-24 03:40:01', 'SYSTEM', '2020-12-15 03:40:04');
insert into department (department_cd, parent_department_cd, department_name, department_level, department_order,
                        department_email, change_date, delete_flag, creator, created, updater, updated)
values ('WSS4001037', 'WSS4001036', '프론트개발파트', 5, 1, '***@wemakeprice.com', '2020-12-01', 0,
        '1111111111', '2019-07-23 10:29:17', 'SYSTEM', '2020-12-15 03:40:04');
