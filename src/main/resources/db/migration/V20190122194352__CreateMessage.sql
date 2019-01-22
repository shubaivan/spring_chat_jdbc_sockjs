CREATE TABLE messages (
  text TEXT,
  date_of_created TIMESTAMP NOT NULL,
  author_id BIGINT,
  relative_message_id BIGINT,
  relative_chat_id BIGINT,
  chat_id BIGINT
);

ALTER TABLE messages ADD COLUMN id SERIAL PRIMARY KEY NOT NULL;