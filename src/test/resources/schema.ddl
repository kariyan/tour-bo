alter database test character set = utf8mb4 collate = utf8mb4_unicode_ci;

/***********************************************************************************************************************
 * wtour_deal
 **********************************************************************************************************************/

drop table if exists account restrict;
drop table if exists department restrict;

create table account
(
    account_id            varchar(30)                           not null comment '계정ID'
        primary key,
    authority_group_cd    varchar(30)                           null comment '권한그룹',
    account_name          varchar(200)                          not null comment '계정이름',
    account_email         varchar(1000)                         null comment '계정이메일',
    phone                 varchar(100)                          null comment '연락',
    employment_cd         varchar(30)                           null comment '고용코',
    position_cd           varchar(30)                           null comment '직위코드',
    job_cd                varchar(30)                           null comment '직군코드',
    ones_duty_cd          varchar(30)                           null comment '직책',
    department_cd         varchar(30)                           null comment '조직코드',
    account_status        varchar(30)                           null comment '재직중: WORK, 휴직: TIMEOFF, 퇴직 : RETIREMENT',
    change_date           date                                  null,
    use_flag              tinyint(1)  default 1                 null comment '이용가능 : true, 이용불가: false',
    delete_flag           tinyint(1)  default 0                 null comment '삭제:true, 비삭제:false',
    cs_passwd_change_flag tinyint(1)  default 0                 null comment '초기비밀번호변경유무 변경: true, 미변경:false',
    cs_passwd             varchar(256)                          null comment '고객센터비밀번호',
    passwd_change_date    date                                  null comment '패스워드변경일자',
    passwd_fail_cnt       smallint(1) default 0                 null,
    creator               varchar(30)                           not null comment '생성자',
    created               datetime    default current_timestamp null,
    updater               varchar(30)                           null comment '수정자',
    updated               datetime    default current_timestamp null on update current_timestamp comment '업데이트시간'
)
    comment '계정';

create table department
(
    department_cd        varchar(30)                          not null comment '조직코드'
        primary key,
    parent_department_cd varchar(30)                          not null comment '부모조직코드',
    department_name      varchar(50)                          null comment '조직이름',
    department_level     tinyint                              null comment '조직단계',
    department_order     int                                  null comment '조직정렬순',
    department_email     varchar(100)                         null comment '조직이메일',
    change_date          date                                 null comment '변경일자',
    delete_flag          tinyint(1) default 0                 null comment '삭제유무',
    creator              varchar(30)                          null comment '생성자',
    created              datetime   default current_timestamp null,
    updater              varchar(30)                          null comment '수정자',
    updated              datetime   default current_timestamp null on update current_timestamp comment '업데이트시간'
)
    comment '조직정보';

/***********************************************************************************************************************
 * tour_main
 **********************************************************************************************************************/

drop table if exists service_category restrict;

create table service_category
(
    id                 int auto_increment comment 'ID'
        primary key,
    name               varchar(11)                           not null comment '이름',
    parent_category_id int                                   null comment '상위카테고리ID',
    depth              smallint(2)                           not null comment '깊이',
    start_datetime     datetime                              null comment '시작일시',
    end_datetime       datetime                              null comment '종료일시',
    updatable_role     varchar(30)                           not null comment '수정가능권한',
    priority_no_mobile int(3)                                null comment '우선순번모바일',
    priority_no_pc     int(3)                                null comment '우선순번PC',
    apply_target       varchar(30) default 'ALL'             not null comment '적용대상',
    use_flag           bit         default b'0'              not null comment '사용여부',
    delete_flag        bit         default b'0'              not null comment '삭제여부',
    pc_url             varchar(2000)                         null comment 'PCURL',
    mobile_url         varchar(2000)                         null comment '모바일URL',
    deep_link_url      varchar(2000)                         null comment '딥링크URL',
    creator            varchar(30)                           not null comment '등록자',
    created            datetime    default current_timestamp not null comment '등록일시',
    updater            varchar(30)                           not null comment '수정자',
    updated            datetime    default current_timestamp not null on update current_timestamp comment '수정일시',
    constraint fk_service_category_ref_service_category
        foreign key (parent_category_id) references service_category (id)
)
    comment '서비스카테고리';

/***********************************************************************************************************************
 * tour_bo
 **********************************************************************************************************************/

-- 조직별접근권한
drop table if exists tb_department_authority restrict;

-- 권한그룹
drop table if exists tb_account_menu restrict;

-- 권한그룹계정별메뉴매핑
drop table if exists tb_authority_group_menu restrict;

-- 메뉴
drop table if exists tb_account_authority_group restrict;

-- 권한그룹별메뉴
drop table if exists tb_authority_group restrict;

-- 접근로그
drop table if exists tb_menu restrict;

-- 계정권한
drop table if exists tb_acc_log restrict;

-- 조직별접근권한
create table tb_department_authority
(
    department_cd varchar(30) not null comment '조직코드',                          -- 조직코드
    created_by    varchar(30) not null comment '등록자',                           -- 등록자
    created_at    datetime    not null default current_timestamp comment '등록일시' -- 등록일시
)
    comment '조직별접근권한';

-- 조직별접근권한
alter table tb_department_authority
    add constraint pk_tb_department_authority -- 조직별접근권한 기본키
        primary key (
                     department_cd -- 조직코드
            );

-- 권한그룹
create table tb_authority_group
(
    authority_group_id integer     not null comment '권한그룹ID',                                                             -- 권한그룹ID
    authority_name     varchar(50) not null comment '권한이름',                                                               -- 권한이름
    authority_type     varchar(30) not null comment '권한유형',                                                               -- 권한유형
    default_menu_id    varchar(30) null comment '기본메뉴ID',                                                                 -- 기본메뉴ID
    delete_flag        bit         not null default false comment '삭제유무',                                                 -- 삭제유무
    created_by         varchar(30) not null comment '등록자',                                                                -- 등록자
    created_at         datetime    not null default current_timestamp comment '등록일시',                                     -- 등록일시
    updated_by         varchar(30) not null comment '수정자',                                                                -- 수정자
    updated_at         datetime    not null default current_timestamp not null on update current_timestamp comment '수정일시' -- 수정일시
)
    comment '권한그룹';

-- 권한그룹
alter table tb_authority_group
    add constraint pk_tb_authority_group -- 권한그룹 기본키
        primary key (
                     authority_group_id -- 권한그룹ID
            );

alter table tb_authority_group
    modify column authority_group_id integer not null auto_increment comment '권한그룹ID';

-- 권한그룹계정별메뉴매핑
create table tb_account_menu
(
    account_id varchar(30) not null comment '계정ID',                                                               -- 계정ID
    menu_id    integer     not null comment '메뉴ID',                                                               -- 메뉴ID
    read_flag  bit         not null default false comment '읽기여부',                                                 -- 읽기여부
    write_flag bit         not null default false comment '쓰기여부',                                                 -- 쓰기여부
    created_by varchar(30) not null comment '등록자',                                                                -- 등록자
    created_at datetime    not null default current_timestamp comment '등록일시',                                     -- 등록일시
    updated_by varchar(30) not null comment '수정자',                                                                -- 수정자
    updated_at datetime    not null default current_timestamp not null on update current_timestamp comment '수정일시' -- 수정일시
)
    comment '권한그룹계정별메뉴매핑';

-- 권한그룹계정별메뉴매핑
alter table tb_account_menu
    add constraint pk_tb_account_menu -- 권한그룹계정별메뉴매핑 기본키
        primary key (
                     account_id, -- 계정ID
                     menu_id -- 메뉴ID
            );

-- 권한그룹계정별메뉴매핑 인덱스
create index idx_tb_account_menu
    on tb_account_menu ( -- 권한그룹계정별메뉴매핑
                        account_id asc -- 계정ID
        );

-- 메뉴
create table tb_menu
(
    menu_id        integer      not null comment '메뉴ID',                                                               -- 메뉴ID
    parent_menu_id integer      null comment '부모메뉴ID',                                                                 -- 부모메뉴ID
    menu_level     smallint(2)  not null comment '메뉴단계',                                                               -- 메뉴단계
    menu_name      varchar(50)  not null comment '메뉴이름',                                                               -- 메뉴이름
    sort_no        smallint(2)  not null comment '정렬',                                                                 -- 정렬
    link           varchar(255) not null comment '링크',                                                                 -- 링크
    use_flag       bit          null     default false comment '사용여부',                                                 -- 사용여부
    delete_flag    bit          not null default false comment '삭제여부',                                                 -- 삭제여부
    created_by     varchar(30)  not null comment '등록자',                                                                -- 등록자
    created_at     datetime     not null default current_timestamp comment '등록일시',                                     -- 등록일시
    updated_by     varchar(30)  not null comment '수정자',                                                                -- 수정자
    updated_at     datetime     not null default current_timestamp not null on update current_timestamp comment '수정일시' -- 수정일시
)
    comment '메뉴';

-- 메뉴
alter table tb_menu
    add constraint pk_tb_menu -- 메뉴 기본키
        primary key (
                     menu_id -- 메뉴ID
            );

alter table tb_menu
    modify column menu_id integer not null auto_increment comment '메뉴ID';

-- 메뉴
alter table tb_menu
    add constraint fk_tb_menu_ref_tb_menu -- 메뉴 -> 메뉴
        foreign key (
                     parent_menu_id -- 부모메뉴ID
            )
            references tb_menu ( -- 메뉴
                                menu_id -- 메뉴ID
                );

-- 권한그룹별메뉴
create table tb_authority_group_menu
(
    menu_id            integer     not null comment '메뉴ID',                                                               -- 메뉴ID
    authority_group_id integer     not null comment '권한그룹ID',                                                             -- 권한그룹ID
    read_flag          bit         not null default false comment '읽기여부',                                                 -- 읽기여부
    write_flag         bit         not null default false comment '쓰기여부',                                                 -- 쓰기여부
    delete_flag        bit         not null default false comment '삭제유무',                                                 -- 삭제유무
    created_by         varchar(30) not null comment '등록자',                                                                -- 등록자
    created_at         datetime    not null default current_timestamp comment '등록일시',                                     -- 등록일시
    updated_by         varchar(30) not null comment '수정자',                                                                -- 수정자
    updated_at         datetime    not null default current_timestamp not null on update current_timestamp comment '수정일시' -- 수정일시
)
    comment '권한그룹별메뉴';

-- 권한그룹별메뉴
alter table tb_authority_group_menu
    add constraint pk_tb_authority_group_menu -- 권한그룹별메뉴 기본키
        primary key (
                     menu_id, -- 메뉴ID
                     authority_group_id -- 권한그룹ID
            );

-- 접근로그
create table tb_acc_log
(
    log_id      integer       not null comment '로그ID',                          -- 로그ID
    log_ip      varchar(40)   null comment '로그ip',                              -- 로그ip
    http_method varchar(30)   null comment 'GET,PUT,POST,DELETE',               -- 요청유형
    log_uri     varchar(2000) null comment 'URI',                               -- 요청uri
    created_by  varchar(30)   not null comment '등록자',                           -- 등록자
    created_at  datetime      not null default current_timestamp comment '등록일시' -- 등록일시
)
    comment '접근로그';

-- 접근로그
alter table tb_acc_log
    add constraint pk_tb_acc_log -- 접근로그 기본키
        primary key (
                     log_id -- 로그ID
            );

alter table tb_acc_log
    modify column log_id integer not null auto_increment comment '로그ID';

-- 계정권한
create table tb_account_authority_group
(
    authority_group_id integer     not null comment '권한그룹ID',                        -- 권한그룹ID
    account_id         varchar(30) not null comment '계정ID',                          -- 계정ID
    created_by         varchar(30) not null comment '등록자',                           -- 등록자
    created_at         datetime    not null default current_timestamp comment '등록일시' -- 등록일시
)
    comment '계정권한';

-- 계정권한
alter table tb_account_authority_group
    add constraint pk_tb_account_authority_group -- 계정권한 기본키
        primary key (
                     authority_group_id, -- 권한그룹ID
                     account_id -- 계정ID
            );

-- 계정권한 인덱스
create index idx_tb_account_authority_group
    on tb_account_authority_group ( -- 계정권한
                                   account_id asc -- 계정ID
        );

-- 권한그룹계정별메뉴매핑
alter table tb_account_menu
    add constraint fk_tb_menu_ref_tb_account_menu -- 메뉴 -> 권한그룹계정별메뉴매핑
        foreign key (
                     menu_id -- 메뉴ID
            )
            references tb_menu ( -- 메뉴
                                menu_id -- 메뉴ID
                );

-- 권한그룹별메뉴
alter table tb_authority_group_menu
    add constraint fk_tb_authority_group_ref_tb_authority_group_menu -- 권한그룹 -> 권한그룹별메뉴
        foreign key (
                     authority_group_id -- 권한그룹ID
            )
            references tb_authority_group ( -- 권한그룹
                                           authority_group_id -- 권한그룹ID
                );

-- 권한그룹별메뉴
alter table tb_authority_group_menu
    add constraint fk_tb_menu_ref_tb_authority_group_menu -- 메뉴 -> 권한그룹별메뉴
        foreign key (
                     menu_id -- 메뉴ID
            )
            references tb_menu ( -- 메뉴
                                menu_id -- 메뉴ID
                );

-- 계정권한
alter table tb_account_authority_group
    add constraint fk_tb_authority_group_ref_tb_account_authority_group -- 권한그룹 -> 계정권한
        foreign key (
                     authority_group_id -- 권한그룹ID
            )
            references tb_authority_group ( -- 권한그룹
                                           authority_group_id -- 권한그룹ID
                );
