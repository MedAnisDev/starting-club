-- create tables
DROP PROCEDURE IF EXISTS AddIndexIfNotExists;
DROP PROCEDURE IF EXISTS AddUniqueConstraintIfNotExists;
DROP PROCEDURE IF EXISTS AddForeignKeyIfNotExists;

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

-- Add unique constraint
DELIMITER //
CREATE PROCEDURE AddUniqueConstraintIfNotExists(
    IN tbl_name VARCHAR(64),
    IN uk_name VARCHAR(64),
    IN column_list VARCHAR(255)
)
BEGIN
    DECLARE constraint_count INT;

    -- check unique constraint existence
    SELECT COUNT(*)
    INTO constraint_count
    FROM information_schema.table_constraints
    WHERE table_schema = DATABASE()
      AND table_name = tbl_name
      AND CONSTRAINT_NAME = uk_name
      AND constraint_type = 'UNIQUE';

    -- add unique constraint if it doesn't exist
    IF constraint_count = 0 THEN
        SET @sql = CONCAT('ALTER TABLE ', tbl_name, ' ADD CONSTRAINT ', uk_name, ' UNIQUE (', column_list, ');');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE AddForeignKeyIfNotExists(
    IN tbl_name VARCHAR(64),
    IN fk_name VARCHAR(64),
    IN fk_columns VARCHAR(255),
    IN ref_tbl_name VARCHAR(64),
    IN ref_columns VARCHAR(255)
)
BEGIN
    DECLARE fk_count INT;

    -- check foreign key existence
    SELECT COUNT(*)
    INTO fk_count
    FROM information_schema.table_constraints tc
             JOIN information_schema.key_column_usage kcu
                  ON tc.constraint_name = kcu.constraint_name
    WHERE tc.table_schema = DATABASE()
      AND tc.table_name = tbl_name
      AND tc.constraint_type = 'FOREIGN KEY'
      AND tc.constraint_name = fk_name
      AND kcu.referenced_table_name = ref_tbl_name;

    -- add foreign key if it doesn't exist
    IF fk_count = 0 THEN
        SET @sql = CONCAT('ALTER TABLE ', tbl_name, ' ADD CONSTRAINT ', fk_name, ' FOREIGN KEY (', fk_columns,
                          ') REFERENCES ', ref_tbl_name, ' (', ref_columns, ');');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END//
DELIMITER ;

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
    announcement_id bigint                              null,
    athlete_id      BIGINT                              null
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

create table IF NOT EXISTS performances
(
    id              bigint auto_increment
        primary key,
    federation_note DOUBLE    DEFAULT 0.00,
    created_at      TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP                           NOT NULL,
    created_by      BIGINT                              NOT NULL,
    updated_by      BIGINT,
    athlete_id      BIGINT
);

create table IF NOT EXISTS training_sessions
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    date           DATE                                NOT NULL,
    session_note   DOUBLE    DEFAULT 0.00              NOT NULL,
    created_at     TIMESTAMP default CURRENT_TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP                           NOT NULL,
    created_by     BIGINT                              NOT NULL,
    updated_by     BIGINT,
    performance_id BIGINT
);


CREATE TABLE IF NOT EXISTS event_performance
(
    id         BIGINT auto_increment primary key,
    athlete_id BIGINT,
    event_id   BIGINT NOT NULL,
    note       DOUBLE DEFAULT 0.00
);


CALL AddIndexIfNotExists('announcements', 'idx_announcements_created_by', 'created_by');

CALL AddIndexIfNotExists('athlete_events', 'idx_athlete_events_event_id', 'event_id');

CALL AddIndexIfNotExists('files', 'idx_files_event_id', 'event_id');
CALL AddIndexIfNotExists('files', 'idx_files_uploaded_by', 'uploaded_by');

CALL AddIndexIfNotExists('forum_comments', 'idx_forum_comments_parent_comment_id', 'parent_comment_id');
CALL AddIndexIfNotExists('forum_comments', 'idx_forum_comments_posted_by', 'posted_by');

CALL AddIndexIfNotExists('tokens', 'idx_tokens_user_id', 'user_id');

CALL AddIndexIfNotExists('performances', 'idx_performances_athlete_id', 'athlete_id');
CALL AddIndexIfNotExists('performances', 'idx_performances_created_by', 'created_by');
CALL AddIndexIfNotExists('performances', 'idx_performances_updated_by', 'updated_by') ;

CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_performance_id', 'performance_id');
CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_created_by', 'created_by');
CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_updated_by', 'updated_by');










