CREATE TABLE IF NOT EXISTS managers(
    manager_id BIGINT,
    manager_role varchar(40),
    CONSTRAINT UQ_MANAGER_ID UNIQUE (manager_id)
);

CREATE TABLE IF NOT EXISTS event_team_managers (
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
    manager_id BIGINT REFERENCES managers(manager_id) ON DELETE SET NULL
);