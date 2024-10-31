
CALL AddUniqueConstraintIfNotExists('files', 'UK_files_path', 'path');
CALL AddUniqueConstraintIfNotExists('tokens', 'UK_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('refresh_tokens', 'UK_refresh_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('confirm_tokens', 'UK_confirm_tokens_token', 'token');
CALL AddUniqueConstraintIfNotExists('users', 'UK_users_email', 'email');
CALL AddUniqueConstraintIfNotExists('users', 'UK_users_phone_number', 'phone_number');
CALL AddUniqueConstraintIfNotExists('athletes', 'UK_athlete_licence_id', 'licence_id');

-- Add foreign keys


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

CALL AddForeignKeyIfNotExists('files', 'FK_files_athlete_id', 'athlete_id', 'athletes', 'id');

CALL AddForeignKeyIfNotExists('event_performance', 'FK_event_performance_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('event_performance', 'FK_event_performance_event_id', 'event_id', 'events', 'id');

CALL AddForeignKeyIfNotExists('performances', 'FK_performances_athlete_id', 'athlete_id', 'athletes', 'id');
CALL AddForeignKeyIfNotExists('performances', 'FK_performances_created_by', 'created_by', 'admins', 'id');
CALL AddForeignKeyIfNotExists('performances', 'FK_performances_updated_by', 'updated_by', 'admins', 'id') ;
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_performance_id', 'performance_id',
                              'performances', 'id');
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_created_by', 'created_by', 'admins', 'id');
CALL AddForeignKeyIfNotExists('training_sessions', 'FK_training_sessions_updated_by', 'updated_by', 'admins', 'id') ;
