-- Add unique constraint

DROP PROCEDURE IF EXISTS AddUniqueConstraintIfNotExists;

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

CALL AddUniqueConstraintIfNotExists('files', 'UK_files_path', 'path');
CALL AddUniqueConstraintIfNotExists('tokens', 'UK_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('refresh_tokens', 'UK_refresh_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('confirm_tokens', 'UK_confirm_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('users', 'UK_users_email', 'email');
CALL AddUniqueConstraintIfNotExists('users', 'UK_users_phone_number', 'phone_number');
CALL AddUniqueConstraintIfNotExists('athletes', 'UK_athlete_licence_id', 'licence_id');

-- Add foreign keys
DROP PROCEDURE IF EXISTS AddForeignKeyIfNotExists;

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
        SET @sql = CONCAT('ALTER TABLE ', tbl_name, ' ADD CONSTRAINT ', fk_name, ' FOREIGN KEY (', fk_columns, ') REFERENCES ', ref_tbl_name, ' (', ref_columns, ');');
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
    END IF;
END//

DELIMITER ;

CALL AddForeignKeyIfNotExists('users', 'FK_users_role_id', 'role_id', 'roles', 'id');
CALL AddForeignKeyIfNotExists('admins', 'FK_admins_user_id', 'id', 'users', 'id');
CALL AddForeignKeyIfNotExists('athletes', 'FK_athletes_user_id', 'id', 'users', 'id');

CALL AddForeignKeyIfNotExists('tokens', 'FK_tokens_user_id', 'user_id', 'users', 'id');
CALL AddForeignKeyIfNotExists('refresh_tokens', 'FK_refresh_tokens_user_id', 'user_id', 'users', 'id');
CALL AddForeignKeyIfNotExists('confirm_tokens', 'FK_confirm_tokens_user_id', 'user_id', 'athletes', 'id');

CALL AddForeignKeyIfNotExists('events', 'FK_events_created_by', 'created_by', 'admins', 'id');

CALL AddForeignKeyIfNotExists('athlete_events', 'FK_athlete_events_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('athlete_events', 'FK_athlete_events_event_id', 'event_id', 'events', 'id');

CALL AddForeignKeyIfNotExists('announcements', 'FK_announcements_created_by', 'created_by', 'users', 'id');

CALL AddForeignKeyIfNotExists('files', 'FK_files_uploaded_by', 'uploaded_by', 'admins', 'id');
CALL AddForeignKeyIfNotExists('files', 'FK_files_event_id', 'event_id', 'events', 'id');
CALL AddForeignKeyIfNotExists('files', 'FK_files_announcement_id', 'announcement_id', 'announcements', 'id');

CALL AddForeignKeyIfNotExists('forum_comments', 'FK_forum_comments_parent_comment_id', 'parent_comment_id', 'forum_comments', 'id');
CALL AddForeignKeyIfNotExists('forum_comments', 'FK_forum_comments_posted_by', 'posted_by', 'users', 'id');


