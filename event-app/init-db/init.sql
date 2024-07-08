CREATE TABLE events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    taxonomy VARCHAR(255) NOT NULL,
    version VARCHAR(10) NOT NULL
);

-- Insert the event 'Death'
INSERT INTO events (name, description, taxonomy, version) 
VALUES ('Death', 'Notification event that shows date and time of someone died', '/domain/service/x/y/z/a/b', '1.0');
