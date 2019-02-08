CREATE TABLE file_entities (
  name VARCHAR(150),
  path VARCHAR(200),
  content_type VARCHAR(150),
  created_at TIMESTAMP NOT NULL,
  owner_id BIGINT
);

ALTER TABLE file_entities ADD COLUMN id SERIAL PRIMARY KEY NOT NULL;

ALTER TABLE file_entities
ADD CONSTRAINT FK_db_users
FOREIGN KEY (owner_id)
REFERENCES db_users(id);