-- create table athlete_event
CREATE TABLE IF NOT EXISTS event_performance (
    id BIGINT auto_increment primary key ,
    athlete_id BIGINT ,
    event_id BIGINT NOT NULL,
    note DOUBLE DEFAULT 0.00
);

-- add foreign key
CALL AddForeignKeyIfNotExists('event_performance', 'FK_event_performance_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('event_performance', 'FK_event_performance_event_id', 'event_id', 'events', 'id');