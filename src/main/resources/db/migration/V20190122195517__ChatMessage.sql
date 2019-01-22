ALTER TABLE messages
ADD CONSTRAINT FK_chats
FOREIGN KEY (chat_id)
REFERENCES chats(id);

ALTER TABLE messages
ADD CONSTRAINT FK_db_users
FOREIGN KEY (author_id)
REFERENCES db_users(id);