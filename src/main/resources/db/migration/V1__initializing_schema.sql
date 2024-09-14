-- create tables
DROP PROCEDURE IF EXISTS AddIndexIfNotExists;

create table IF NOT EXISTS roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) not null
);

create table IF NOT EXISTS users
(
    id           bigint auto_increment
        primary key,
    firstname    varchar(255) not null,
    lastname     varchar(255) not null,
    email        varchar(255) not null,
    password     varchar(255) not null,
    role_id      bigint       null,
    is_enabled   tinyint(1)   not null,
    phone_number varchar(255) not null,
    created_at   timestamp    not null
);

create table IF NOT EXISTS athletes
(
    id            bigint                                                       not null
        primary key,
    licence_id    varchar(255)                                                 null,
    date_of_birth date                                                         not null,
    has_medal     tinyint(1)                                                   null,
    age           int                                                          null,
    branch        enum ('HEALTH_SPORT', 'PERFORMANCE_SPORT' ,'DISABLED_SPORT') not null
);


create table IF NOT EXISTS admins
(
    id bigint not null primary key
);

create table IF NOT EXISTS tokens
(
    id         bigint auto_increment
        primary key,
    token      varchar(255)                  not null,
    token_type varchar(255) default 'BEARER' null,
    revoked    tinyint(1)                    not null,
    expired    tinyint(1)                    not null,
    user_id    bigint                        null
);

create table IF NOT EXISTS refresh_tokens
(
    id         bigint auto_increment
        primary key,
    token      varchar(255) null,
    expired    tinyint(1)   not null,
    revoked    tinyint(1)   not null,
    issued_at  timestamp    not null,
    expires_at timestamp    not null,
    user_id    bigint       null
);

create table IF NOT EXISTS confirm_tokens
(
    id           bigint auto_increment
        primary key,
    token        varchar(255) not null,
    created_at   timestamp    not null,
    expired_at   timestamp    not null,
    user_id      bigint       not null,
    confirmed_at datetime(6)  null
);

create table IF NOT EXISTS events
(
    id          bigint auto_increment
        primary key,
    title       varchar(255)                                 not null,
    date        timestamp                                    not null,
    location    varchar(255)                                 not null,
    description text                                         null,
    created_by  bigint                                       null,
    created_at  timestamp default CURRENT_TIMESTAMP          null,
    updated_at  timestamp                                    null,
    type        enum ('LONGUEUR', '_100M', '_400m', '_800M') null
);

create table IF NOT EXISTS athlete_events
(
    athlete_id bigint not null,
    event_id   bigint not null,
    primary key (athlete_id, event_id)
);


create table IF NOT EXISTS announcements
(
    id         bigint auto_increment
        primary key,
    title      varchar(255)                        not null,
    content    text                                not null,
    created_by bigint                              null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp                           null
);

create table IF NOT EXISTS files
(
    id              bigint auto_increment
        primary key,
    name            varchar(255)                        not null,
    type            varchar(255)                        not null,
    path            varchar(255)                        not null,
    uploaded_at     timestamp default CURRENT_TIMESTAMP not null,
    uploaded_by     bigint                              null,
    event_id        bigint                              null,
    announcement_id bigint                              null
);

create table IF NOT EXISTS forum_comments
(
    id                bigint auto_increment
        primary key,
    content           varchar(255)                        not null,
    likes_count       int       default 0                 null,
    created_at        timestamp default CURRENT_TIMESTAMP not null,
    posted_by         bigint                              null,
    parent_comment_id bigint                              null,
    updated_at        timestamp                           not null
);

-- create index


DELIMITER //

CREATE PROCEDURE AddIndexIfNotExists(
    IN tbl_name VARCHAR(64),
    IN idx_name VARCHAR(64),
    IN idx_columns VARCHAR(255)
)
BEGIN
    DECLARE idx_count INT;

    -- Check if the index exists
    SELECT COUNT(*)
    INTO idx_count
    FROM information_schema.statistics
    WHERE table_schema = DATABASE()
      AND table_name = tbl_name
      AND index_name = idx_name;

    -- Add the index if it does not exist
    IF idx_count = 0 THEN
        SET @sql = CONCAT('ALTER TABLE ', tbl_name, ' ADD INDEX ', idx_name, ' (', idx_columns, ');');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END//

DELIMITER ;
CALL AddIndexIfNotExists('announcements', 'idx_announcements_created_by', 'created_by');
CALL AddIndexIfNotExists('athlete_events', 'idx_athlete_events_event_id', 'event_id');
CALL AddIndexIfNotExists('files', 'idx_files_event_id', 'event_id');
CALL AddIndexIfNotExists('files', 'idx_files_uploaded_by', 'uploaded_by');
CALL AddIndexIfNotExists('forum_comments', 'idx_forum_comments_parent_comment_id', 'parent_comment_id');
CALL AddIndexIfNotExists('forum_comments', 'idx_forum_comments_posted_by', 'posted_by');
CALL AddIndexIfNotExists('tokens', 'idx_tokens_user_id', 'user_id');











