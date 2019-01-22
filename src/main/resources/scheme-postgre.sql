CREATE TABLE users (
  id BIGINT NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  user_name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  cretaed_at TIMESTAMP NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE chat (
  id BIGINT NOT NULL,
  name VARCHAR(255),
  type INT NOT NULL,
  cretaed_at TIMESTAMP NOT NULL,
  tags VARCHAR(255),
PRIMARY KEY (id)
);

CREATE TABLE chats_users (
  id BIGINT NOT NULL,
  chat_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE messages (
  id BIGINT NOT NULL,
  chat_id BIGINT NOT NULL,
  from_user_id BIGINT NOT NULL,
  cretaed_at BIGINT NOT NULL,
  relative_message_id BIGINT,
  forward_to_chat_id BIGINT,
  text VARCHAR(255) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE events (
  id BIGINT NOT NULL,
  name_event VARCHAR(255) NOT NULL,
  description VARCHAR(255) NOT NULL,
  start_event_at TIMESTAMP NOT NULL,
  finsih_event_at TIMESTAMP NOT NULL,
  author_id BIGINT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE events_users (
  id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE files_storage (
  id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  path VARCHAR(255) NOT NULL,
  content_type VARCHAR(255) NOT NULL,
  user_id BIGINT NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE files_chats (
  file_id BIGINT NOT NULL,
  chat_id BIGINT NOT NULL
);

CREATE TABLE favorite_message (
  id BIGINT NOT NULL,
  message_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  text VARCHAR(255) NOT NULL,
PRIMARY KEY (id)
);

CREATE TABLE file_messages (
  file_id BIGINT NOT NULL,
  message_id BIGINT NOT NULL
);

CREATE TABLE file_favorite_messages (
  file_id BIGINT NOT NULL,
  favorite_message_id BIGINT NOT NULL
);

ALTER TABLE chats_users ADD CONSTRAINT chats_users_fk0 FOREIGN KEY (chat_id) REFERENCES users(id);

ALTER TABLE chats_users ADD CONSTRAINT chats_users_fk1 FOREIGN KEY (user_id) REFERENCES chat(id);

ALTER TABLE messages ADD CONSTRAINT messages_fk0 FOREIGN KEY (chat_id) REFERENCES chat(id);

ALTER TABLE messages ADD CONSTRAINT messages_fk1 FOREIGN KEY (from_user_id) REFERENCES users(id);

ALTER TABLE messages ADD CONSTRAINT messages_fk2 FOREIGN KEY (relative_message_id) REFERENCES messages(id);

ALTER TABLE messages ADD CONSTRAINT messages_fk3 FOREIGN KEY (forward_to_chat_id) REFERENCES chat(id);

ALTER TABLE events ADD CONSTRAINT events_fk0 FOREIGN KEY (author_id) REFERENCES users(id);

ALTER TABLE events_users ADD CONSTRAINT events_users_fk0 FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE events_users ADD CONSTRAINT events_users_fk1 FOREIGN KEY (event_id) REFERENCES events(id);

ALTER TABLE files_storage ADD CONSTRAINT files_storage_fk0 FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE files_chats ADD CONSTRAINT files_chats_fk0 FOREIGN KEY (file_id) REFERENCES files_storage(id);

ALTER TABLE files_chats ADD CONSTRAINT files_chats_fk1 FOREIGN KEY (chat_id) REFERENCES chat(id);

ALTER TABLE favorite_message ADD CONSTRAINT favorite_message_fk0 FOREIGN KEY (message_id) REFERENCES messages(id);

ALTER TABLE favorite_message ADD CONSTRAINT favorite_message_fk1 FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE file_messages ADD CONSTRAINT file_messages_fk0 FOREIGN KEY (file_id) REFERENCES files_storage(id);

ALTER TABLE file_favorite_messages ADD CONSTRAINT file_favorite_messages_fk0 FOREIGN KEY (file_id) REFERENCES files_storage(id);