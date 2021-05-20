-- ***** �������̺� ***** --
create table tbl_position
( pcode   varchar2(3)   not null   -- �����ڵ�
, pname   varchar2(50)  not null   -- ���޸�
, offcnt  varchar2(3)   not null   -- �⺻������
, salary  varchar2(50)  not null   -- �⺻��
, commissionpercase   varchar2(50)    not null  -- �Ǵ缺����
, constraint PK_tbl_position_pcode primary key(pcode)
, constraint UK_tbl_position_pname unique(pname)
);
-- Table TBL_POSITION��(��) �����Ǿ����ϴ�.

create sequence seq_tbl_position_pcode
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_POSITION_PCODE��(��) �����Ǿ����ϴ�.

-- ***** �μ����̺� ***** --
create table tbl_department
( dcode      varchar2(3)    not null  -- �μ��ڵ�    
, dname      varchar2(50)   not null  -- �μ���
, managerid  varchar2(50)   not null  -- �μ�����
, duty       varchar2(100)  not null  -- ����
, constraint PK_tbl_department_dcode primary key(dcode)
, constraint UK_tbl_department_dname unique(dname)
, constraint FK_tbl_department_managerid foreign key (managerid) references tbl_employee(employeeid)
);
-- Table TBL_DEPARTMENT��(��) �����Ǿ����ϴ�.

create sequence seq_tbl_department_dcode
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_DEPARTMENT_DCODE��(��) �����Ǿ����ϴ�.


-- ***** �������̺� ***** --
create table tbl_employee
( employeeid   varchar2(50)    not null   -- ���
, fk_dcode     varchar2(3)                -- �μ��ڵ�
, fk_pcode     varchar2(3)                -- �����ڵ�
, name         varchar2(50)    not null   -- ������
, passwd       varchar2(100)   not null   -- ��й�ȣ
, email        varchar2(100)   not null   -- ȸ���̸���
, mobile       varchar2(50)               -- ����ó   
, cmobile      varchar2(50)    not null   -- ȸ�翬��ó
, jubun        varchar2(100)   not null   -- �ֹι�ȣ
, hiredate     date            default sysdate not null  -- �Ի�����
, status       varchar2(2)     default '0' not null     -- �������� (0:������, 1:���)
, managerid    varchar2(50)    null   -- ���ӻ����
, employeeimg  varchar2(100)   default 'noimage.png'  -- �����̹���
, constraint  PK_tbl_employee_employeeid primary key(employeeid)
, constraint  FK_tbl_employee_fk_dcode foreign key(fk_dcode) references tbl_department(dcode) on delete cascade
, constraint  FK_tbl_employee_fk_pcode foreign key(fk_pcode) references tbl_position(pcode) on delete cascade
, constraint  UK_tbl_employee_email unique(email)
, constraint  UK_tbl_employee_mobile unique(mobile)
, constraint  UK_tbl_employee_cmobile unique(cmobile)
, constraint  CK_tbl_employee_status check (status in ('0','1'))
);
-- Table TBL_EMPLOYEE��(��) �����Ǿ����ϴ�.


create sequence seq_tbl_employee_employeeid
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_EMPLOYEE_EMPLOYEEID��(��) �����Ǿ����ϴ�.

-- �α��� ��� ���̺� ����
create table login_history
( loginno        varchar2(100)   not null     -- ����
, fk_employeeid  varchar2(50)    not null     -- ���
, loginip        varchar2(100)   not null     -- ����ip
, logindate      varchar2(100)   default sysdate not null --�α��� ��¥ �ð�
, constraint  PK_login_history_loginno primary key(loginno)
, constraint  FK_login_history_fk_employeeid foreign key(fk_employeeid) references tbl_employee(employeeid) 
);
-- Table LOGIN_HISTORY��(��) �����Ǿ����ϴ�.

create sequence seq_login_history_loginno
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_LOGIN_HISTORY_LOGINNO��(��) �����Ǿ����ϴ�.


-- ����ٱ�� ���̺�
create table tbl_inout
( gooutdate        date            not null -- ��¥
, fk_employeeid    varchar2(50)    not null -- ���
, intime           date            not null -- ��ٽð�
, outtime          date            not null -- ��ٽð�
, lateno           varchar2(2)     not null -- ��������(0 :������� 1: ���� )
, constraint PK_tbl_inout_gooutdate primary key(gooutdate)
, constraint FK_tbl_inout_fk_employeeid foreign key(fk_employeeid) references tbl_employee(employeeid) 
, constraint CK_tbl_inout_lateno check (lateno in ('0','1'))
)
-- Table TBL_INOUT��(��) �����Ǿ����ϴ�.

create sequence seq_tbl_inout_gooutdate
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_INOUT_GOOUTDATE��(��) �����Ǿ����ϴ�.


-- ȸ����ġ ���̺�
create table tbl_twmap 
(storeCode     varchar2(20)  not null     --  �����ڵ�
,storeName     varchar2(100) not null     --  ������
,storeAddress  varchar2(200)              --  �����ּ�
,storeImg      varchar2(200) not null     --  ���� �Ұ��̹���
,storePhone    varchar2(200) not null     --  ������ȭ��ȣ
,lat           varchar2(100) not null     --  ����
,lng           varchar2(100) not null     --  �浵 
,zindex        varchar2(100) not null     --  zindex 
,constraint PK_tbl_twmap_storeCode primary key(storeCode)
,constraint UQ_tbl_twmap_zindex unique(zindex)
);
-- Table TBL_TWMAP��(��) �����Ǿ����ϴ�.

create sequence seq_tbl_twmap_zindex
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_TWMAP_ZINDEX��(��) �����Ǿ����ϴ�.

-- �������̺�
create table tbl_todo
( todoNo         varchar2(50)   not null -- ����
, fk_dcode       varchar2(3)    not null  -- �μ��ڵ�
, projectName    varchar2(200)  not null -- ������Ʈ��
, fk_managerid   varchar2(50)   not null -- �����ڻ��
, assignDate     date                    -- ������
, startDate      date                    -- ������
, endDate        date                    -- �Ϸ���
, fk_employeeid  varchar2(50)            -- ����ڻ��
, hurryno        varchar2(2)    not null -- ��޿���(0:�Ϲ� 1: ���)
, constraint PK_tbl_todo_todoNo primary key(todoNo)
, constraint FK_tbl_todo_fk_dcode foreign key(fk_dcode) references tbl_department(dcode) 
, constraint FK_tbl_todo_fk_managerid foreign key(fk_managerid) references tbl_employee(employeeid) 
, constraint FK_tbl_todo_fk_employeeid foreign key(fk_employeeid) references tbl_employee(employeeid)
, constraint CK_tbl_todo_hurryno check(hurryno in ('0','1'))
)
-- Table TBL_TODO��(��) �����Ǿ����ϴ�.


create sequence seq_tbl_todo_todoNo
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_TODO_TODONO��(��) �����Ǿ����ϴ�.

-- �������̺�
create table tbl_reserve
( reserveNo varchar2(50)   not null -- ����
, miniNo    varchar2(50)   not null -- �ּҿ����ο�
, maxNo     varchar2(50)   not null -- �ִ뿹���ο�
, nowNo     varchar2(50)   not null -- �ִ뿹���ο�
, constraint PK_tbl_reserve_reserveNo primary key(reserveNo)
)
-- Table TBL_RESERVE��(��) �����Ǿ����ϴ�.

create sequence seq_tbl_reserve_reserveNo
start with 1
increment by 1
nomaxvalue
nominvalue
nocycle
nocache;
-- Sequence SEQ_TBL_RESERVE_RESERVENO��(��) �����Ǿ����ϴ�.