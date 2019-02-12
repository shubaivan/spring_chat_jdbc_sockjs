ALTER TABLE db_users ADD COLUMN is_enabled boolean;

CREATE TABLE confirmation_token (
  confirmation_token VARCHAR(200),
  created_at TIMESTAMP NOT NULL,
  user_id BIGINT
);

ALTER TABLE confirmation_token
ADD CONSTRAINT FK_db_users
FOREIGN KEY (user_id)
REFERENCES db_users(id);