CREATE TABLE chats_users (
  id BIGINT NOT NULL,
  chat_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  date_of_joined TIMESTAMP,
PRIMARY KEY (id)
);

ALTER TABLE chats_users
ADD CONSTRAINT FK_db_users
FOREIGN KEY (user_id)
REFERENCES db_users(id);

ALTER TABLE chats_users
ADD CONSTRAINT FK_chats
FOREIGN KEY (chat_id)
REFERENCES chats(id);