drop table IF EXISTS ROLE_MENU;
drop table IF EXISTS ROLE_USER;
drop table IF EXISTS MENU;
drop table IF EXISTS ROLE;
drop table IF EXISTS USER_INFO;
drop table IF EXISTS WEB_LOGS;
drop table IF EXISTS OAUTH_CLIENT_DETAILS;
drop table IF EXISTS OAUTH_TOKEN;
drop table IF EXISTS ROLE_CLIENT;


create table MENU
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    CONTROLLER_CLASS VARCHAR(255),
    ICON_PATH BLOB,
    MARK VARCHAR(255),
    MENU_DESC VARCHAR(255),
    MENU_INDEX INTEGER,
    MENU_NAME VARCHAR(255),
    MENU_URL VARCHAR(255),
    SMALL_ICON_PATH BLOB,
    PARENT_ID VARCHAR(255)
);

create table ROLE
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    ROLE_DESCRIPTION VARCHAR(255),
    ROLE_INDEX INTEGER,
    ROLE_NAME VARCHAR(255)
);


create table USER_INFO
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    ADDRESS VARCHAR(255),
    AVATAR VARCHAR(255),
    AVATAR_CONTENT BLOB,
    BIRTHDAY VARCHAR(255),
    BLOG VARCHAR(255),
    EMAIL VARCHAR(255),
    GENDER VARCHAR(255),
    ID_NUMBER VARCHAR(255),
    INTEGRAL TINYINT default 0,
    LOGIN_NAME VARCHAR(255),
    NICKNAME VARCHAR(255),
    PASSWORD VARCHAR(255),
    PHONE_NUM VARCHAR(255),
    STATE VARCHAR(255),
    TAG VARCHAR(255),
    USER_INDEX INTEGER,
    USER_NAME VARCHAR(255)
);


create table WEB_LOGS
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    END_TIME TIMESTAMP,
    INFO VARCHAR(255),
    LOG_CLASS_NAME VARCHAR(255),
    LOG_CLIENT_IP VARCHAR(255),
    LOG_INFO VARCHAR(255),
    LOG_METHOD_NAME VARCHAR(255),
    LOG_OPERATION_TYPE VARCHAR(255),
    LOG_PRIMARY_KEY VARCHAR(255),
    LOG_SERVER_IP VARCHAR(255),
    LOG_TABLE_NAME VARCHAR(255),
    LOG_URL VARCHAR(255),
    LOG_USER_NAME VARCHAR(255),
    MODULE VARCHAR(255),
    R1 VARCHAR(255),
    R2 VARCHAR(255),
    R3 VARCHAR(255),
    R4 VARCHAR(255),
    R5 VARCHAR(255),
    START_TIME TIMESTAMP,
    TYPE VARCHAR(255)
);


create table ROLE_MENU
(
    MENU_ID VARCHAR(255) not null,
    ROLE_ID VARCHAR(255) not null,
    primary key (MENU_ID, ROLE_ID)
);

create table ROLE_USER
(
    ROLE_ID VARCHAR(255) not null,
    USER_ID VARCHAR(255) not null,
    primary key (ROLE_ID, USER_ID)
);

create table OAUTH_CLIENT_DETAILS
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    ACCESS_TOKEN_VALIDITY INTEGER,
    ADDITIONAL_INFORMATION VARCHAR(4096),
    AUTHORITIES VARCHAR(255),
    AUTHORIZED_GRANT_TYPES VARCHAR(255),
    AUTOAPPROVE VARCHAR(255),
    CLIENT_ID VARCHAR(255),
    CLIENT_SECRET VARCHAR(255),
    CUSTOM_NAME VARCHAR(255),
    REFRESH_TOKEN_VALIDITY INTEGER,
    RESOURCE_IDS VARCHAR(255),
    SCOPE VARCHAR(255),
    WEB_SERVER_REDIRECT_URI VARCHAR(4000)
);

create table OAUTH_TOKEN
(
    ID VARCHAR(255) not null
        primary key,
    CREATED_BY VARCHAR(255),
    CREATED_DATE TIMESTAMP,
    FLAG INTEGER,
    MODIFIED_BY VARCHAR(255),
    MODIFIED_DATE TIMESTAMP,
    REMARK VARCHAR(255),
    ACCESS_TOKEN VARCHAR(1024),
    CLIENT_ID VARCHAR(255),
    EXPIRATION BIGINT,
    EXPIRES_IN INTEGER,
    JTI VARCHAR(255),
    REFRESH_TOKEN VARCHAR(1024),
    REFRESH_TOKEN_EXPIRATION BIGINT,
    REMOTE_ADDRESS VARCHAR(255),
    SCOPE VARCHAR(255),
    SUB VARCHAR(255),
    TOKEN_TYPE VARCHAR(255),
    constraint JTI唯一索引
        unique (FLAG, JTI)
);

create table ROLE_CLIENT
(
    ID VARCHAR(255) not null
        primary key,
    CLIENT_ID VARCHAR(255),
    ROLE_NAME VARCHAR(255)
);