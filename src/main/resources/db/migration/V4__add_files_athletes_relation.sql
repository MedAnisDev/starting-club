-- add column to table files
ALTER TABLE files
    ADD COLUMN athlete_id BIGINT ;

-- add foreign key
CALL AddForeignKeyIfNotExists('files', 'FK_files_athlete_id', 'athlete_id', 'athletes', 'id')