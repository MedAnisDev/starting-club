-- create tables
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


-- add indexes
CALL AddIndexIfNotExists('performances', 'idx_performances_athlete_id', 'athlete_id');
CALL AddIndexIfNotExists('performances', 'idx_performances_created_by', 'created_by');
CALL AddIndexIfNotExists('performances', 'idx_performances_updated_by', 'updated_by') ;
CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_performance_id', 'performance_id');
CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_created_by', 'created_by');
CALL AddIndexIfNotExists('training_sessions', 'idx_training_sessions_updated_by', 'updated_by');

-- add foreign keys
CALL AddForeignKeyIfNotExists('performances', 'FK_performances_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('performances', 'FK_performances_created_by', 'created_by', 'admins', 'id');
CALL AddForeignKeyIfNotExists('performances', 'FK_performances_updated_by', 'updated_by', 'admins', 'id') ;
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_performance_id', 'performance_id',
                              'performances', 'id');
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_created_by', 'created_by', 'admins', 'id');
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_updated_by', 'updated_by', 'admins', 'id') ;