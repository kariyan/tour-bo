delete
from tb_account_menu;
delete
from tb_authority_group_menu;
delete
from tb_account_authority_group;
delete
from tb_authority_group;
delete
from tb_menu
where menu_level = 2;
delete
from tb_menu
where menu_level = 1;
delete
from tb_menu;

insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (1, null, 0, '메인', 0, '메인', true, false, '2019030046', '2019-05-21 18:34:10', '2019060005',
        '2020-12-02 12:47:48');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (2, 1, 1, '전시관리', 0, '/exhibition', true, false, '2011111111', '2020-11-27 17:15:11', '2019060005',
        '2020-12-14 16:51:04');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (3, 2, 2, '전시 카테고리관리', 0, '/exhibition/categories', true, false, '2019060005',
        '2020-11-27 18:26:04', '2020060024', '2020-12-14 16:51:04');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (4, 1, 1, '계정관리', 6, '/auth', true, false, '2019030046', '2019-05-21 18:34:10', '2018060020',
        '2020-12-09 21:54:16');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (5, 4, 2, '계정권한관리', 0, '/auth/accounts', true, false, '2019030046',
        '2019-05-21 19:01:41', '2018060020', '2020-12-14 16:51:41');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (6, 4, 2, '메뉴관리', 2, '/auth/menus', true, false, '2019030046', '2019-05-21 19:01:41',
        '2019030046', '2020-12-14 16:52:24');
insert into tb_menu (menu_id, parent_menu_id, menu_level, menu_name, sort_no, link, use_flag, delete_flag,
                     created_by, created_at, updated_by, updated_at)
values (7, 4, 2, '조직별로그인권한관리', 3, '/auth/departments', false, false,
        '2019030046', '2019-05-21 19:01:41', '2018110011', '2020-12-14 16:52:24');

insert into tb_authority_group (authority_group_id, authority_name, authority_type, default_menu_id, delete_flag,
                                created_by, created_at, updated_by, updated_at)
values (1, '관리자', 'ADMIN', '0', false, '2011111111', '2020-11-30 17:55:04', '2011111111', '2020-12-02 12:48:30');
insert into tb_authority_group (authority_group_id, authority_name, authority_type, default_menu_id, delete_flag,
                                created_by, created_at, updated_by, updated_at)
values (3, '사용자', 'USER', '0', false, '2019120013', '2020-12-04 16:46:55', '2019120013', '2020-12-04 16:46:55');

insert into tb_account_authority_group (authority_group_id, account_id, created_by, created_at)
values (3, '2018110011', '2011111111', '2020-12-01 13:22:39');
insert into tb_account_authority_group (authority_group_id, account_id, created_by, created_at)
values (1, '2019060005', '2011111111', '2020-12-01 13:22:39');

insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (1, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (1, 3, true, true, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (2, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (2, 3, true, true, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (3, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (3, 3, true, false, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (4, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (4, 3, true, true, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (5, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (5, 3, false, false, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (6, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (6, 3, false, false, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (7, 1, true, true, false, '2018060020', '2020-12-09 23:31:07', '2018060020', '2020-12-09 23:31:07');
insert into tb_authority_group_menu (menu_id, authority_group_id, read_flag, write_flag, delete_flag, created_by,
                                     created_at, updated_by, updated_at)
values (7, 3, false, false, false, '2018060020', '2020-12-11 15:13:17', '2018060020', '2020-12-11 15:13:17');

insert into tb_account_menu (account_id, menu_id, read_flag, write_flag, created_by, created_at, updated_by, updated_at)
values ('2019060005', 3, false, true, '2019060005', '2020-12-09 15:17:54', '2019060005', '2020-12-09 15:17:54');
insert into tb_account_menu (account_id, menu_id, read_flag, write_flag, created_by, created_at, updated_by, updated_at)
values ('2019060005', 7, true, false, '2019060005', '2020-12-09 15:17:54', '2019060005', '2020-12-09 15:17:54');
insert into tb_account_menu (account_id, menu_id, read_flag, write_flag, created_by, created_at, updated_by, updated_at)
values ('2018110011', 5, true, true, '2019060005', '2020-12-09 15:17:54', '2019060005', '2020-12-09 15:17:54');
