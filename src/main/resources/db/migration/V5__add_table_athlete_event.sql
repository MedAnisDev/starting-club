-- create table athlete_event
CREATE TABLE IF NOT EXISTS athlete_event (
    id BIGINT auto_increment primary key ,
    athlete_id BIGINT ,
    event_id BIGINT NOT NULL,
    note VARCHAR(255) DEFAULT 'NA'
);

-- add foreign key
CALL AddForeignKeyIfNotExists('athlete_event', 'FK_athlete_event_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('athlete_event', 'FK_athlete_event_event_id', 'event_id', 'events', 'id');