delete
from service_category
where depth = 2;
delete
from service_category;

insert into service_category (id, name, parent_category_id, depth, start_datetime, end_datetime, updatable_role,
                              priority_no_mobile, priority_no_pc, apply_target, pc_url, mobile_url, deep_link_url,
                              use_flag, delete_flag, creator, created, updater, updated)
values (99, '수진카테고리', null, 1, '2020-12-01 00:00:00', '2020-12-31 00:00:00', 'ALL', 1, 1, 'ALL',
        'https://tour-dev.wd.wemakeprice.com/activity', 'https://tour-dev.wd.wemakeprice.com/activity', '1', true,
        false, '2019040004', '2020-12-09 15:29:49', '2019040004', '2020-12-10 16:52:54');
insert into service_category (id, name, parent_category_id, depth, start_datetime, end_datetime, updatable_role,
                              priority_no_mobile, priority_no_pc, apply_target, pc_url, mobile_url, deep_link_url,
                              use_flag, delete_flag, creator, created, updater, updated)
values (104, '인코딩 링크', 99, 2, '2020-12-01 00:00:00', '2020-12-31 00:00:00', 'ALL', 2, 2, 'ALL', '1', '1', '1', true,
        false, '2019040004', '2020-12-09 15:40:26', '2019040004', '2020-12-11 15:23:46');
